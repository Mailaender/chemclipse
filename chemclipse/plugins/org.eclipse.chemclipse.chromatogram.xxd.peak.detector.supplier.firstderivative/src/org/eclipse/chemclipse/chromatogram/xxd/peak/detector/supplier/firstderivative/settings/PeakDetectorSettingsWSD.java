/*******************************************************************************
 * Copyright (c) 2018, 2021 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - remove the window size enum
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.settings;

import org.eclipse.chemclipse.chromatogram.peak.detector.model.Threshold;
import org.eclipse.chemclipse.chromatogram.wsd.peak.detector.settings.AbstractPeakDetectorWSDSettings;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.support.settings.EnumSelectionRadioButtonsSettingProperty;
import org.eclipse.chemclipse.support.settings.FloatSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty;
import org.eclipse.chemclipse.support.settings.IntSettingsProperty.Validation;
import org.eclipse.chemclipse.support.settings.serialization.WindowSizeDeserializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PeakDetectorSettingsWSD extends AbstractPeakDetectorWSDSettings {

	@JsonProperty(value = "Threshold", defaultValue = "MEDIUM")
	@EnumSelectionRadioButtonsSettingProperty
	private Threshold threshold = Threshold.MEDIUM;
	@JsonProperty(value = "Include Background (VV: true, BV|VB: false)", defaultValue = "false")
	private boolean includeBackground = false;
	@JsonProperty(value = "Min S/N Ratio", defaultValue = "0")
	@FloatSettingsProperty(minValue = PreferenceSupplier.MIN_SN_RATIO_MIN, maxValue = PreferenceSupplier.MIN_SN_RATIO_MAX)
	private float minimumSignalToNoiseRatio;
	@JsonProperty(value = "Window Size", defaultValue = "5")
	@JsonPropertyDescription(value = "Window Size: 3, 5, 7, ..., 45")
	@IntSettingsProperty(minValue = PreferenceSupplier.MIN_WINDOW_SIZE, maxValue = PreferenceSupplier.MAX_WINDOW_SIZE, validation = Validation.ODD_NUMBER_INCLUDING_ZERO)
	@JsonDeserialize(using = WindowSizeDeserializer.class)
	private int windowSize;

	public PeakDetectorSettingsWSD() {

		windowSize = 5;
	}

	public Threshold getThreshold() {

		return threshold;
	}

	public void setThreshold(Threshold threshold) {

		if(threshold != null) {
			this.threshold = threshold;
		}
	}

	public boolean isIncludeBackground() {

		return includeBackground;
	}

	public void setIncludeBackground(boolean includeBackground) {

		this.includeBackground = includeBackground;
	}

	public float getMinimumSignalToNoiseRatio() {

		return minimumSignalToNoiseRatio;
	}

	public void setMinimumSignalToNoiseRatio(float minimumSignalToNoiseRatio) {

		this.minimumSignalToNoiseRatio = minimumSignalToNoiseRatio;
	}

	public int getMovingAverageWindowSize() {

		return windowSize;
	}

	public void setMovingAverageWindowSize(int windowSize) {

		this.windowSize = windowSize;
	}
}
