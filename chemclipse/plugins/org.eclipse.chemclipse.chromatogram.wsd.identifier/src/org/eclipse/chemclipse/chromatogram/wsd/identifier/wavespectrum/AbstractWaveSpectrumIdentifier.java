/*******************************************************************************
 * Copyright (c) 2010, 2021 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.identifier.wavespectrum;

import org.eclipse.chemclipse.chromatogram.wsd.identifier.settings.IIdentifierSettingsWSD;
import org.eclipse.chemclipse.model.exceptions.ValueMustNotBeNullException;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;

public abstract class AbstractWaveSpectrumIdentifier implements IWaveSpectrumIdentifier {

	/**
	 * Validates that the peak is not null.<br/>
	 * If yes, an exception will be thrown.
	 * 
	 * @param peak
	 * @throws ValueMustNotBeNullException
	 */
	public void validateWaveSpectrum(IScanWSD waveSpectrum) throws ValueMustNotBeNullException {

		if(waveSpectrum == null) {
			throw new ValueMustNotBeNullException("The wave spectrum must not be null.");
		}
	}

	/**
	 * Throws an exception if the settings are null.
	 * 
	 * @param identifierSettings
	 * @throws ValueMustNotBeNullException
	 */
	public void validateSettings(IIdentifierSettingsWSD identifierSettings) throws ValueMustNotBeNullException {

		if(identifierSettings == null) {
			throw new ValueMustNotBeNullException("The identifier settings must not be null.");
		}
	}
}
