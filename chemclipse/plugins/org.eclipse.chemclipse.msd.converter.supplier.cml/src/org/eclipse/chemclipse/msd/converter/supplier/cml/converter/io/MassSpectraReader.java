/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.cml.converter.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;
import org.eclipse.chemclipse.model.core.MassSpectrumPeak;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.LibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.msd.converter.io.AbstractMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.io.IMassSpectraReader;
import org.eclipse.chemclipse.msd.converter.supplier.cml.model.IVendorLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.converter.supplier.cml.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.cml.model.VendorLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.IStandaloneMassSpectrum;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.msd.model.implementation.StandaloneMassSpectrum;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.io.RootElement;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Array;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Formula;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Metadata;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.MetadataList;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Molecule;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Name;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Peak;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.PeakList;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Sample;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Spectrum;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.SpectrumData;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.SpectrumType;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Xaxis;
import org.eclipse.chemclipse.xxd.converter.supplier.cml.model.v3.Yaxis;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class MassSpectraReader extends AbstractMassSpectraReader implements IMassSpectraReader {

	private static final Logger logger = Logger.getLogger(MassSpectraReader.class);

	@Override
	public IMassSpectra read(File file, IProgressMonitor monitor) throws IOException {

		IMassSpectra massSpectra = new MassSpectra();
		try {
			Spectrum spectrum = RootElement.getSpectrum(file);
			if(spectrum.getType() == SpectrumType.MASS_SPECTRUM) {
				PeakList peakList = spectrum.getPeakList();
				if(peakList != null) {
					IStandaloneMassSpectrum massSpectrum = new StandaloneMassSpectrum();
					massSpectrum.setFile(file);
					massSpectrum.setMassSpectrumType((short)1); // profile
					readIons(spectrum, massSpectrum);
					readPeaks(peakList, massSpectrum);
					massSpectra.addMassSpectrum(massSpectrum);
				} else {
					IVendorLibraryMassSpectrum massSpectrum = new VendorLibraryMassSpectrum();
					readIons(spectrum, massSpectrum);
					massSpectrum.setIdentifier(spectrum.getId());
					MetadataList metadataList = spectrum.getMetadataList();
					if(metadataList != null) {
						for(Metadata metadata : metadataList.getMetadata()) {
							if(metadata.getName().equals("dc:origin")) {
								massSpectrum.getLibraryInformation().setContributor(metadata.getValue());
							}
						}
					}
					Sample sample = spectrum.getSample();
					if(sample != null) {
						readSample(sample, massSpectrum);
					}
					massSpectra.addMassSpectrum(massSpectrum);
				}
			}
		} catch(SAXException | IOException | JAXBException
				| ParserConfigurationException e) {
			logger.warn(e);
		}
		return massSpectra;
	}

	private void readIons(Spectrum spectrum, IScanMSD scanMSD) {

		SpectrumData spectrumData = spectrum.getSpectrumData();
		List<Double> mzs = new ArrayList<>();
		List<Double> abundances = new ArrayList<>();
		if(spectrumData != null) {
			Xaxis xAxis = spectrumData.getXaxis();
			if(xAxis != null) {
				Array array = xAxis.getArray();
				if(!(array.getUnits().equals("unit:mz"))) {
					logger.warn("Unexpected mass-to-charge unit " + array.getUnits());
				}
				if(array != null) {
					mzs = Arrays.stream( //
							StringUtils.split(array.getValue())) //
							.map(Double::parseDouble) //
							.toList(); //
				}
			}
			Yaxis yAxis = spectrumData.getYaxis();
			if(yAxis != null) {
				Array array = yAxis.getArray();
				if(array != null) {
					if(!(array.getUnits().equals("cmls:relativeAbundance"))) {
						logger.warn("Unexpected abundance unit " + array.getUnits());
					}
					abundances = Arrays.stream( //
							StringUtils.split(array.getValue())) //
							.map(Double::parseDouble)//
							.toList();
				}
			}
			try {
				int scans = Math.min(mzs.size(), abundances.size());
				for(int i = 0; i < scans; i++) {
					double mz = mzs.get(i);
					float abundance = abundances.get(i).floatValue();
					if(yAxis != null && yAxis.getMultiplierToData() != null) {
						abundance = abundance * yAxis.getMultiplierToData().floatValue();
					}
					VendorIon ion = new VendorIon(mz, abundance);
					scanMSD.addIon(ion);
				}
			} catch(NumberFormatException e) {
				logger.warn(e);
			}
		}
	}

	private void readPeaks(PeakList peakList, IStandaloneMassSpectrum massSpectrum) {

		for(Peak peak : peakList.getPeak()) {
			IMassSpectrumPeak massSpectrumPeak = new MassSpectrumPeak();
			if(peak.getXValue() != null) {
				massSpectrumPeak.setIon(peak.getXValue());
			}
			if(peak.getYValue() != null) {
				massSpectrumPeak.setAbundance(peak.getYValue());
			}
			if(peak.getTitle() != null) {
				ILibraryInformation libraryInformation = new LibraryInformation();
				libraryInformation.setName(peak.getTitle());
				ComparisonResult comparisionResult = new ComparisonResult(1f, 1f, 1f, 1f);
				IdentificationTarget identificationTarget = new IdentificationTarget(libraryInformation, comparisionResult);
				identificationTarget.setIdentifier("CML import");
				massSpectrumPeak.getTargets().add(identificationTarget);
			}
			massSpectrum.getPeaks().add(massSpectrumPeak);
		}
	}

	private void readSample(Sample sample, IVendorLibraryMassSpectrum massSpectrum) {

		List<Molecule> molecules = sample.getMolecule();
		if(molecules != null) {
			for(Molecule molecule : molecules) {
				Formula formula = molecule.getFormula();
				if(formula.getConcise() != null) {
					massSpectrum.getLibraryInformation().setFormula(formula.getConcise());
				}
				if(formula.getInline() != null) {
					massSpectrum.getLibraryInformation().setFormula(formula.getInline());
				}
				Name name = molecule.getName();
				if(name != null) {
					if(name.getConvention().equals("cas:regno")) {
						massSpectrum.getLibraryInformation().addCasNumber(name.getValue());
					}
				}
			}
		}
	}
}
