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
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.Activator;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.settings.PeakDetectorSettingsCSD;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.settings.PeakDetectorSettingsMSD;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.settings.PeakDetectorSettingsWSD;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class PreferenceSupplier implements IPreferenceSupplier {

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
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static PeakDetectorSettingsCSD getPeakDetectorSettingsCSD() {

		return new PeakDetectorSettingsCSD();
	}

	public static PeakDetectorSettingsMSD getPeakDetectorSettingsMSD() {

		return new PeakDetectorSettingsMSD();
	}

	public static PeakDetectorSettingsWSD getPeakDetectorSettingsWSD() {

		return new PeakDetectorSettingsWSD();
	}
}
