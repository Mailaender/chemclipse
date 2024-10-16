/*******************************************************************************
 * Copyright (c) 2011, 2019 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - implementation
 * Christoph Läubrich - support new lazy table model
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.wizards;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.chemclipse.ux.extension.ui.swt.DataExplorerTreeUI;
import org.eclipse.chemclipse.ux.extension.ui.swt.DataExplorerTreeUI.DataExplorerTreeRoot;
import org.eclipse.chemclipse.ux.extension.ui.swt.MultiDataExplorerTreeUI;
import org.eclipse.chemclipse.xxd.process.files.ISupplierFileIdentifier;
import org.eclipse.chemclipse.xxd.process.files.SupplierFileIdentifierCache;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class InputEntriesWizardPage extends WizardPage {

	private final class WizardMultiDataExplorerTreeUI extends MultiDataExplorerTreeUI {

		private WizardMultiDataExplorerTreeUI(Composite parent, SupplierFileIdentifierCache identifierCache, IPreferenceStore preferenceStore) {
			super(parent, identifierCache, preferenceStore);
		}

		@Override
		protected void handleSelection(File[] files, DataExplorerTreeUI treeUI) {

			treeSelection = treeUI.getRoot();
			selectedItems.clear();
			for(File file : files) {
				Collection<ISupplierFileIdentifier> identifier = getIdentifierSupplier().apply(file);
				if(!identifier.isEmpty()) {
					selectedItems.put(file, identifier);
				}
			}
			validate();
		}

		@Override
		protected String getUserLocationPreferenceKey() {

			String key = inputWizardSettings.getUserLocationPreferenceKey();
			if(key != null) {
				return key;
			}
			return super.getUserLocationPreferenceKey();
		}
	}

	private InputWizardSettings inputWizardSettings;
	private DataExplorerTreeRoot treeSelection = DataExplorerTreeRoot.NONE;
	private Map<File, Collection<ISupplierFileIdentifier>> selectedItems = new HashMap<>();
	private MultiDataExplorerTreeUI explorerTreeUI;

	public InputEntriesWizardPage(InputWizardSettings inputWizardSettings) {
		//
		super(InputEntriesWizardPage.class.getName());
		this.inputWizardSettings = inputWizardSettings;
		//
		setTitle(inputWizardSettings.getTitle());
		setDescription(inputWizardSettings.getDescription());
		validate();
	}

	private void validate() {

		if(selectedItems.isEmpty()) {
			setPageComplete(false);
			setErrorMessage("Please select at least one valid data item");
		} else {
			setPageComplete(true);
			setErrorMessage(null);
		}
	}

	public Map<File, Collection<ISupplierFileIdentifier>> getSelectedItems() {

		return selectedItems;
	}

	@Override
	public void createControl(Composite parent) {

		explorerTreeUI = new WizardMultiDataExplorerTreeUI(parent, inputWizardSettings.getSupplierCache(), inputWizardSettings.getPreferenceStore());
		explorerTreeUI.expandLastDirectoryPath();
		setControl(explorerTreeUI.getControl());
	}

	public DataExplorerTreeRoot getTreeSelection() {

		return treeSelection;
	}

	public void savePath() {

		explorerTreeUI.saveLastDirectoryPath();
	}
}
