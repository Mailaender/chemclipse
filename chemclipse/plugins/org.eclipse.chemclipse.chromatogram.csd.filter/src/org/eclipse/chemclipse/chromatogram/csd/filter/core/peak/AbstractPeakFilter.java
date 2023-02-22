/*******************************************************************************
 * Copyright (c) 2015, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.csd.filter.core.peak;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.csd.filter.l10n.Messages;
import org.eclipse.chemclipse.chromatogram.filter.settings.IPeakFilterSettings;
import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.csd.model.core.selection.IChromatogramSelectionCSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;

public abstract class AbstractPeakFilter implements IPeakFilter {

	private static final String DESCRIPTION = Messages.peakFilter;

	@Override
	public IProcessingInfo<?> validate(IPeakCSD peak, IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validatePeak(peak));
		processingInfo.addMessages(validateFilterSettings(peakFilterSettings));
		return processingInfo;
	}

	@Override
	public IProcessingInfo<?> validate(List<IPeakCSD> peaks, IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validatePeaks(peaks));
		processingInfo.addMessages(validateFilterSettings(peakFilterSettings));
		return processingInfo;
	}

	@Override
	public IProcessingInfo<?> validate(IChromatogramSelectionCSD chromatogramSelection, IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validateChromatogramSelection(chromatogramSelection));
		processingInfo.addMessages(validateFilterSettings(peakFilterSettings));
		return processingInfo;
	}

	/**
	 * Validates the peak.
	 * 
	 * @param peak
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<?> validatePeak(IPeakCSD peak) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		if(peak == null) {
			processingInfo.addErrorMessage(DESCRIPTION, Messages.invalidPeak);
		}
		return processingInfo;
	}

	/**
	 * Validates the peak.
	 * 
	 * @param peak
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<?> validatePeaks(List<IPeakCSD> peaks) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		if(peaks == null) {
			processingInfo.addErrorMessage(DESCRIPTION, Messages.invalidPeakList);
		}
		return processingInfo;
	}

	/**
	 * Validates that the filter settings are not null.
	 * 
	 * @param peakFilterSettings
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<?> validateFilterSettings(IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		if(peakFilterSettings == null) {
			processingInfo.addErrorMessage(DESCRIPTION, Messages.invalidFilterSettings);
		}
		return processingInfo;
	}

	/**
	 * Validates the chromatogram selection and the referenced chromatogram.
	 * 
	 * @param chromatogramSelection
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<?> validateChromatogramSelection(IChromatogramSelectionCSD chromatogramSelection) {

		IProcessingInfo<?> processingInfo = new ProcessingInfo<>();
		if(chromatogramSelection == null) {
			processingInfo.addErrorMessage(DESCRIPTION, Messages.invalidChromatogramSelection);
		} else {
			if(chromatogramSelection.getChromatogram() == null) {
				processingInfo.addErrorMessage(DESCRIPTION, Messages.invalidChromatogram);
			}
		}
		return processingInfo;
	}
}
