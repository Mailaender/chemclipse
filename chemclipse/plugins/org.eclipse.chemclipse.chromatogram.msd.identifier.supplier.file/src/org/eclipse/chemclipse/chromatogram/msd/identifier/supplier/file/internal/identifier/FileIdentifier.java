/*******************************************************************************
 * Copyright (c) 2015, 2016 Dr. Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.file.internal.identifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum.MassSpectrumComparator;
import org.eclipse.chemclipse.chromatogram.msd.comparison.processing.IMassSpectrumComparatorProcessingInfo;
import org.eclipse.chemclipse.chromatogram.msd.identifier.processing.IPeakIdentifierProcessingInfo;
import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.file.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.file.settings.IFileMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.chromatogram.msd.identifier.supplier.file.settings.IFilePeakIdentifierSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.model.identifier.IPeakIdentificationResults;
import org.eclipse.chemclipse.model.identifier.PeakIdentificationResults;
import org.eclipse.chemclipse.model.identifier.PeakLibraryInformation;
import org.eclipse.chemclipse.model.targets.IPeakTarget;
import org.eclipse.chemclipse.model.targets.PeakTarget;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.msd.converter.processing.massspectrum.IMassSpectrumImportConverterProcessingInfo;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularLibraryMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.core.identifier.massspectrum.IMassSpectrumComparisonResult;
import org.eclipse.chemclipse.msd.model.core.identifier.massspectrum.IMassSpectrumTarget;
import org.eclipse.chemclipse.msd.model.core.identifier.massspectrum.MassSpectrumLibraryInformation;
import org.eclipse.chemclipse.msd.model.core.identifier.massspectrum.MassSpectrumTarget;
import org.eclipse.chemclipse.msd.model.implementation.MassSpectra;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;

public class FileIdentifier {

	private static final Logger logger = Logger.getLogger(FileIdentifier.class);
	/*
	 * Don't reload the database on each request, only if neccessary.
	 */
	private static long fileSize = 0;
	private static String fileName = "";
	private static IMassSpectra massSpectraDatabase = null;

	public IMassSpectra runIdentification(List<IScanMSD> massSpectraList, IMassSpectrumIdentifierSettings massSpectrumIdentifierSettings, IProgressMonitor monitor) throws FileNotFoundException {

		IMassSpectra massSpectra = new MassSpectra();
		if(massSpectrumIdentifierSettings instanceof IFileMassSpectrumIdentifierSettings) {
			IFileMassSpectrumIdentifierSettings settings = (IFileMassSpectrumIdentifierSettings)massSpectrumIdentifierSettings;
			/*
			 * Run the identification.
			 */
			String comparatorId = settings.getMassSpectrumComparatorId();
			float minMatchFactor = settings.getMinMatchFactor();
			float minReverseMatchFactor = settings.getMinReverseMatchFactor();
			/*
			 * Load the mass spectra database only if the raw file or its content has changed.
			 */
			IMassSpectra database = getDatabase(settings.getMassSpectraFile(), monitor);
			/*
			 * Compare
			 */
			int countUnknown = 1;
			for(IScanMSD unknown : massSpectraList) {
				int countReference = 1;
				for(IScanMSD reference : database.getList()) {
					try {
						monitor.subTask("Compare " + countUnknown + " / " + countReference++);
						IMassSpectrumComparatorProcessingInfo infoCompare = MassSpectrumComparator.compare(unknown, reference, comparatorId);
						IMassSpectrumComparisonResult comparisonResult = infoCompare.getMassSpectrumComparisonResult();
						//
						if(comparisonResult.getMatchFactor() >= minMatchFactor && comparisonResult.getReverseMatchFactor() >= minReverseMatchFactor) {
							/*
							 * Add the target.
							 */
							unknown.addTarget(getMassSpectrumTarget(reference, comparisonResult));
							massSpectra.addMassSpectrum(unknown);
						}
					} catch(TypeCastException e1) {
						logger.warn(e1);
					}
				}
				countUnknown++;
			}
		} else {
			throw new FileNotFoundException("Can't get the file from the settings.");
		}
		//
		return massSpectra;
	}

	/**
	 * Run the peak identification.
	 * 
	 * @param peaks
	 * @param peakIdentifierSettings
	 * @param processingInfo
	 * @param monitor
	 * @return {@link IPeakIdentificationResults}
	 * @throws FileNotFoundException
	 */
	public IPeakIdentificationResults runPeakIdentification(List<IPeakMSD> peaks, IFilePeakIdentifierSettings peakIdentifierSettings, IPeakIdentifierProcessingInfo processingInfo, IProgressMonitor monitor) throws FileNotFoundException {

		IPeakIdentificationResults identificationResults = new PeakIdentificationResults();
		/*
		 * Run the identification.
		 */
		String comparatorId = peakIdentifierSettings.getMassSpectrumComparatorId();
		float minMatchFactor = peakIdentifierSettings.getMinMatchFactor();
		float minReverseMatchFactor = peakIdentifierSettings.getMinReverseMatchFactor();
		/*
		 * Load the mass spectra database only if the raw file or its content has changed.
		 */
		IMassSpectra database = getDatabase(peakIdentifierSettings.getMassSpectraFile(), monitor);
		/*
		 * Compare
		 */
		int countUnknown = 1;
		for(IPeakMSD peakMSD : peaks) {
			int countReference = 1;
			IScanMSD unknown = peakMSD.getPeakModel().getPeakMassSpectrum();
			for(IScanMSD reference : database.getList()) {
				try {
					monitor.subTask("Compare " + countUnknown + " / " + countReference++);
					IMassSpectrumComparatorProcessingInfo infoCompare = MassSpectrumComparator.compare(unknown, reference, comparatorId);
					IMassSpectrumComparisonResult comparisonResult = infoCompare.getMassSpectrumComparisonResult();
					//
					if(comparisonResult.getMatchFactor() >= minMatchFactor && comparisonResult.getReverseMatchFactor() >= minReverseMatchFactor) {
						/*
						 * Add the target.
						 */
						peakMSD.addTarget(getPeakTarget(reference, comparisonResult));
					}
				} catch(TypeCastException e1) {
					logger.warn(e1);
				}
			}
			countUnknown++;
		}
		return identificationResults;
	}

	private IMassSpectra getDatabase(String databasePath, IProgressMonitor monitor) throws FileNotFoundException {

		try {
			File file = new File(databasePath);
			if(file.exists()) {
				/*
				 * Make further checks.
				 */
				if(massSpectraDatabase == null) {
					loadMassSpectraFromFile(file, monitor);
				} else {
					/*
					 * Has the content been edited?
					 */
					if(file.length() != fileSize || !file.getName().equals(fileName)) {
						loadMassSpectraFromFile(file, monitor);
					}
				}
			} else {
				massSpectraDatabase = null;
			}
		} catch(TypeCastException e) {
			logger.warn(e);
		}
		//
		if(massSpectraDatabase == null) {
			throw new FileNotFoundException();
		}
		//
		return massSpectraDatabase;
	}

	private void loadMassSpectraFromFile(File file, IProgressMonitor monitor) throws TypeCastException {

		IMassSpectrumImportConverterProcessingInfo infoConvert = MassSpectrumConverter.convert(file, PreferenceSupplier.CONVERTER_ID, monitor);
		massSpectraDatabase = infoConvert.getMassSpectra();
		fileName = file.getName();
		fileSize = file.length();
	}

	// TODO Merge
	public IMassSpectrumTarget getMassSpectrumTarget(IScanMSD reference, IMassSpectrumComparisonResult comparisonResult) {

		String name = "???";
		String cas = "???";
		String comments = "???";
		//
		if(reference instanceof IRegularLibraryMassSpectrum) {
			IRegularLibraryMassSpectrum libraryMassSpectrum = (IRegularLibraryMassSpectrum)reference;
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			name = libraryInformation.getName();
			cas = libraryInformation.getCasNumber();
			comments = libraryInformation.getComments();
		}
		IMassSpectrumTarget identificationEntry = null;
		ILibraryInformation libraryInformation;
		/*
		 * Get the library information.
		 */
		libraryInformation = new MassSpectrumLibraryInformation();
		libraryInformation.setName(name);
		libraryInformation.setCasNumber(cas);
		libraryInformation.setMiscellaneous(comments);
		//
		try {
			identificationEntry = new MassSpectrumTarget(libraryInformation, comparisonResult);
		} catch(ReferenceMustNotBeNullException e) {
			logger.warn(e);
		}
		return identificationEntry;
	}

	// TODO Merge
	public IPeakTarget getPeakTarget(IScanMSD reference, IMassSpectrumComparisonResult comparisonResult) {

		String name = "???";
		String cas = "???";
		String comments = "???";
		//
		if(reference instanceof IRegularLibraryMassSpectrum) {
			IRegularLibraryMassSpectrum libraryMassSpectrum = (IRegularLibraryMassSpectrum)reference;
			ILibraryInformation libraryInformation = libraryMassSpectrum.getLibraryInformation();
			name = libraryInformation.getName();
			cas = libraryInformation.getCasNumber();
			comments = libraryInformation.getComments();
		}
		IPeakTarget identificationEntry = null;
		ILibraryInformation libraryInformation;
		/*
		 * Get the library information.
		 */
		libraryInformation = new PeakLibraryInformation();
		libraryInformation.setName(name);
		libraryInformation.setCasNumber(cas);
		libraryInformation.setMiscellaneous(comments);
		//
		try {
			identificationEntry = new PeakTarget(libraryInformation, comparisonResult);
		} catch(ReferenceMustNotBeNullException e) {
			logger.warn(e);
		}
		return identificationEntry;
	}
}
