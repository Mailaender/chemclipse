/*******************************************************************************
 * Copyright (c) 2014, 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.Activator;
import org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.settings.BaselineDetectorSettings;
import org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.settings.MassSpectrumFilterSettings;
import org.eclipse.chemclipse.chromatogram.xxd.edit.supplier.snip.settings.PeakFilterSettings;
import org.eclipse.chemclipse.support.model.WindowSize;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferenceSupplier implements IPreferenceSupplier {

	public static final String P_ITERATIONS = "iterations";
	public static final int DEF_ITERATIONS = 100;
	public static final int MIN_ITERATIONS = 5;
	public static final int MAX_ITERATIONS = 2000;
	public static final String P_WINDOW_SIZE = "windowSize";
	public static final int DEF_WINDOW_SIZE = 7;
	public static final int MIN_WINDOW_SIZE = 0;
	public static final int MAX_WINDOW_SIZE = 45;
	public static final String P_MAGNIFICATION_FACTOR = "magnificationFactor";
	public static final double DEF_MAGNIFICATION_FACTOR = 1;
	public static final double MIN_MAGNIFICATION_FACTOR = 0.01d;
	public static final double MAX_MAGNIFICATION_FACTOR = 5.0d;
	public static final String P_TRANSITIONS = "transitions";
	public static final int DEF_TRANSITIONS = 1;
	public static final int MIN_TRANSITIONS = 1;
	public static final int MAX_TRANSITIONS = 100;
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
		defaultValues.put(P_ITERATIONS, Integer.toString(DEF_ITERATIONS));
		defaultValues.put(P_MAGNIFICATION_FACTOR, Double.toString(DEF_MAGNIFICATION_FACTOR));
		defaultValues.put(P_TRANSITIONS, Integer.toString(DEF_TRANSITIONS));
		defaultValues.put(P_WINDOW_SIZE, Integer.toString(DEF_WINDOW_SIZE));
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static BaselineDetectorSettings getBaselineDetectorSettings() {

		BaselineDetectorSettings settings = new BaselineDetectorSettings();
		settings.setIterations(getIterations());
		settings.setWindowSize(getWindowSize());
		return settings;
	}

	public static PeakFilterSettings getPeakFilterSettings() {

		PeakFilterSettings settings = new PeakFilterSettings();
		settings.setIterations(getIterations());
		settings.setMagnificationFactor(getMagnificationFactor());
		settings.setTransitions(getTransitions());
		return settings;
	}

	public static MassSpectrumFilterSettings getMassSpectrumFilterSettings() {

		MassSpectrumFilterSettings settings = new MassSpectrumFilterSettings();
		settings.setIterations(getIterations());
		settings.setMagnificationFactor(getMagnificationFactor());
		settings.setTransitions(getTransitions());
		return settings;
	}

	public static int getIterations() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getInt(P_ITERATIONS, DEF_ITERATIONS);
	}

	public static int getWindowSize() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return WindowSize.getAdjustedSetting(preferences.get(P_WINDOW_SIZE, Integer.toString(DEF_WINDOW_SIZE)));
	}

	public static double getMagnificationFactor() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getDouble(P_MAGNIFICATION_FACTOR, DEF_MAGNIFICATION_FACTOR);
	}

	public static int getTransitions() {

		IEclipsePreferences preferences = INSTANCE().getPreferences();
		return preferences.getInt(P_TRANSITIONS, DEF_TRANSITIONS);
	}
}
