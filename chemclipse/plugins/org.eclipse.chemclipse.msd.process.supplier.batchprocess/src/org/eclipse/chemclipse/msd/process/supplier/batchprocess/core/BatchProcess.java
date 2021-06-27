/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.core;

import java.io.File;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.msd.converter.processing.massspectrum.IMassSpectrumImportConverterProcessingInfo;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IBatchProcessJob;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumInputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumOutputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumProcessEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumReportSupplierEntry;
import org.eclipse.chemclipse.msd.process.support.ProcessTypeSupport;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Matthias Mailänder
 * 
 */
public class BatchProcess implements IBatchProcess {

	private Logger logger = Logger.getLogger(BatchProcess.class);
	private static final String DESCRIPTION = "Batch Processor";
	private ProcessTypeSupport processTypeSupport;

	public BatchProcess() {
		processTypeSupport = new ProcessTypeSupport();
	}

	@Override
	public IProcessingInfo execute(IBatchProcessJob batchProcessJob, IProgressMonitor monitor) {

		IProcessingInfo batchProcessingInfo = new ProcessingInfo();
		/*
		 * The batch process jobs must not be null.
		 */
		if(batchProcessJob == null) {
			batchProcessingInfo.addErrorMessage(DESCRIPTION, "The batch job was null.");
		} else {
			/*
			 * Process all entries.
			 * Input -> Process -> Output
			 */
			for(IMassSpectrumInputEntry massSpectrumInput : batchProcessJob.getMassSpectrumInputEntries()) {
				/*
				 * Get the mass spectrum.
				 */
				File massSpectrumInputFile = new File(massSpectrumInput.getInputFile());
				try {
					IMassSpectra massSpectra = loadMassSpectra(massSpectrumInputFile, batchProcessingInfo, monitor);
					processMassSpectra(massSpectra, batchProcessJob, batchProcessingInfo, monitor);
					batchProcessingInfo.addMessage(new ProcessingMessage(MessageType.INFO, DESCRIPTION, "The file has been processed successfully: " + massSpectrumInputFile));
				} catch(TypeCastException e) {
					batchProcessingInfo.addErrorMessage(DESCRIPTION, "A failure occurred fetching the chromatogram: " + massSpectrumInputFile);
				}
			}
		}
		return batchProcessingInfo;
	}

	private IMassSpectra loadMassSpectra(File massSpectrumInputFile, IProcessingInfo batchProcessingInfo, IProgressMonitor monitor) throws TypeCastException {

		IMassSpectrumImportConverterProcessingInfo processingInfo = MassSpectrumConverter.convert(massSpectrumInputFile, monitor);
		batchProcessingInfo.addMessages(processingInfo);
		IMassSpectra massSpectra = processingInfo.getMassSpectra();
		return massSpectra;
	}

	private void processMassSpectra(IMassSpectra massSpectra, IBatchProcessJob batchProcessJob, IProcessingInfo batchProcessingInfo, IProgressMonitor monitor) {

		/*
		 * The mass spectrum must be not null.
		 */
		if(massSpectra != null) {
			processMassSpectrumEntry(massSpectra, batchProcessJob, batchProcessingInfo, monitor);
			writeMassSpectrumOutputEntries(massSpectra, batchProcessJob, batchProcessingInfo, monitor);
			processMassSpectrumReportEntries(massSpectra, batchProcessJob, batchProcessingInfo, monitor);
		} else {
			batchProcessingInfo.addErrorMessage(DESCRIPTION, "The chromatogram must be not null.");
		}
	}

	/**
	 * Process the mass spectra selection with each process entry.
	 * 
	 * @param chromatogramSelection
	 * @param batchProcessJob
	 * @param batchProcessReport
	 * @param monitor
	 */
	private void processMassSpectrumEntry(IMassSpectra massSpectra, IBatchProcessJob batchProcessJob, IProcessingInfo batchProcessingInfo, IProgressMonitor monitor) {

		for(IMassSpectrumProcessEntry processEntry : batchProcessJob.getMassSpectrumProcessEntries()) {
			for(IScanMSD massSpectrum : massSpectra.getList()) {
				if(massSpectrum == null) {
					batchProcessingInfo.addErrorMessage(DESCRIPTION, "The mass spectrum must not be null.");
				} else {
					IProcessingInfo processingInfo = processTypeSupport.applyProcessor(massSpectrum, processEntry, monitor);
					batchProcessingInfo.addMessages(processingInfo);
				}
			}
		}
	}

	/**
	 * Write the mass spectrum output entries.
	 * 
	 * @param chromatogramSelection
	 * @param batchProcessJob
	 * @param batchProcessReport
	 * @param monitor
	 */
	private void writeMassSpectrumOutputEntries(IMassSpectra massSpectra, IBatchProcessJob batchProcessJob, IProcessingInfo batchProcessingInfo, IProgressMonitor monitor) {

		/*
		 * Write the mass spectrum to each listed output format.
		 */
		IProcessingInfo processingInfo = null;
		for(IMassSpectrumOutputEntry massSpectrumOutput : batchProcessJob.getMassSpectrumOutputEntries()) {
			/*
			 * Append the "/" or "\" to the end of the folder if not exists.
			 */
			String outputFolder = massSpectrumOutput.getOutputFolder();
			if(!outputFolder.endsWith(File.separator)) {
				outputFolder += File.separator;
			}
			/*
			 * Write the mass spectrum.
			 */
			File massSpectrumOutputFile = new File(outputFolder + massSpectra.getName());
			processingInfo = MassSpectrumConverter.convert(massSpectrumOutputFile, massSpectra, true, massSpectrumOutput.getConverterId(), monitor);
			batchProcessingInfo.addMessages(processingInfo);
		}
	}

	/**
	 * Process the mass spectrum report entries.
	 * 
	 * @param chromatogramSelection
	 * @param batchProcessJob
	 * @param batchProcessReport
	 * @param monitor
	 */
	private void processMassSpectrumReportEntries(IMassSpectra massSpectra, IBatchProcessJob batchProcessJob, IProcessingInfo batchProcessingInfo, IProgressMonitor monitor) {

		/*
		 * Report the mass spectrum by each selected report supplier.
		 */
		IProcessingInfo processingInfo = null;
		for(IMassSpectrumReportSupplierEntry massSpectrumReport : batchProcessJob.getMassSpectrumReportEntries()) {
			/*
			 * Append the reports?
			 */
			boolean appendReport;
			String reportFolderOrFile = massSpectrumReport.getReportFolderOrFile();
			File massSpectrumReportFile = new File(reportFolderOrFile);
			/*
			 * If it's a directory, then prepare the file name. Otherwise, the stored selection is the file name.
			 */
			if(massSpectrumReportFile.isDirectory()) {
				appendReport = false;
				//
				if(!reportFolderOrFile.endsWith(File.separator)) {
					reportFolderOrFile += File.separator;
				}
				massSpectrumReportFile = new File(reportFolderOrFile + massSpectra.getName());
			} else {
				appendReport = true;
			}
			/*
			 * Report the mass spectrum.
			 */
			// TODO:
			// processingInfo = MassSpectrumReports.generate(massSpectrumReportFile, appendReport, massSpectra, massSpectrumReport.getReportSupplierId(), monitor);
			batchProcessingInfo.addMessages(processingInfo);
		}
	}
}
