/*******************************************************************************
 * Copyright (c) 2008, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.baseline.correction;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.settings.IBaselineDetectorSettings;
import org.eclipse.chemclipse.model.baseline.IBaselineModel;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IBaselineCorrection {

	/**
	 * All classes which implement this interface are responsible to set a
	 * baseline to the mass spectrum.<br/>
	 * How they will calculate the baseline (automatically, manually) is their
	 * on scope.<br/>
	 * Use {@link IScanMSD} and {@link IBaselineModel} which can be
	 * retrieved from the mass spectrum instance.
	 * 
	 * @param mass
	 *            spectrum
	 * @param baselineDetectorSettings
	 * @param monitor
	 * @return {@link IBaselineCorrectionProcessingInfo}
	 */
	IBaselineCorrectionProcessingInfo setBaseline(IScanMSD massSpectrum, IBaselineCorrectionSettings baselineCorrectionSettings, IProgressMonitor monitor);

	/**
	 * This class does the same as the other setBaseline method but does not require settings.<br/>
	 * 
	 * @param mass
	 *            spectrum
	 * @param monitor
	 * @return {@link IBaselineCorrectionProcessingInfo}
	 */
	IBaselineCorrectionProcessingInfo setBaseline(IScanMSD massSpectrum, IProgressMonitor monitor);

	/**
	 * Validates the parameters.
	 * 
	 * @param massSpectrum
	 * @param baselineDetectorSettings
	 * @param monitor
	 * @return {@link IBaselineCorrectionProcessingInfo}
	 */
	IBaselineCorrectionProcessingInfo validate(IScanMSD massSpectrum, IBaselineDetectorSettings baselineDetectorSettings, IProgressMonitor monitor);
}
