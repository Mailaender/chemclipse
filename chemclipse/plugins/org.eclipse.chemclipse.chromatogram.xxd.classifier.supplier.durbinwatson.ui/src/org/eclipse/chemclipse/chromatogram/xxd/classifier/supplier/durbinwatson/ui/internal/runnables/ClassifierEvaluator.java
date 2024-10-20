/*******************************************************************************
 * Copyright (c) 2015, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.classifier.supplier.durbinwatson.ui.internal.runnables;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.chromatogram.msd.classifier.core.ChromatogramClassifier;
import org.eclipse.chemclipse.model.processor.AbstractChromatogramProcessor;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.ui.support.ProcessingInfoViewSupport;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ClassifierEvaluator extends AbstractChromatogramProcessor implements IRunnableWithProgress {

	private static final String DESCRIPTION = "Durbin-Watson Classifier";
	private static final String FILTER_ID = "org.eclipse.chemclipse.chromatogram.xxd.classifier.supplier.durbinwatson";

	public ClassifierEvaluator(IChromatogramSelectionMSD chromatogramSelection) {
		super(chromatogramSelection);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(IProgressMonitor monitor) {

		final IChromatogramSelection chromatogramSelection = getChromatogramSelection();
		if(chromatogramSelection instanceof IChromatogramSelectionMSD) {
			IProcessingInfo processingInfo = ChromatogramClassifier.applyClassifier((IChromatogramSelectionMSD)chromatogramSelection, FILTER_ID, monitor);
			ProcessingInfoViewSupport.updateProcessingInfo(processingInfo, false);
			chromatogramSelection.update(true);
		}
	}

	@Override
	public String getDescription() {

		return DESCRIPTION;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		/*
		 * The doOperation calls the execute of the chromatogram inspector
		 * method.<br/> Why not doing it directly? Model and GUI should be
		 * handled separately.
		 */
		try {
			monitor.beginTask("Durbin-Watson Classifier", IProgressMonitor.UNKNOWN);
			getChromatogramSelection().getChromatogram().doOperation(this, false, false, monitor);
		} finally {
			monitor.done();
		}
	}
}
