/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.ui.model;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.ISamples;
import org.eclipse.chemclipse.model.statistics.IVariable;

public abstract class AbstractSamplesVisualization<V extends IVariableVisualization, S extends ISampleVisualization> implements ISamplesVisualization<V, S>, ISamples<V, S> {

	public <VM extends IVariable, SM extends ISample> AbstractSamplesVisualization(ISamples<VM, SM> samplesModel) {

		super();
	}
}
