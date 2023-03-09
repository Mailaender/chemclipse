/*******************************************************************************
 * Copyright (c) 2016, 2023 Lablicate GmbH.
 *
 * All rights reserved.
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.workflows.ui.runnables;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ImportChromatogramRunnable implements IRunnableWithProgress {

	private String pathChromatogram;
	private IChromatogramMSD chromatogramMSD;

	public ImportChromatogramRunnable(String pathChromatogram) {

		this.pathChromatogram = pathChromatogram;
	}

	public IChromatogramMSD getChromatogram() {

		return chromatogramMSD;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		File chromatogramImportFile = new File(pathChromatogram);
		IProcessingInfo<IChromatogramMSD> processingInfoImport = ChromatogramConverterMSD.getInstance().convert(chromatogramImportFile, monitor);
		chromatogramMSD = processingInfoImport.getProcessingResult();
	}
}
