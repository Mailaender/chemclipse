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
package org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.amdis.ui.preferences;

import org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.amdis.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.amdis.settings.IOnsiteSettings;
import org.eclipse.chemclipse.chromatogram.msd.peak.detector.supplier.amdis.ui.Activator;
import org.eclipse.chemclipse.support.settings.OperatingSystemUtils;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.FloatFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private BooleanFieldEditor autodetectLowMZ;
	private IntegerFieldEditor startMZ;
	private BooleanFieldEditor autodetectHighMZ;
	private IntegerFieldEditor stopMZ;
	private BooleanFieldEditor omitMZ;
	private StringFieldEditor omitedMZ;
	private BooleanFieldEditor useSolventTailing;
	private IntegerFieldEditor solventTailingMZ;
	private BooleanFieldEditor useColumnBleed;
	private IntegerFieldEditor columnBleedMZ;

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("AMDIS Peak Detector");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		/*
		 * If the operating system is Windows, the File can be used
		 * directly.<br/> In case of Linux/Unix, Emulators like Wine may be used
		 * that need some other arguments.
		 */
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("AMDIS application (AMDIS32$.EXE).", getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceSupplier.P_AMDIS_APPLICATION, "AMDIS32$.EXE", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceSupplier.P_AMDIS_TMP_PATH, "Tmp Path", getFieldEditorParent()));
		/*
		 * MAC OS X - try to define the Wine binary
		 */
		if(OperatingSystemUtils.isMac()) {
			addField(new StringFieldEditor(PreferenceSupplier.P_MAC_WINE_BINARY, "Wine binary (/Applications/Wine.app)", getFieldEditorParent()));
		}
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("AMDIS settings.", getFieldEditorParent()));
		addField(autodetectLowMZ = new BooleanFieldEditor(PreferenceSupplier.P_LOW_MZ_AUTO, "Autodetect low m/z:", getFieldEditorParent()));
		addField(startMZ = new IntegerFieldEditor(PreferenceSupplier.P_START_MZ, "m/z:", getFieldEditorParent()));
		addField(autodetectHighMZ = new BooleanFieldEditor(PreferenceSupplier.P_HIGH_MZ_AUTO, "Autodetect high m/z:", getFieldEditorParent()));
		addField(stopMZ = new IntegerFieldEditor(PreferenceSupplier.P_STOP_MZ, "m/z:", getFieldEditorParent()));
		addField(omitMZ = new BooleanFieldEditor(PreferenceSupplier.P_OMIT_MZ, "Omit m/z:", getFieldEditorParent()));
		addField(omitedMZ = new StringFieldEditor(PreferenceSupplier.P_OMITED_MZ, "Up to 8 m/z values separated by a space, 0 to omit TIC:", getFieldEditorParent()));
		addField(useSolventTailing = new BooleanFieldEditor(PreferenceSupplier.P_USE_SOLVENT_TAILING, "Use solvent tailing:", getFieldEditorParent()));
		addField(solventTailingMZ = new IntegerFieldEditor(PreferenceSupplier.P_SOLVENT_TAILING_MZ, "Solvent tailing m/z:", getFieldEditorParent()));
		addField(useColumnBleed = new BooleanFieldEditor(PreferenceSupplier.P_USE_COLUMN_BLEED, "Use column bleed:", getFieldEditorParent()));
		addField(columnBleedMZ = new IntegerFieldEditor(PreferenceSupplier.P_COLUMN_BLEED_MZ, "Column bleed m/z:", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		IntegerFieldEditor peakWidthFieldEditor = new IntegerFieldEditor(PreferenceSupplier.P_PEAK_WIDTH, "Peak width:", getFieldEditorParent());
		peakWidthFieldEditor.setValidRange(IOnsiteSettings.VALUE_MIN_PEAK_WIDTH, IOnsiteSettings.VALUE_MAX_PEAK_WIDTH);
		addField(peakWidthFieldEditor);
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_THRESHOLD, "Threshold:", IOnsiteSettings.THRESHOLD_VALUES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_ADJACENT_PEAK_SUBTRACTION, "Adjacent Peak Subtraction:", IOnsiteSettings.ADJACENT_PEAK_SUBTRACTION_VALUES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_RESOLUTION, "Resolution:", IOnsiteSettings.RESOLUTION_VALUES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SENSITIVITY, "Sensitivity:", IOnsiteSettings.SENSITIVITY_VALUES, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_SHAPE_REQUIREMENTS, "Shape Requirements:", IOnsiteSettings.SHAPE_REQUIREMENTS_VALUES, getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Extra settings (to improve the result quality).", getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MIN_SN_RATIO, "Minimum S/N ratio (0 = add all peaks):", 0.0f, Float.MAX_VALUE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MIN_LEADING, "Min Leading:", 0.0f, Float.MAX_VALUE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MAX_LEADING, "Max Leading:", 0.0f, Float.MAX_VALUE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MIN_TAILING, "Min Tailing:", 0.0f, Float.MAX_VALUE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceSupplier.P_MAX_TAILING, "Max Tailing:", 0.0f, Float.MAX_VALUE, getFieldEditorParent()));
		//
		for(Control control : getFieldEditorParent().getChildren()) {
			control.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseUp(MouseEvent e) {

					enableDisableFieldEditors(false);
				}
			});
		}
		//
		enableDisableFieldEditors(true);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}

	private void enableDisableFieldEditors(boolean loadInitially) {

		boolean isAutodetectLowMZ;
		boolean isAutodetectHighMZ;
		boolean isOmitMZ;
		boolean isUseSolventTailing;
		boolean isUseColumnBleed;
		//
		if(loadInitially) {
			IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
			isAutodetectLowMZ = preferenceStore.getBoolean(PreferenceSupplier.P_LOW_MZ_AUTO);
			isAutodetectHighMZ = preferenceStore.getBoolean(PreferenceSupplier.P_HIGH_MZ_AUTO);
			isOmitMZ = preferenceStore.getBoolean(PreferenceSupplier.P_OMIT_MZ);
			isUseSolventTailing = preferenceStore.getBoolean(PreferenceSupplier.P_USE_SOLVENT_TAILING);
			isUseColumnBleed = preferenceStore.getBoolean(PreferenceSupplier.P_USE_COLUMN_BLEED);
		} else {
			isAutodetectLowMZ = autodetectLowMZ.getBooleanValue();
			isAutodetectHighMZ = autodetectHighMZ.getBooleanValue();
			isOmitMZ = omitMZ.getBooleanValue();
			isUseSolventTailing = useSolventTailing.getBooleanValue();
			isUseColumnBleed = useColumnBleed.getBooleanValue();
		}
		//
		startMZ.setEnabled(!isAutodetectLowMZ, getFieldEditorParent());
		stopMZ.setEnabled(!isAutodetectHighMZ, getFieldEditorParent());
		omitedMZ.setEnabled(isOmitMZ, getFieldEditorParent());
		solventTailingMZ.setEnabled(isUseSolventTailing, getFieldEditorParent());
		columnBleedMZ.setEnabled(isUseColumnBleed, getFieldEditorParent());
	}
}
