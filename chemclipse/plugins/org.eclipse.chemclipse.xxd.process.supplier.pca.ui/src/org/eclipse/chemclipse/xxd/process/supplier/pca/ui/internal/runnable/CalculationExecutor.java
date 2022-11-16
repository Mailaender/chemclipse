/*******************************************************************************
 * Copyright (c) 2020, 2022 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.ui.internal.runnable;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.IVariable;
import org.eclipse.chemclipse.xxd.process.supplier.pca.core.ProcessorPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.exception.MathIllegalArgumentException;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.EvaluationPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ISamplesPCA;
import org.eclipse.chemclipse.xxd.process.supplier.pca.model.ResultsPCA;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class CalculationExecutor implements IRunnableWithProgress {

	private static final Logger logger = Logger.getLogger(CalculationExecutor.class);
	//
	private ISamplesPCA<? extends IVariable, ? extends ISample> samples = null;
	private EvaluationPCA masterEvaluationPCA = null;
	//
	private EvaluationPCA evaluationPCA = null;

	public CalculationExecutor(ISamplesPCA<? extends IVariable, ? extends ISample> samples, EvaluationPCA masterEvaluationPCA) {

		this.samples = samples;
		this.masterEvaluationPCA = masterEvaluationPCA;
	}

	public EvaluationPCA getEvaluationPCA() {

		return evaluationPCA;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

		if(samples != null) {
			try {
				ProcessorPCA processorPCA = new ProcessorPCA();
				ResultsPCA results = processorPCA.process(samples, masterEvaluationPCA, monitor);
				evaluationPCA = new EvaluationPCA(samples, results);
			} catch(MathIllegalArgumentException e) {
				logger.error(e.getLocalizedMessage(), e);
			} catch(Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}
}
