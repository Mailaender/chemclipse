/*******************************************************************************
 * Copyright (c) 2018, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.runnables;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.l10n.ExtensionMessages;
import org.eclipse.chemclipse.vsd.converter.core.ScanConverterVSD;
import org.eclipse.chemclipse.vsd.model.core.ISpectrumVSD;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ScanVSDImportRunnable implements IRunnableWithProgress {

	private static final Logger logger = Logger.getLogger(ScanVSDImportRunnable.class);
	//
	private File file;
	private ISpectrumVSD spectrumVSD = null;

	public ScanVSDImportRunnable(File file) {

		this.file = file;
	}

	public ISpectrumVSD getSpectrumVSD() {

		return spectrumVSD;
	}

	@Override
	public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			monitor.beginTask(ExtensionMessages.importScan, IProgressMonitor.UNKNOWN);
			IProcessingInfo<?> processingInfo = ScanConverterVSD.convert(file, monitor);
			ProcessingInfoPartSupport.getInstance().update(processingInfo);
			spectrumVSD = (ISpectrumVSD)processingInfo.getProcessingResult();
		} catch(Exception e) {
			logger.error(e);
		} finally {
			monitor.done();
		}
	}
}