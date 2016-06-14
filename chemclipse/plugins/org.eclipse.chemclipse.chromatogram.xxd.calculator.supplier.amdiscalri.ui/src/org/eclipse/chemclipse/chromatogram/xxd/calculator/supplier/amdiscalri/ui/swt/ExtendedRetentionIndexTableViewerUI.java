/*******************************************************************************
 * Copyright (c) 2016 Dr. Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.io.StandardsReader;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.IRetentionIndexEntry;
import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.model.RetentionIndexEntry;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ExtendedRetentionIndexTableViewerUI extends Composite {

	private static final Logger logger = Logger.getLogger(ExtendedRetentionIndexTableViewerUI.class);
	//
	private static final String ACTION_INITIALIZE = "ACTION_INITIALIZE";
	private static final String ACTION_CANCEL = "ACTION_CANCEL";
	private static final String ACTION_DELETE = "ACTION_DELETE";
	private static final String ACTION_ADD = "ACTION_ADD";
	private static final String ACTION_SELECT = "ACTION_SELECT";
	//
	private Button buttonCancel;
	private Button buttonDelete;
	private Button buttonAdd;
	//
	private Combo comboReferences;
	private Button buttonAddReference;
	private Text textRetentionTime;
	private Text textRetentionIndex;
	//
	private RetentionIndexTableViewerUI retentionIndexTableViewerUI;
	//
	private List<IRetentionIndexEntry> retentionIndexEntries;
	private List<IRetentionIndexEntry> availableRetentionIndexEntries;

	public ExtendedRetentionIndexTableViewerUI(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	public void setInput(List<IRetentionIndexEntry> retentionIndexEntries) {

		this.retentionIndexEntries = retentionIndexEntries;
		retentionIndexTableViewerUI.setInput(retentionIndexEntries);
	}

	public List<IRetentionIndexEntry> getRetentionIndexEntries() {

		return retentionIndexEntries;
	}

	private void initialize() {

		setLayout(new FillLayout());
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(6, false));
		//
		retentionIndexEntries = new ArrayList<IRetentionIndexEntry>(); // default list
		StandardsReader standardsReader = new StandardsReader();
		availableRetentionIndexEntries = standardsReader.getStandardsList();
		//
		createButtonField(composite);
		createAddReferenceField(composite);
		createTableField(composite);
		//
		enableButtonFields(ACTION_INITIALIZE);
	}

	private void createButtonField(Composite composite) {

		Label label = new Label(composite, SWT.NONE);
		label.setText("");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 5;
		label.setLayoutData(gridData);
		/*
		 * Buttons
		 */
		Composite compositeButtons = new Composite(composite, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(3, true));
		GridData gridDataComposite = new GridData();
		gridDataComposite.horizontalAlignment = SWT.RIGHT;
		compositeButtons.setLayoutData(gridDataComposite);
		//
		buttonCancel = new Button(compositeButtons, SWT.PUSH);
		buttonCancel.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CANCEL, IApplicationImage.SIZE_16x16));
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				comboReferences.setText("");
				textRetentionTime.setText("");
				enableButtonFields(ACTION_CANCEL);
			}
		});
		//
		buttonDelete = new Button(compositeButtons, SWT.PUSH);
		buttonDelete.setEnabled(false);
		buttonDelete.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImage.SIZE_16x16));
		buttonDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				Table table = retentionIndexTableViewerUI.getTable();
				int index = table.getSelectionIndex();
				if(index >= 0) {
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING);
					messageBox.setText("Delete reference(s)?");
					messageBox.setMessage("Would you like to delete the reference(s)?");
					if(messageBox.open() == SWT.OK) {
						//
						enableButtonFields(ACTION_DELETE);
						TableItem tableItem = table.getItem(index);
						Object object = tableItem.getData();
						if(object instanceof IRetentionIndexEntry) {
							IRetentionIndexEntry retentionIndexEntry = (IRetentionIndexEntry)object;
							retentionIndexEntries.remove(retentionIndexEntry);
							retentionIndexTableViewerUI.setInput(retentionIndexEntries);
						}
					}
				}
			}
		});
		//
		buttonAdd = new Button(compositeButtons, SWT.PUSH);
		buttonAdd.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImage.SIZE_16x16));
		buttonAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				enableButtonFields(ACTION_ADD);
			}
		});
	}

	private void createAddReferenceField(Composite composite) {

		comboReferences = new Combo(composite, SWT.BORDER);
		comboReferences.setText("");
		comboReferences.setItems(getAvailableStandards());
		comboReferences.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String name = comboReferences.getText().trim();
				IRetentionIndexEntry retentionIndexEntry = getRetentionIndexEntry(name);
				if(retentionIndexEntry != null) {
					textRetentionIndex.setText(Float.toString(retentionIndexEntry.getRetentionIndex()));
				} else {
					textRetentionIndex.setText("");
				}
			}
		});
		//
		Label labelRetentionTime = new Label(composite, SWT.NONE);
		labelRetentionTime.setText("RT:");
		//
		textRetentionTime = new Text(composite, SWT.BORDER);
		textRetentionTime.setText("");
		textRetentionTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		Label labelRetentionIndex = new Label(composite, SWT.NONE);
		labelRetentionIndex.setText("RI:");
		//
		textRetentionIndex = new Text(composite, SWT.BORDER);
		textRetentionIndex.setText("");
		textRetentionIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		buttonAddReference = new Button(composite, SWT.PUSH);
		buttonAddReference.setText("");
		buttonAddReference.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE_ADD, IApplicationImage.SIZE_16x16));
		buttonAddReference.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonAddReference.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					enableButtonFields(ACTION_INITIALIZE);
					//
					String name = comboReferences.getText().trim();
					int retentionTime = (int)(Double.parseDouble(textRetentionTime.getText().trim()) * AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
					float retentionIndex;
					String retentionIndexText = textRetentionIndex.getText().trim();
					if(retentionIndexText.equals("")) {
						IRetentionIndexEntry retentionIndexEntry = getRetentionIndexEntry(name);
						if(retentionIndexEntry != null) {
							retentionIndex = retentionIndexEntry.getRetentionIndex();
						} else {
							retentionIndex = 0.0f;
						}
					} else {
						retentionIndex = Float.parseFloat(retentionIndexText);
					}
					//
					comboReferences.setText("");
					textRetentionTime.setText("");
					textRetentionIndex.setText("");
					//
					IRetentionIndexEntry retentionIndexEntry = new RetentionIndexEntry(retentionTime, retentionIndex, name);
					retentionIndexEntries.add(retentionIndexEntry);
					retentionIndexTableViewerUI.setInput(retentionIndexEntries);
				} catch(Exception e1) {
					logger.warn(e1);
				}
			}
		});
	}

	private void createTableField(Composite composite) {

		retentionIndexTableViewerUI = new RetentionIndexTableViewerUI(composite, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 6;
		retentionIndexTableViewerUI.getTable().setLayoutData(gridData);
		retentionIndexTableViewerUI.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				enableButtonFields(ACTION_SELECT);
				Table table = retentionIndexTableViewerUI.getTable();
				int index = table.getSelectionIndex();
				TableItem tableItem = table.getItem(index);
				Object object = tableItem.getData();
				if(object instanceof IRetentionIndexEntry) {
					IRetentionIndexEntry retentionIndexEntry = (IRetentionIndexEntry)object;
					System.out.println(retentionIndexEntry.getName());
				}
			}
		});
	}

	private void enableButtonFields(String action) {

		enableFields(false);
		switch(action) {
			case ACTION_INITIALIZE:
				buttonAdd.setEnabled(true);
				break;
			case ACTION_CANCEL:
				buttonAdd.setEnabled(true);
				break;
			case ACTION_DELETE:
				buttonAdd.setEnabled(true);
				break;
			case ACTION_ADD:
				buttonCancel.setEnabled(true);
				comboReferences.setEnabled(true);
				textRetentionTime.setEnabled(true);
				textRetentionIndex.setEnabled(true);
				buttonAddReference.setEnabled(true);
				break;
			case ACTION_SELECT:
				buttonAdd.setEnabled(true);
				if(retentionIndexTableViewerUI.getTable().getSelectionIndex() >= 0) {
					buttonDelete.setEnabled(true);
				} else {
					buttonDelete.setEnabled(false);
				}
				break;
		}
	}

	private void enableFields(boolean enabled) {

		buttonCancel.setEnabled(enabled);
		buttonDelete.setEnabled(enabled);
		buttonAdd.setEnabled(enabled);
		//
		comboReferences.setEnabled(enabled);
		textRetentionTime.setEnabled(enabled);
		textRetentionIndex.setEnabled(enabled);
		buttonAddReference.setEnabled(enabled);
	}

	private String[] getAvailableStandards() {

		int size = availableRetentionIndexEntries.size();
		String[] availableStandards = new String[size];
		for(int i = 0; i < size; i++) {
			availableStandards[i] = availableRetentionIndexEntries.get(i).getName();
		}
		return availableStandards;
	}

	private IRetentionIndexEntry getRetentionIndexEntry(String name) {

		for(IRetentionIndexEntry retentionIndexEntry : availableRetentionIndexEntries) {
			if(retentionIndexEntry.getName().equals(name)) {
				return retentionIndexEntry;
			}
		}
		return null;
	}
}
