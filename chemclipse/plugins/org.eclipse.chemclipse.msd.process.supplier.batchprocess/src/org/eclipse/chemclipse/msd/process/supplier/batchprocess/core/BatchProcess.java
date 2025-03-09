/*******************************************************************************
 * Copyright (c) 2010, 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - propagate errors/infos from processors to the user
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.core;

import java.io.File;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.supplier.IScanProcessSupplier;
import org.eclipse.chemclipse.msd.converter.massspectrum.MassSpectrumConverter;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.process.supplier.batchprocess.core.BatchProcessJob;
import org.eclipse.chemclipse.process.supplier.batchprocess.io.IBatchProcessInputEntry;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.core.runtime.IProgressMonitor;

public class BatchProcess {

	private static final Logger logger = Logger.getLogger(BatchProcess.class);
	private static final String DESCRIPTION = "Batch Processor";

	private final IProcessSupplierContext processSupplierContext;

	public BatchProcess(IProcessSupplierContext processSupplierContext) {

		this.processSupplierContext = processSupplierContext;
	}

	public IProcessingInfo<?> execute(BatchProcessJob batchProcessJob, IProgressMonitor monitor) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		/*
		 * The batch process jobs must not be null.
		 */
		if(batchProcessJob == null || batchProcessJob.getProcessMethod() == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The batch job and/or process method was null.");
		} else {
			IProcessMethod processMethod = batchProcessJob.getProcessMethod();
			for(IBatchProcessInputEntry batchProcessInput : batchProcessJob.getBatchProcessInputEntries()) {
				File file = new File(batchProcessInput.getInputFile());
				try {
					IProcessingInfo<IMassSpectra> processingInfoImport = MassSpectrumConverter.convert(file, monitor);
					if(!processingInfoImport.hasErrorMessages()) {
						IMassSpectra massSpectra = processingInfoImport.getProcessingResult();
						for(IScanMSD scanMSD : massSpectra.getList()) {
							ProcessingInfo<?> processorResult = new ProcessingInfo<>();
							ProcessEntryContainer.applyProcessEntries(processMethod, new ProcessExecutionContext(monitor, processorResult, processSupplierContext), IScanProcessSupplier.createConsumer(scanMSD));
							if(processorResult.hasErrorMessages()) {
								processingInfo.addErrorMessage(DESCRIPTION, "Processing: " + file + " failed");
							} else {
								processingInfo.addInfoMessage(DESCRIPTION, "Processing: " + file + " completed");
							}
							processingInfo.addMessages(processorResult);
						}
					} else {
						processingInfo.addErrorMessage(DESCRIPTION, "Failure to process: " + file);
					}
				} catch(TypeCastException e) {
					logger.warn(e);
					processingInfo.addErrorMessage(DESCRIPTION, "Failure to process: " + file);
				}
			}
		}
		return processingInfo;
	}
}
