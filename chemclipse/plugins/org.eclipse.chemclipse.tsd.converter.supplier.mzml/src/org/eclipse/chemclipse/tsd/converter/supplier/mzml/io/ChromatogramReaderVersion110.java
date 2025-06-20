package org.eclipse.chemclipse.tsd.converter.supplier.mzml.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.model.implementation.Scan;
import org.eclipse.chemclipse.tsd.converter.supplier.mzml.model.IVendorChromatogram;
import org.eclipse.chemclipse.tsd.converter.supplier.mzml.model.VendorChromatogram;
import org.eclipse.chemclipse.tsd.model.core.IChromatogramTSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.BinaryReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.MetadataReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.BinaryDataArrayType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.CVParamType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.MzMLType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.RunType;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.model.v110.SpectrumType;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;

public class ChromatogramReaderVersion110 {

	private static final Logger logger = Logger.getLogger(ChromatogramReaderVersion110.class);

	public IChromatogramOverview readOverview(InputStream inputStream, IProgressMonitor monitor) throws IOException {

		IVendorChromatogram chromatogram = null;
		try {
			chromatogram = new VendorChromatogram();
			MzMLType mzML = XmlReader110.getMzML(inputStream);
			chromatogram = (IVendorChromatogram)MetadataReader110.readMetadata(mzML, chromatogram);
			RunType run = mzML.getRun();
			readTotalIonCurrent(run, chromatogram);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		}
		return chromatogram;
	}

	public IChromatogramTSD read(InputStream inputStream, IProgressMonitor monitor) throws IOException {

		VendorChromatogram chromatogram = null;
		try {
			chromatogram = new VendorChromatogram();
			MzMLType mzML = XmlReader110.getMzML(inputStream);
			chromatogram = (VendorChromatogram)MetadataReader110.readMetadata(mzML, chromatogram);
			RunType run = mzML.getRun();
			readIonMobility(run, chromatogram);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		}
		return chromatogram;
	}

	private void readTotalIonCurrent(RunType run, IVendorChromatogram chromatogram) {

		for(SpectrumType spectrumType : run.getSpectrumList().getSpectrum()) {
			for(CVParamType cvParam : spectrumType.getCvParam()) {
				if(cvParam.getAccession().equals("MS:1000285") && cvParam.getName().equals("total ion current")) {
					float tic = Float.parseFloat(cvParam.getValue());
					chromatogram.addScan(new Scan(tic));
				}
			}
		}
	}

	private void readIonMobility(RunType run, IVendorChromatogram chromatogram) {

		List<Float> driftTimes = new ArrayList<>();
		double[] mzs = new double[0];
		double[] intensities = new double[0];
		try {
			for(SpectrumType spectrumType : run.getSpectrumList().getSpectrum()) {
				for(CVParamType cvParam : spectrumType.getCvParam()) {
					if(cvParam.getAccession().equals("MS:1002476") && cvParam.getName().equals("ion mobility drift time")) {
						float driftTime = Float.parseFloat(cvParam.getValue());
					} else if(cvParam.getAccession().equals("MS:1002476") && cvParam.getName().equals("absorption chromatogram")) {
						for(BinaryDataArrayType binaryDataArrayType : spectrumType.getBinaryDataArrayList().getBinaryDataArray()) {
							Pair<String, double[]> binaryData = BinaryReader110.parseBinaryData(binaryDataArrayType);
							if(binaryData.getKey().equals("m/z")) {
								mzs = binaryData.getValue();
							} else if(binaryData.getKey().equals("intensity")) {
								intensities = binaryData.getValue();
							}
						}
					}
				}
			}
			addScans(driftTimes, intensities, mzs, chromatogram);
		} catch(DataFormatException e) {
			logger.warn(e);
		}
	}

}
