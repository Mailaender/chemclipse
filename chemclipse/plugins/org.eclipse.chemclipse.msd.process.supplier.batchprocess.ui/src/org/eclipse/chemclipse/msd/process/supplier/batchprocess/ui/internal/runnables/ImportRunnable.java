/*******************************************************************************
 * Copyright (c) 2011, 2025 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.ui.internal.runnables;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.msd.process.supplier.batchprocess.io.BatchProcessJobReader;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IBatchProcessJob;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ImportRunnable implements IRunnableWithProgress {

	private File file;
	private IBatchProcessJob batchProcessJob = null;

	public ImportRunnable(File file) {

		this.file = file;
	}

	public IBatchProcessJob getBatchProcessJob() {

		return batchProcessJob;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			BatchProcessJobReader jobReader = new BatchProcessJobReader();
			batchProcessJob = jobReader.read(file, monitor);
		} catch(Exception e) {
			throw new InterruptedException("Failed to process the file: " + file.getPath() + ".");
		}
	}
}