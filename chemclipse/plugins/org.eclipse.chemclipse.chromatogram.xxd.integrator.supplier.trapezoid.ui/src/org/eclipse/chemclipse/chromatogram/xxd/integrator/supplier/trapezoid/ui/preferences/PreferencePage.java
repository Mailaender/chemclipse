/*******************************************************************************
 * Copyright (c) 2008, 2022 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.ui.preferences;

import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.xxd.integrator.supplier.trapezoid.ui.Activator;
import org.eclipse.chemclipse.support.ui.preferences.editors.IonTableEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setTitle("Trapezoid Integrator");
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {

		addField(new IonTableEditor(PreferenceSupplier.P_SELECTED_IONS, "Selected ions (default empty list: 0 = TIC)", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_PEAK_AREA_INCLUDE_BACKGROUND, "Include peak background in area calculation.", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceSupplier.P_USE_AREA_CONSTRAINT, "Use Area Constraint (integrations < 1 are set to 0).", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {

	}
}