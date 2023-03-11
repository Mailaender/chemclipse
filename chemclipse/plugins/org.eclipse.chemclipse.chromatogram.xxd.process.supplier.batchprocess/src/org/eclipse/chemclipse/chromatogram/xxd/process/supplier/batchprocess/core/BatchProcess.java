/*******************************************************************************
 * Copyright (c) 2010, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - propagate errors/infos from processors to the user
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.batchprocess.core;

import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.batchprocess.model.BatchProcessJob;
import org.eclipse.chemclipse.converter.model.IChromatogramInputEntry;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.ChromatogramIsNullException;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.model.supplier.IChromatogramSelectionProcessSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.processing.core.exceptions.TypeCastException;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.processing.supplier.ProcessExecutionContext;
import org.eclipse.chemclipse.xxd.process.support.ChromatogramTypeSupport;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.core.runtime.IProgressMonitor;

public class BatchProcess {

	private static final Logger logger = Logger.getLogger(BatchProcess.class);
	private static final String DESCRIPTION = "Batch Processor";
	//
	private final ChromatogramTypeSupport chromatogramTypeSupport;
	private final IProcessSupplierContext processSupplierContext;

	@Deprecated
	public BatchProcess() {

		this(new DataType[]{DataType.CSD, DataType.MSD, DataType.WSD}, new ProcessTypeSupport());
	}

	public BatchProcess(DataType[] dataTypes, IProcessSupplierContext processSupplierContext) {

		this.processSupplierContext = processSupplierContext;
		chromatogramTypeSupport = new ChromatogramTypeSupport(dataTypes);
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
			for(IChromatogramInputEntry chromatogramInput : batchProcessJob.getChromatogramInputEntries()) {
				String pathChromatogram = chromatogramInput.getInputFile();
				try {
					IProcessingInfo<IChromatogramSelection<?, ?>> processingInfoX = chromatogramTypeSupport.getChromatogramSelection(pathChromatogram, monitor);
					if(!processingInfoX.hasErrorMessages()) {
						IChromatogramSelection<?, ?> chromatogramSelection = processingInfoX.getProcessingResult();
						ProcessingInfo<?> processorResult = new ProcessingInfo<>();
						ProcessEntryContainer.applyProcessEntries(processMethod, new ProcessExecutionContext(monitor, processorResult, processSupplierContext), IChromatogramSelectionProcessSupplier.createConsumer(chromatogramSelection));
						if(processorResult.hasErrorMessages()) {
							processingInfo.addErrorMessage(DESCRIPTION, "Processing: " + pathChromatogram + " failed");
						} else {
							processingInfo.addInfoMessage(DESCRIPTION, "Processing: " + pathChromatogram + " completed");
						}
						processingInfo.addMessages(processorResult);
					} else {
						processingInfo.addErrorMessage(DESCRIPTION, "Failure to process: " + pathChromatogram);
					}
				} catch(TypeCastException e) {
					logger.warn(e);
					processingInfo.addErrorMessage(DESCRIPTION, "Failure to process: " + pathChromatogram);
				} catch(ChromatogramIsNullException e) {
					logger.error(e);
					processingInfo.addErrorMessage(DESCRIPTION, "Chromatogram is empty: " + pathChromatogram);
				}
			}
		}
		return processingInfo;
	}
}
