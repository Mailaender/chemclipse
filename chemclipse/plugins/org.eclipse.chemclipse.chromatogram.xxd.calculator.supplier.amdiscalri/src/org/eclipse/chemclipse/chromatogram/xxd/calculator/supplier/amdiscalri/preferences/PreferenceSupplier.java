/*******************************************************************************
 * Copyright (c) 2014, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.preferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.Activator;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.settings.CalculatorSettings;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.support.preferences.IPreferenceSupplier;
import org.eclipse.chemclipse.support.util.FileListUtil;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class PreferenceSupplier implements IPreferenceSupplier {

	private static final Logger logger = Logger.getLogger(PreferenceSupplier.class);
	/*
	 * RI Calculation
	 */
	public static final String DETECTION_STRATEGY_AUTO = "Automatic";
	public static final String DETECTION_STRATEGY_CHROMATOGRAM = "Chromatogram";
	public static final String DETECTION_STRATEGY_FILES = "Files";
	//
	public static final String P_RETENTION_INDEX_FILES = "retentionIndexFiles";
	public static final String DEF_RETENTION_INDEX_FILES = "";
	public static final String P_DETECTION_STRATEGY = "detectionStrategy";
	public static final String DEF_DETECTION_STRATEGY = DETECTION_STRATEGY_FILES;
	public static final String[][] DETECTION_OPTIONS = new String[][]{{"First Chromatogram - Then File(s)", DETECTION_STRATEGY_AUTO}, {"Chromatogram Only", DETECTION_STRATEGY_CHROMATOGRAM}, {"File(s) Only", DETECTION_STRATEGY_FILES}};
	public static final String P_USE_DEFAULT_COLUMN = "useDefaultColumn";
	public static final boolean DEF_USE_DEFAULT_COLUMN = true;
	public static final String P_PROCESS_REFERENCED_CHROMATOGRAMS = "processReferencedChromatograms";
	public static final boolean DEF_PROCESS_REFERENCED_CHROMATOGRAMS = true;
	public static final String P_FILTER_PATH_INDEX_FILES = "filterPathIndexFiles";
	public static final String DEF_FILTER_PATH_INDEX_FILES = "";
	public static final String P_FILTER_PATH_MODELS_MSD = "filterPathModelsMSD";
	public static final String DEF_FILTER_PATH_MODELS_MSD = "";
	public static final String P_FILTER_PATH_MODELS_CSD = "filterPathModelsCSD";
	public static final String DEF_FILTER_PATH_MODELS_CSD = "";
	/*
	 * Alkane Identifier
	 */
	public static final String P_NUMBER_OF_TARGETS = "numberOfTargets";
	public static final int DEF_NUMBER_OF_TARGETS = 15;
	public static final int MIN_NUMBER_OF_TARGETS = 1;
	public static final int MAX_NUMBER_OF_TARGETS = 100;
	public static final String P_MIN_MATCH_FACTOR = "minMatchFactor";
	public static final float DEF_MIN_MATCH_FACTOR = 70.0f;
	public static final float MIN_MIN_MATCH_FACTOR = 0.0f;
	public static final float MAX_MIN_MATCH_FACTOR = 100.0f;
	public static final String P_MIN_REVERSE_MATCH_FACTOR = "minReverseMatchFactor";
	public static final float DEF_MIN_REVERSE_MATCH_FACTOR = 70.0f;
	public static final float MIN_MIN_REVERSE_MATCH_FACTOR = 0.0f;
	public static final float MAX_MIN_REVERSE_MATCH_FACTOR = 100.0f;
	public static final String P_ADD_UNKNOWN_MZ_LIST = "addUnknownMzList";
	public static final boolean DEF_ADD_UNKNOWN_MZ_LIST = true;
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
		defaultValues.put(P_RETENTION_INDEX_FILES, DEF_RETENTION_INDEX_FILES);
		defaultValues.put(P_DETECTION_STRATEGY, DEF_DETECTION_STRATEGY);
		defaultValues.put(P_USE_DEFAULT_COLUMN, Boolean.toString(DEF_USE_DEFAULT_COLUMN));
		defaultValues.put(P_PROCESS_REFERENCED_CHROMATOGRAMS, Boolean.toString(DEF_PROCESS_REFERENCED_CHROMATOGRAMS));
		defaultValues.put(P_FILTER_PATH_INDEX_FILES, DEF_FILTER_PATH_INDEX_FILES);
		defaultValues.put(P_FILTER_PATH_MODELS_MSD, DEF_FILTER_PATH_MODELS_MSD);
		defaultValues.put(P_FILTER_PATH_MODELS_CSD, DEF_FILTER_PATH_MODELS_CSD);
		defaultValues.put(P_NUMBER_OF_TARGETS, Integer.toString(DEF_NUMBER_OF_TARGETS));
		defaultValues.put(P_MIN_MATCH_FACTOR, Float.toString(DEF_MIN_MATCH_FACTOR));
		defaultValues.put(P_MIN_REVERSE_MATCH_FACTOR, Float.toString(DEF_MIN_REVERSE_MATCH_FACTOR));
		defaultValues.put(P_ADD_UNKNOWN_MZ_LIST, Boolean.toString(DEF_ADD_UNKNOWN_MZ_LIST));
		return defaultValues;
	}

	@Override
	public IEclipsePreferences getPreferences() {

		return getScopeContext().getNode(getPreferenceNode());
	}

	public static CalculatorSettings getChromatogramCalculatorSettings() {

		CalculatorSettings calculatorSettings = new CalculatorSettings();
		calculatorSettings.setRetentionIndexFiles(getRetentionIndexFiles());
		return calculatorSettings;
	}

	public static String getDetectionStrategy() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.get(P_DETECTION_STRATEGY, DEF_DETECTION_STRATEGY);
	}

	public static boolean isUseDefaultColumn() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.getBoolean(P_USE_DEFAULT_COLUMN, DEF_USE_DEFAULT_COLUMN);
	}

	public static boolean isProcessReferencedChromatograms() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.getBoolean(P_PROCESS_REFERENCED_CHROMATOGRAMS, DEF_PROCESS_REFERENCED_CHROMATOGRAMS);
	}

	public static List<String> getRetentionIndexFiles() {

		FileListUtil fileListUtil = new FileListUtil();
		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return fileListUtil.getFiles(preferences.get(P_RETENTION_INDEX_FILES, DEF_RETENTION_INDEX_FILES));
	}

	public static void setRetentionIndexFiles(List<String> retentionIndexFiles) {

		try {
			FileListUtil fileListUtil = new FileListUtil();
			IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
			String items[] = retentionIndexFiles.toArray(new String[retentionIndexFiles.size()]);
			preferences.put(P_RETENTION_INDEX_FILES, fileListUtil.createList(items));
			preferences.flush();
		} catch(BackingStoreException e) {
			logger.warn(e);
		}
	}

	public static String getFilterPathIndexFiles() {

		return getFilterPath(P_FILTER_PATH_INDEX_FILES, DEF_FILTER_PATH_INDEX_FILES);
	}

	public static void setFilterPathIndexFiles(String filterPath) {

		setFilterPath(P_FILTER_PATH_INDEX_FILES, filterPath);
	}

	public static String getFilterPathModelsMSD() {

		return getFilterPath(P_FILTER_PATH_MODELS_MSD, DEF_FILTER_PATH_MODELS_MSD);
	}

	public static void setFilterPathModelsMSD(String filterPath) {

		setFilterPath(P_FILTER_PATH_MODELS_MSD, filterPath);
	}

	public static String getFilterPathModelsCSD() {

		return getFilterPath(P_FILTER_PATH_MODELS_CSD, DEF_FILTER_PATH_MODELS_CSD);
	}

	public static void setFilterPathModelsCSD(String filterPath) {

		setFilterPath(P_FILTER_PATH_MODELS_CSD, filterPath);
	}

	public static int getNumberOfTargets() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.getInt(P_NUMBER_OF_TARGETS, DEF_NUMBER_OF_TARGETS);
	}

	public static float getMinMatchFactor() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.getFloat(P_MIN_MATCH_FACTOR, DEF_MIN_MATCH_FACTOR);
	}

	public static float getMinReverseMatchFactor() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.getFloat(P_MIN_REVERSE_MATCH_FACTOR, DEF_MIN_REVERSE_MATCH_FACTOR);
	}

	public static boolean isAddUnknownMzList() {

		IEclipsePreferences preferences = PreferenceSupplier.INSTANCE().getPreferences();
		return preferences.getBoolean(P_ADD_UNKNOWN_MZ_LIST, DEF_ADD_UNKNOWN_MZ_LIST);
	}

	private static String getFilterPath(String key, String def) {

		IEclipsePreferences eclipsePreferences = INSTANCE().getPreferences();
		return eclipsePreferences.get(key, def);
	}

	private static void setFilterPath(String key, String filterPath) {

		try {
			IEclipsePreferences eclipsePreferences = INSTANCE().getPreferences();
			eclipsePreferences.put(key, filterPath);
			eclipsePreferences.flush();
		} catch(BackingStoreException e) {
			logger.warn(e);
		}
	}
}
