/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.ui.internal.provider;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.result.IDenoisingFilterResult;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ResultsContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {

		if(inputElement instanceof IMeasurementResult) {
			IMeasurementResult measurementResult = (IMeasurementResult)inputElement;
			Object object = measurementResult.getResult();
			if(object instanceof IDenoisingFilterResult) {
				return ((IDenoisingFilterResult)object).getNoiseMassSpectra().toArray();
			}
		}
		//
		return null;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}
}
