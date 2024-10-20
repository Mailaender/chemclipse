/*******************************************************************************
 * Copyright (c) 2011, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.process.supplier.batchprocess.ui.internal.runnables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.stream.XMLStreamException;

import org.eclipse.chemclipse.chromatogram.msd.process.supplier.batchprocess.io.JobWriter;
import org.eclipse.chemclipse.chromatogram.msd.process.supplier.batchprocess.model.BatchProcessJob;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ExportRunnable implements IRunnableWithProgress {

	private File file;
	private BatchProcessJob batchProcessJob;

	public ExportRunnable(File file, BatchProcessJob batchProcessJob) {
		this.file = file;
		this.batchProcessJob = batchProcessJob;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		try {
			JobWriter writer = new JobWriter();
			writer.writeBatchProcessJob(file, batchProcessJob, monitor);
		} catch(FileNotFoundException e) {
			throw new InterruptedException("The file " + file.getPath() + " couldn't be found.");
		} catch(FileIsNotWriteableException e) {
			throw new InterruptedException("The file " + file.getPath() + " is not writable.");
		} catch(IOException e) {
			throw new InterruptedException("The file " + file.getPath() + " makes problems.");
		} catch(XMLStreamException e) {
			throw new InterruptedException("There is a problem writing the file " + file.getPath());
		}
	}
}
