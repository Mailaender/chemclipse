/*******************************************************************************
 * Copyright (c) 2018, 2025 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - rework dirty flag handling
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.ui.editors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.core.BatchProcess;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.io.BatchProcessJobWriter;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.BatchProcessJob;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IBatchProcessJob;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumInputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.MassSpectrumInputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.ui.Activator;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.ui.internal.runnables.ImportRunnable;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.methods.ProcessMethod;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplierContext;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoPartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.BatchJobUI;
import org.eclipse.chemclipse.xxd.process.support.ProcessTypeSupport;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class BatchJobEditor extends EditorPart implements IRunnableWithProgress {

	private static final Logger logger = Logger.getLogger(BatchJobEditor.class);

	private BatchJobUI batchJobUI;
	private File file;
	private boolean isDirty = false;
	private IBatchProcessJob batchProcessJob;

	private IProcessSupplierContext supplierContext;

	@Override
	public void doSave(IProgressMonitor monitor) {

		if(file != null) {
			BatchProcessJobWriter writer = new BatchProcessJobWriter();
			try {
				batchProcessJob = getBatchProcessJob();
				writer.writeBatchProcessJob(file, batchProcessJob, monitor);
				updateDirtyStatus(false);
			} catch(FileNotFoundException e) {
				logger.warn(e);
			} catch(IOException e) {
				logger.warn(e);
			} catch(XMLStreamException e) {
				logger.warn(e);
			} catch(FileIsNotWriteableException e) {
				logger.warn(e);
			}
		}
	}

	@Override
	public void doSaveAs() {

		// TODO
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		setSite(site);
		setInput(input);

		String fileName = input.getName();
		fileName = fileName.substring(0, fileName.length() - 4);
		setPartName(fileName);

		if(batchProcessJob == null && input instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput)input;
			file = fileEditorInput.getFile().getLocation().toFile();

			ImportRunnable runnable = new ImportRunnable(file);
			ProgressMonitorDialog monitor = new ProgressMonitorDialog(site.getShell());
			try {
				monitor.run(false, false, runnable);
				batchProcessJob = runnable.getBatchProcessJob();
			} catch(InvocationTargetException e) {
				throw new PartInitException("The file could't be loaded.", e.getTargetException());
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		} else {
			throw new PartInitException("The file could't be loaded.");
		}
	}

	@Override
	public boolean isDirty() {

		return isDirty;
	}

	/**
	 * Sets the editor dirty.
	 */
	protected void updateDirtyStatus(boolean dirty) {

		this.isDirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {

		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());

		supplierContext = new ProcessTypeSupport();
		batchJobUI = new BatchJobUI(parent, supplierContext, Activator.getDefault().getPreferenceStore(), PreferenceSupplier.P_FILTER_PATH_IMPORT_RECORDS, DataType.MALDI, this);
		batchJobUI.setModificationHandler(this::updateDirtyStatus);
		batchJobUI.doLoad(getBatchJobFiles(), new ProcessMethod(batchProcessJob.getProcessMethod()));
	}

	@Override
	public void setFocus() {

		batchJobUI.setFocus();
	}

	private List<File> getBatchJobFiles() {

		List<IMassSpectrumInputEntry> massSpectrumInputEntries = batchProcessJob.getMassSpectrumInputEntries();
		List<File> files = new ArrayList<>();
		for(IMassSpectrumInputEntry entry : massSpectrumInputEntries) {
			files.add(new File(entry.getInputFile()));
		}

		return files;
	}

	private IBatchProcessJob getBatchProcessJob() {

		IBatchProcessJob job = new BatchProcessJob();
		List<IMassSpectrumInputEntry> entries = job.getMassSpectrumInputEntries();
		for(File file : batchJobUI.getDataList().getFiles()) {
			entries.add(new MassSpectrumInputEntry(file.getAbsolutePath()));
		}
		return job;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		BatchProcess batchProcess = new BatchProcess();
		IProcessingInfo<?> processingInfo = batchProcess.execute(getBatchProcessJob(), monitor);
		ProcessingInfoPartSupport.getInstance().update(processingInfo);
	}
}