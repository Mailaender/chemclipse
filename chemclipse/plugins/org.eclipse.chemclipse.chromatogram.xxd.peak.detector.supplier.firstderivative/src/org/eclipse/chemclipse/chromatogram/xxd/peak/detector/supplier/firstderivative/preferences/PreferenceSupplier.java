/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.Threshold;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.Activator;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.settings.PeakDetectorSettingsCSD;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.settings.PeakDetectorSettingsMSD;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.firstderivative.settings.PeakDetectorSettingsWSD;
import org.eclipse.chemclipse.numeric.statistics.WindowSize;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferenceSupplier implements IPreferenceSupplier {

	public static final float MIN_SN_RATIO_MIN = 0.0f; // 0 = all peaks will be added
	public static final float MIN_SN_RATIO_MAX = Float.MAX_VALUE; // 0 = all peaks will be added
	//
	public static final String P_THRESHOLD_MSD = "thresholdMSD";
	public static final String DEF_THRESHOLD_MSD = Threshold.MEDIUM.name();
	public static final String P_INCLUDE_BACKGROUND_MSD = "includeBackgroundMSD";
	public static final boolean DEF_INCLUDE_BACKGROUND_MSD = false; // false will use BV oder VB, if true VV will be used.
	public static final String P_MIN_SN_RATIO_MSD = "minSNRatioMSD";
	public static final float DEF_MIN_SN_RATIO_MSD = 0.0f; // 0 = all peaks will be added
	public static final String P_MOVING_AVERAGE_WINDOW_SIZE_MSD = "movingAverageWindowSizeMSD";
	public static final String DEF_MOVING_AVERAGE_WINDOW_SIZE_MSD = WindowSize.WIDTH_3.name();
	//
	public static final String P_THRESHOLD_CSD = "thresholdCSD";
	public static final String DEF_THRESHOLD_CSD = Threshold.MEDIUM.name();
	public static final String P_INCLUDE_BACKGROUND_CSD = "includeBackgroundCSD";
	public static final boolean DEF_INCLUDE_BACKGROUND_CSD = false; // false will use BV oder VB, if true VV will be used.
	public static final String P_MIN_SN_RATIO_CSD = "minSNRatioCSD";
	public static final float DEF_MIN_SN_RATIO_CSD = 0.0f; // 0 = all peaks will be added
	public static final String P_MOVING_AVERAGE_WINDOW_SIZE_CSD = "movingAverageWindowSizeCSD";
	public static final String DEF_MOVING_AVERAGE_WINDOW_SIZE_CSD = WindowSize.WIDTH_3.name();
	//
	public static final String P_THRESHOLD_WSD = "thresholdWSD";
	public static final String DEF_THRESHOLD_WSD = Threshold.MEDIUM.name();
	public static final String P_INCLUDE_BACKGROUND_WSD = "includeBackgroundWSD";
	public static final boolean DEF_INCLUDE_BACKGROUND_WSD = false; // false will use BV oder VB, if true VV will be used.
	public static final String P_MIN_SN_RATIO_WSD = "minSNRatioWSD";
	public static final float DEF_MIN_SN_RATIO_WSD = 0.0f; // 0 = all peaks will be added
	public static final String P_MOVING_AVERAGE_WINDOW_SIZE_WSD = "movingAverageWindowSizeWSD";
	public static final String DEF_MOVING_AVERAGE_WINDOW_SIZE_WSD = WindowSize.WIDTH_3.name();
	//
	private static IPreferenceSupplier preferenceSupplier;

	public static IPreferenceSupplier INSTANCE() {

		if(preferenceSupplier == null) {
			preferenceSupplier = new PreferenceSupplier();
		}
		return preferenceSupplier;
	}

	@Override
	public IScopeContext getScopeContext() {

		return InstanceScope.INSTANCE;
	}

	@Override
	public String getPreferenceNode() {

		return Activator.getContext().getBundle().getSymbolicName();
	}

	@Override
	public Map<String, String> getDefaultValues() {

		Map<String, String> defaultValues = new HashMap<String, String>();
		//
		defaultValues.put(P_INCLUDE_BACKGROUND_MSD, Boolean.toString(DEF_INCLUDE_BACKGROUND_MSD));
		defaultValues.put(P_MIN_SN_RATIO_MSD, Float.toString(DEF_MIN_SN_RATIO_MSD));
		defaultValues.put(P_MOVING_AVERAGE_WINDOW_SIZE_MSD, DEF_MOVING_AVERAGE_WINDOW_SIZE_MSD);
		defaultValues.put(P_THRESHOLD_MSD, DEF_THRESHOLD_MSD);
		//
		defaultValues.put(P_INCLUDE_BACKGROUND_CSD, Boolean.toString(DEF_INCLUDE_BACKGROUND_CSD));
		defaultValues.put(P_MIN_SN_RATIO_CSD, Float.toString(DEF_MIN_SN_RATIO_CSD));
		defaultValues.put(P_MOVING_AVERAGE_WINDOW_SIZE_CSD, DEF_MOVING_AVERAGE_WINDOW_SIZE_CSD);
		defaultValues.put(P_THRESHOLD_CSD, DEF_THRESHOLD_CSD);
		//
		defaultValues.put(P_INCLUDE_BACKGROUND_WSD, Boolean.toString(DEF_INCLUDE_BACKGROUND_WSD));
		defaultValues.put(P_MIN_SN_RATIO_WSD, Float.toString(DEF_MIN_SN_RATIO_WSD));
		defaultValues.put(P_MOVING_AVERAGE_WINDOW_SIZE_WSD, DEF_MOVING_AVERAGE_WINDOW_SIZE_WSD);
		defaultValues.put(P_THRESHOLD_WSD, DEF_THRESHOLD_WSD);
		//
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static PeakDetectorSettingsMSD getPeakDetectorSettingsMSD() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		PeakDetectorSettingsMSD peakDetectorSettings = new PeakDetectorSettingsMSD();
		peakDetectorSettings.setThreshold(Threshold.valueOf(preferences.get(P_THRESHOLD_MSD, DEF_THRESHOLD_MSD)));
		peakDetectorSettings.setIncludeBackground(preferences.getBoolean(P_INCLUDE_BACKGROUND_MSD, DEF_INCLUDE_BACKGROUND_MSD));
		peakDetectorSettings.setMinimumSignalToNoiseRatio(preferences.getFloat(P_MIN_SN_RATIO_MSD, DEF_MIN_SN_RATIO_MSD));
		peakDetectorSettings.setMovingAverageWindowSize(WindowSize.valueOf(WindowSize.getAdjustedSetting(preferences.get(P_MOVING_AVERAGE_WINDOW_SIZE_MSD, DEF_MOVING_AVERAGE_WINDOW_SIZE_MSD))));
		return peakDetectorSettings;
	}

	public static PeakDetectorSettingsCSD getPeakDetectorSettingsCSD() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		PeakDetectorSettingsCSD peakDetectorSettings = new PeakDetectorSettingsCSD();
		peakDetectorSettings.setThreshold(Threshold.valueOf(preferences.get(P_THRESHOLD_CSD, DEF_THRESHOLD_CSD)));
		peakDetectorSettings.setIncludeBackground(preferences.getBoolean(P_INCLUDE_BACKGROUND_CSD, DEF_INCLUDE_BACKGROUND_CSD));
		peakDetectorSettings.setMinimumSignalToNoiseRatio(preferences.getFloat(P_MIN_SN_RATIO_CSD, DEF_MIN_SN_RATIO_CSD));
		peakDetectorSettings.setMovingAverageWindowSize(WindowSize.valueOf(WindowSize.getAdjustedSetting(preferences.get(P_MOVING_AVERAGE_WINDOW_SIZE_CSD, DEF_MOVING_AVERAGE_WINDOW_SIZE_CSD))));
		return peakDetectorSettings;
	}

	public static PeakDetectorSettingsWSD getPeakDetectorSettingsWSD() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		PeakDetectorSettingsWSD peakDetectorSettings = new PeakDetectorSettingsWSD();
		peakDetectorSettings.setThreshold(Threshold.valueOf(preferences.get(P_THRESHOLD_WSD, DEF_THRESHOLD_WSD)));
		peakDetectorSettings.setIncludeBackground(preferences.getBoolean(P_INCLUDE_BACKGROUND_WSD, DEF_INCLUDE_BACKGROUND_WSD));
		peakDetectorSettings.setMinimumSignalToNoiseRatio(preferences.getFloat(P_MIN_SN_RATIO_WSD, DEF_MIN_SN_RATIO_WSD));
		peakDetectorSettings.setMovingAverageWindowSize(WindowSize.valueOf(WindowSize.getAdjustedSetting(preferences.get(P_MOVING_AVERAGE_WINDOW_SIZE_WSD, DEF_MOVING_AVERAGE_WINDOW_SIZE_WSD))));
		return peakDetectorSettings;
	}
}
