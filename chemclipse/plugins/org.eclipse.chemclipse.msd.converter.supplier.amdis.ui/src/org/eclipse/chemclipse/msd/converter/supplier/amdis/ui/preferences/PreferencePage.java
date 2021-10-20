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
package org.eclipse.chemclipse.msd.converter.supplier.amdis.ui.preferences;

import org.eclipse.chemclipse.msd.converter.supplier.amdis.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.ui.Activator;
import org.eclipse.chemclipse.support.text.CharsetNIO;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("AMDIS Converter");
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		addField(new BooleanFieldEditor(PreferenceSupplier.P_SPLIT_LIBRARY, "Split library to several output files (<= 65535 mass spectra)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_EXCLUDE_UNCERTAIN_IONS, "Exclude uncertain ions from ELU file conversion", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_UNIT_MASS_RESOLUTION, "Use unit mass resolution", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_REMOVE_INTENSITIES_LOWER_THAN_ONE, "Remove intesities < 1.0", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_NORMALIZE_INTENSITIES, "Normalize intensities", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_EXPORT_INTENSITIES_AS_INTEGER, "Export intensities as Integer", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_PARSE_COMPOUND_INFORMATION, "Parse Compound Information (*.CID)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_PARSE_MOL_INFORMATION, "Parse MOL Information (*.MOL)", getFieldEditorParent()));
		//
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHARSET_IMPORT_MSL, "Charset Import MSL", CharsetNIO.getOptions(), getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHARSET_IMPORT_MSP, "Charset Import MSP", CharsetNIO.getOptions(), getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHARSET_IMPORT_FIN, "Charset Import FIN", CharsetNIO.getOptions(), getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHARSET_IMPORT_ELU, "Charset Import ELU", CharsetNIO.getOptions(), getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}
}
