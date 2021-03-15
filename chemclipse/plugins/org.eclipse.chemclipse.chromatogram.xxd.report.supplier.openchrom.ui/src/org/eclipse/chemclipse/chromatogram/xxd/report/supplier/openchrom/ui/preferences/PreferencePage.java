/*******************************************************************************
 * Copyright (c) 2012, 2021 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.report.supplier.openchrom.ui.preferences;

import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.openchrom.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.report.supplier.openchrom.ui.Activator;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.LabelFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Reports");
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_APPEND_FILES, "Append report files", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_BEST_MATCH, "Use Best Match", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new LabelFieldEditor("Area% Report", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceSupplier.P_DELTA_RETENTION_TIME_LEFT, "Delta Retention Time Left [ms]", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceSupplier.P_DELTA_RETENTION_TIME_LEFT, "Delta Retention Time Right [ms]", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_RETENTION_INDEX_QC, "Use Retention Index QC", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_ADD_PEAK_AREA, "Add Peak Area", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}
}
