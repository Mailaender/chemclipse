/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpinnerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swtchart.extensions.preferences.PreferenceSupport;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePagePeaks extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePagePeaks() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Peaks");
		setDescription("");
	}

	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_BACKGROUND, "Show Background", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK, "Show Peak", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_BASELINE, "Show Peak Baseline", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_TANGENTS, "Show Peak Tangents", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_WIDTH_0, "Show Peak Width 0% Height", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_WIDTH_50, "Show Peak Width 50% Height", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_WIDTH_CONDAL_BOSH, "Show Peak Width 15% and 85% Height (Condal-Bosh)", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_BACKGROUND, "Color Background", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_1, "Color Peak 1:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_2, "Color Peak 2:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_BASELINE, "Color Peak Baseline:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_TANGENTS, "Color Peak Tangents:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_WIDTH_0, "Color Peak Width 0% Height:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_WIDTH_50, "Color Peak Width 50% Height:", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_WIDTH_CONDAL_BOSH, "Color Peak Width 15% and 85% Height (Condal-Bosh):", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_COLOR_PEAK_DETECTOR_CHROMATOGRAM, "Color Peak Detector Chromatogram:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_SHOW_PEAK_DETECTOR_CHROMATOGRAM_AREA, "Show Peak Detector Chromatogram Area", getFieldEditorParent()));
		addField(new SpinnerFieldEditor(PreferenceSupplier.P_PEAK_DETECTOR_SCAN_MARKER_SIZE, "Peak Detector Scan Marker Size:", PreferenceSupplier.MIN_SYMBOL_SIZE, PreferenceSupplier.MAX_SYMBOL_SIZE, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceSupplier.P_PEAK_DETECTOR_SCAN_MARKER_COLOR, "Peak Detector Scan Marker Color:", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_PEAK_DETECTOR_SCAN_MARKER_TYPE, "Peak Detector Scan Marker Type:", PreferenceSupport.SYMBOL_TYPES, getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}
}
