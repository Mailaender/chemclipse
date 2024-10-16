/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 *
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.massspectrum;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.identifier.settings.IMassSpectrumIdentifierSettings;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IMassSpectrumIdentifier {

	/**
	 * Identifies a mass spectrum.
	 *
	 * @param massSpectrum
	 *            the mass spectrum to be identified
	 * @param massSpectrumIdentifierSettings
	 *            the settings that describe details of identification
	 *            configuration
	 * @param monitor
	 *            a {@link IProgressMonitor monitor} to monitor progress and
	 *            provide cancellation functionality
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<IMassSpectra> identify(IScanMSD massSpectrum, IMassSpectrumIdentifierSettings massSpectrumIdentifierSettings, IProgressMonitor monitor);

	/**
	 * Identifies a mass spectrum.
	 *
	 * @param massSpectrum
	 *            the mass spectrum to be identified
	 * @param monitor
	 *            a {@link IProgressMonitor monitor} to monitor progress and
	 *            provide cancellation functionality
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<IMassSpectra> identify(IScanMSD massSpectrum, IProgressMonitor monitor);

	/**
	 * Identifies a list of mass spectra.
	 *
	 * @param massSpectra
	 *            the mass spectra to be identified
	 * @param massSpectrumIdentifierSettings
	 * @param monitor
	 *            a {@link IProgressMonitor monitor} to monitor progress and
	 *            provide cancellation functionality
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<IMassSpectra> identify(List<IScanMSD> massSpectra, IMassSpectrumIdentifierSettings massSpectrumIdentifierSettings, IProgressMonitor monitor);

	/**
	 * Identifies a list of mass spectra.
	 *
	 * @param massSpectra
	 *            the mass spectrum to be identified
	 * @param monitor
	 *            a {@link IProgressMonitor monitor} to monitor progress and
	 *            provide cancellation functionality
	 * @return {@link IProcessingInfo}
	 */
	IProcessingInfo<IMassSpectra> identify(List<IScanMSD> massSpectra, IProgressMonitor monitor);
}
