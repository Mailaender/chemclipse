/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.ui.preferences;

import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpacerFieldEditor;
import org.eclipse.chemclipse.support.ui.preferences.fieldeditors.SpinnerFieldEditor;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.ui.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePageOCB extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePageOCB() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Measurement Data (*.ocb)");
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_VERSION_SAVE, "Save (*.ocb) as version:", PreferenceSupplier.getChromatogramVersions(), getFieldEditorParent()));
		addField(new SpinnerFieldEditor(PreferenceSupplier.P_CHROMATOGRAM_COMPRESSION_LEVEL, "Compression 0 = off, 9 = best", PreferenceSupplier.MIN_COMPRESSION_LEVEL, PreferenceSupplier.MAX_COMPRESSION_LEVEL, getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_FORCE_LOAD_ALTERNATE_DETECTOR, "Force load as alternate detector type", getFieldEditorParent()));
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_SCAN_PROXIES, "Use scan proxies (experimental).", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_LOAD_SCAN_PROXIES_IN_BACKGROUND, "Load scan proxies in background.", getFieldEditorParent()));
		addField(new IntegerFieldEditor(PreferenceSupplier.P_MIN_BYTES_TO_LOAD_IN_BACKGROUND, "Min size (bytes) to load proxies in background.", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}
}
