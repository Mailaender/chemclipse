/*******************************************************************************
 * Copyright (c) 2012, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.peakmax.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.peakmax.Activator;
import org.eclipse.chemclipse.chromatogram.msd.integrator.supplier.peakmax.settings.PeakIntegrationSettings;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IAreaSupport;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.core.settings.peaks.IIntegrationSupport;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.support.util.IonSettingUtil;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferenceSupplier implements IPreferenceSupplier {

	public static final String P_SELECTED_IONS = "selectedIons";
	public static final String DEF_SELECTED_IONS = "0"; // 103;104 | 0 = TIC
	public static final String P_MINIMUM_PEAK_WIDTH = "minimumPeakWidth";
	public static final int DEF_MINIMUM_PEAK_WIDTH = 0; // milliseconds
	public static final String P_MINIMUM_SIGNAL_TO_NOISE_RATIO = "minimumSignalToNoiseRatio";
	public static final int DEF_MINIMUM_SIGNAL_TO_NOISE_RATIO = 0; // 0 all peaks will be accepted
	public static final String P_MINIMUM_PEAK_AREA = "minimumPeakArea";
	public static final int DEF_MINIMUM_PEAK_AREA = 0; // 0 all peaks will be accepted
	//
	public static final int MIN_RETENTION_TIME = 0; // = 0.0 minutes
	public static final int MAX_RETENTION_TIME = 60000; // = 1.0 minutes;
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
		defaultValues.put(P_SELECTED_IONS, DEF_SELECTED_IONS);
		defaultValues.put(P_MINIMUM_PEAK_AREA, Integer.toString(DEF_MINIMUM_PEAK_AREA));
		defaultValues.put(P_MINIMUM_PEAK_WIDTH, Integer.toString(DEF_MINIMUM_PEAK_WIDTH));
		defaultValues.put(P_MINIMUM_SIGNAL_TO_NOISE_RATIO, Integer.toString(DEF_MINIMUM_SIGNAL_TO_NOISE_RATIO));
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static PeakIntegrationSettings getPeakIntegrationSettings() {

		PeakIntegrationSettings peakIntegrationSettings = new PeakIntegrationSettings();
		// baselineSupport.setBaselineHoldOn(5000, 100000);
		/*
		 * Area Support
		 */
		IAreaSupport areaSupport = peakIntegrationSettings.getAreaSupport();
		areaSupport.setMinimumArea(PreferenceSupplier.getMinimumPeakArea()); // int but double should be used.
		/*
		 * Integration Support
		 */
		IIntegrationSupport integrationSupport = peakIntegrationSettings.getIntegrationSupport();
		integrationSupport.setMinimumPeakWidth(PreferenceSupplier.getMinimumPeakWidth());
		integrationSupport.setMinimumSignalToNoiseRatio(PreferenceSupplier.getMinimumSignalToNoiseRatio()); // int but float should be used.
		IMarkedIons selectedIons = peakIntegrationSettings.getSelectedIons();
		String ions = PreferenceSupplier.getIons(P_SELECTED_IONS, DEF_SELECTED_IONS);
		IonSettingUtil settingIon = new IonSettingUtil();
		selectedIons.add(settingIon.extractIons(settingIon.deserialize(ions)));
		return peakIntegrationSettings;
	}

	public static int getMinimumPeakWidth() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getInt(P_MINIMUM_PEAK_WIDTH, DEF_MINIMUM_PEAK_WIDTH);
	}

	public static int getMinimumSignalToNoiseRatio() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getInt(P_MINIMUM_SIGNAL_TO_NOISE_RATIO, DEF_MINIMUM_SIGNAL_TO_NOISE_RATIO);
	}

	public static int getMinimumPeakArea() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getInt(P_MINIMUM_PEAK_AREA, DEF_MINIMUM_PEAK_AREA);
	}

	/**
	 * Returns a list of ions to preserve stored in the settings.
	 * 
	 * @return List<Integer>
	 */
	public static String getIons(String preference, String def) {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		/*
		 * E.g. "18;28;84;207" to 18 28 84 207
		 */
		return preferences.get(preference, def);
	}
}
