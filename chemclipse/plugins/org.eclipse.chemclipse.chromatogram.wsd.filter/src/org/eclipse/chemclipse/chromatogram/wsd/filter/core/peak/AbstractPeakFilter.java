/*******************************************************************************
 * Copyright (c) 2018, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.wsd.filter.core.peak;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.IPeakFilterResult;
import org.eclipse.chemclipse.chromatogram.filter.settings.IPeakFilterSettings;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.wsd.model.core.IPeakWSD;
import org.eclipse.chemclipse.wsd.model.core.selection.IChromatogramSelectionWSD;

public abstract class AbstractPeakFilter implements IPeakFilter {

	private static final String DESCRIPTION = "Peak Filter";

	@Override
	public IProcessingInfo<IPeakFilterResult> validate(IPeakWSD peak, IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validatePeak(peak));
		processingInfo.addMessages(validateFilterSettings(peakFilterSettings));
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IPeakFilterResult> validate(List<IPeakWSD> peaks, IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
		processingInfo.addMessages(validatePeaks(peaks));
		processingInfo.addMessages(validateFilterSettings(peakFilterSettings));
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IPeakFilterResult> validate(IChromatogramSelectionWSD chromatogramSelection, IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
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
	private IProcessingInfo<IPeakFilterResult> validatePeak(IPeakWSD peak) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
		if(peak == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The peak is not valid.");
		}
		return processingInfo;
	}

	/**
	 * Validates the peak.
	 * 
	 * @param peak
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<IPeakFilterResult> validatePeaks(List<IPeakWSD> peaks) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
		if(peaks == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The peak list is not valid.");
		}
		return processingInfo;
	}

	/**
	 * Validates that the filter settings are not null.
	 * 
	 * @param peakFilterSettings
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<IPeakFilterResult> validateFilterSettings(IPeakFilterSettings peakFilterSettings) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
		if(peakFilterSettings == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The filter settings are not valid.");
		}
		return processingInfo;
	}

	/**
	 * Validates the chromatogram selection and the referenced chromatogram.
	 * 
	 * @param chromatogramSelection
	 * @return {@link IProcessingInfo}
	 */
	private IProcessingInfo<IPeakFilterResult> validateChromatogramSelection(IChromatogramSelectionWSD chromatogramSelection) {

		IProcessingInfo<IPeakFilterResult> processingInfo = new ProcessingInfo<>();
		if(chromatogramSelection == null) {
			processingInfo.addErrorMessage(DESCRIPTION, "The chromatogram selection is not valid.");
		} else {
			if(chromatogramSelection.getChromatogram() == null) {
				processingInfo.addErrorMessage(DESCRIPTION, "The chromatogram is not valid.");
			}
		}
		return processingInfo;
	}
}
