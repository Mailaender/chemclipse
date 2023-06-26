/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
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

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.ExtendedIntegerFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.FloatFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.TargetIdentifierUI;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageTargets extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageTargets() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Targets");
		setDescription("");
	}

	@Override
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceConstants.P_USE_TARGET_LIST, "Use Target List", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_PROPAGATE_TARGET_ON_UPDATE, "Propagate Target on Update", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_TARGETS_TABLE_SORTABLE, "Table Sortable", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_TARGETS_TABLE_SHOW_DEVIATION_RT, "Show Deviation Retention Time", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_TARGETS_TABLE_SHOW_DEVIATION_RI, "Show Deviation Retention Index", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(PreferenceConstants.P_TARGET_TEMPLATE_LIBRARY_IMPORT_FOLDER, "Library Path", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_TARGET_IDENTIFER, "Target Identifier:", TargetIdentifierUI.getTargetIdentifier(), getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_USE_ABSOLUTE_DEVIATION_RETENTION_TIME, "Retention Time: Use absolute deviation", getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceConstants.P_RETENTION_TIME_DEVIATION_REL_OK, "Allowed Deviation [%]", PreferenceConstants.MIN_DEVIATION_RELATIVE, PreferenceConstants.MAX_DEVIATION_RELATIVE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceConstants.P_RETENTION_TIME_DEVIATION_REL_WARN, "Warn Deviation [%]", PreferenceConstants.MIN_DEVIATION_RELATIVE, PreferenceConstants.MAX_DEVIATION_RELATIVE, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceConstants.P_RETENTION_TIME_DEVIATION_ABS_OK, "Allowed Deviation [ms]", PreferenceConstants.MIN_DEVIATION_RETENTION_TIME, PreferenceConstants.MAX_DEVIATION_RETENTION_TIME, getFieldEditorParent()));
		addField(new ExtendedIntegerFieldEditor(PreferenceConstants.P_RETENTION_TIME_DEVIATION_ABS_WARN, "Warn Deviation [ms]", PreferenceConstants.MIN_DEVIATION_RETENTION_TIME, PreferenceConstants.MAX_DEVIATION_RETENTION_TIME, getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.P_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX, "Retention Index: Use absolute deviation", getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceConstants.P_RETENTION_INDEX_DEVIATION_REL_OK, "Allowed Deviation [%]", PreferenceConstants.MIN_DEVIATION_RELATIVE, PreferenceConstants.MAX_DEVIATION_RELATIVE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceConstants.P_RETENTION_INDEX_DEVIATION_REL_WARN, "Warn Deviation [%]", PreferenceConstants.MIN_DEVIATION_RELATIVE, PreferenceConstants.MAX_DEVIATION_RELATIVE, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceConstants.P_RETENTION_INDEX_DEVIATION_ABS_OK, "Allowed Deviation [abs]", PreferenceConstants.MIN_DEVIATION_RETENTION_INDEX, PreferenceConstants.MAX_DEVIATION_RETENTION_INDEX, getFieldEditorParent()));
		addField(new FloatFieldEditor(PreferenceConstants.P_RETENTION_INDEX_DEVIATION_ABS_WARN, "Warn Deviation [abs]", PreferenceConstants.MIN_DEVIATION_RETENTION_INDEX, PreferenceConstants.MAX_DEVIATION_RETENTION_INDEX, getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
