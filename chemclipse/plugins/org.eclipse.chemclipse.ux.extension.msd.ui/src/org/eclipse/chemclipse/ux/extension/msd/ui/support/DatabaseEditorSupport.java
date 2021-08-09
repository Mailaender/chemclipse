/*******************************************************************************
 * Copyright (c) 2017, 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.support;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.database.DatabaseConverter;
import org.eclipse.chemclipse.processing.converter.ISupplier;
import org.eclipse.chemclipse.ux.extension.msd.ui.editors.DatabaseEditor;
import org.eclipse.chemclipse.ux.extension.ui.provider.AbstractSupplierFileEditorSupport;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierFileEditorSupport;

public class DatabaseEditorSupport extends AbstractSupplierFileEditorSupport implements ISupplierFileEditorSupport {

	public DatabaseEditorSupport() {

		super(DatabaseConverter.getDatabaseConverterSupport().getSupplier());
	}

	@Override
	public String getType() {

		return TYPE_DATABASE_MSD;
	}

	@Override
	public boolean openEditor(File file) {

		return openEditor(file, false);
	}

	@Override
	public boolean openEditor(final File file, boolean batch) {

		/*
		 * Check that the selected file or directory is a valid database.
		 */
		if(isSupplierFile(file)) {
			openEditor(file, null, DatabaseEditor.ID, DatabaseEditor.CONTRIBUTION_URI, DatabaseEditor.ICON_URI, DatabaseEditor.TOOLTIP, batch);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void openOverview(final File file) {

		/*
		 * Check that the selected file or directory is a valid database.
		 */
		if(isSupplierFile(file)) {
			/*
			 * TODO - Probably show an abstract overview like the chromatogram overview part.
			 */
		}
	}

	@Override
	public boolean openEditor(File file, ISupplier supplier) {

		return openEditor(file);
	}
}
