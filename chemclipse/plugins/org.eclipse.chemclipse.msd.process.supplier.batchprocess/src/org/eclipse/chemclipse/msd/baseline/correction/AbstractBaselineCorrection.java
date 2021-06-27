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
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.baseline.correction;

import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class AbstractBaselineCorrection implements IBaselineCorrection {

	private static final String ERROR_DESCRIPTION = "Baseline Correction";

	public IBaselineCorrectionProcessingInfo validate(IScanMSD massSpectrum, IBaselineCorrectionSettings baselineDetectorSettings, IProgressMonitor monitor) {

		IProcessingMessage processingMessage;
		IBaselineCorrectionProcessingInfo processingInfo = new BaselineCorrectionProcessingInfo();
		if(massSpectrum == null) {
			processingMessage = new ProcessingMessage(MessageType.ERROR, ERROR_DESCRIPTION, "The mass spectra are invalid.");
			processingInfo.addMessage(processingMessage);
		}
		/*
		 * Settings
		 */
		if(baselineDetectorSettings == null) {
			processingMessage = new ProcessingMessage(MessageType.ERROR, ERROR_DESCRIPTION, "The baseline correction settings are invalid.");
			processingInfo.addMessage(processingMessage);
		}
		return processingInfo;
	}
}
