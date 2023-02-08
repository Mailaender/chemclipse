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
package org.eclipse.chemclipse.ux.extension.xxd.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.swt.ui.support.Colors;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swtchart.extensions.charts.ChartOptions;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageQuantitationAxes extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageQuantitationAxes() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Quantitation Axes");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceConstants.P_COLOR_SCHEME_DISPLAY_CALIBRATION, "Display Color Scheme", Colors.getAvailableColorSchemes(), getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Concentration", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_X_AXIS_CONCENTRATION_CALIBRATION, "Show X Axis (Concentration)", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_POSITION_X_AXIS_CONCENTRATION_CALIBRATION, "Position X Axis (Concentration):", ChartOptions.POSITIONS, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_COLOR_X_AXIS_CONCENTRATION_CALIBRATION, "Color X Axis (Concentration):", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_GRIDLINE_STYLE_X_AXIS_CONCENTRATION_CALIBRATION, "GridLine Style X Axis (Concentration):", ChartOptions.LINE_STYLES, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_GRIDLINE_COLOR_X_AXIS_CONCENTRATION_CALIBRATION, "GridLine Color X Axis (Concentration):", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Response", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_Y_AXIS_RESPONSE_CALIBRATION, "Show Y Axis (Response)", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_POSITION_Y_AXIS_RESPONSE_CALIBRATION, "Position Y Axis (Response):", ChartOptions.POSITIONS, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_COLOR_Y_AXIS_RESPONSE_CALIBRATION, "Color Y Axis (Response):", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_GRIDLINE_STYLE_Y_AXIS_RESPONSE_CALIBRATION, "GridLine Style Y Axis (Response):", ChartOptions.LINE_STYLES, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_GRIDLINE_COLOR_Y_AXIS_RESPONSE_CALIBRATION, "GridLine Color Y Axis (Response):", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Response%", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_SHOW_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, "Show Y Axis (Response %)", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_POSITION_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, "Position Y Axis (Response %):", ChartOptions.POSITIONS, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, "Color Y Axis (Response %):", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_GRIDLINE_STYLE_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, "GridLine Style Y Axis (Response %):", ChartOptions.LINE_STYLES, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_GRIDLINE_COLOR_Y_AXIS_RELATIVE_RESPONSE_CALIBRATION, "GridLine Color Y Axis (Response %):", getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
