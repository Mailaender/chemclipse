/*******************************************************************************
 * Copyright (c) 2012, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core.BaselineDetector;
import org.eclipse.chemclipse.msd.baseline.correction.BaselineCorrection;
import org.eclipse.chemclipse.msd.baseline.correction.IBaselineCorrectionSupplier;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.process.support.IProcessTypeSupplier;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class BaselineCorrectionTypeSupplier extends AbstractProcessTypeSupplier implements IProcessTypeSupplier {

	public static final String CATEGORY = "Baseline Correction";

	@Override
	public String getCategory() {

		return CATEGORY;
	}

	@Override
	public String getProcessorName(String processorId) throws Exception {

		IBaselineCorrectionSupplier baselineSupplier = BaselineCorrection.getBaselineCorrectionSupport().getBaselineCorrectionSupplier(processorId);
		return baselineSupplier.getDetectorName();
	}

	@Override
	public List<String> getPluginIds() throws Exception {

		return BaselineDetector.getBaselineDetectorSupport().getAvailableDetectorIds();
	}

	@Override
	public IProcessingInfo applyProcessor(IScanMSD massSpectrum, String processorId, IProgressMonitor monitor) {

		return BaselineCorrection.setBaseline(massSpectrum, processorId, monitor);
	}
}
