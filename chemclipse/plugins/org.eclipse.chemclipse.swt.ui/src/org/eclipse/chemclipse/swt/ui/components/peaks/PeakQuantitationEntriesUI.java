/*******************************************************************************
 * Copyright (c) 2016 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.swt.ui.components.peaks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.quantitation.IQuantitationEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.swt.ui.internal.provider.PeakQuantitationEntriesContentProvider;
import org.eclipse.chemclipse.swt.ui.internal.provider.PeakQuantitationEntriesLabelProvider;
import org.eclipse.chemclipse.swt.ui.internal.provider.PeakQuantitationEntriesTableComparator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class PeakQuantitationEntriesUI {

	private ExtendedTableViewer tableViewer;
	private PeakQuantitationEntriesTableComparator peakQuantitationEntriesTableComparator;
	private String[] titles = {"Name", "Chemical Class", "Concentration", "Concentration Unit", "Area", "Calibration Method", "Cross Zero", "Description"};
	private int bounds[] = {100, 100, 100, 100, 100, 100, 100, 100};

	public PeakQuantitationEntriesUI(Composite parent, int style) {
		parent.setLayout(new FillLayout());
		/*
		 * E.g. Scan
		 */
		// SWT.VIRTUAL | SWT.FULL_SELECTION
		tableViewer = new ExtendedTableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.createColumns(titles, bounds);
		;
		tableViewer.setContentProvider(new PeakQuantitationEntriesContentProvider());
		tableViewer.setLabelProvider(new PeakQuantitationEntriesLabelProvider());
		/*
		 * Sorting the table.
		 */
		peakQuantitationEntriesTableComparator = new PeakQuantitationEntriesTableComparator();
		tableViewer.setComparator(peakQuantitationEntriesTableComparator);
		tableViewer.getControl().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(e.keyCode == 99 && e.stateMask == 262144) {
					/*
					 * The selected content will be placed to the clipboard if
					 * the user is using "Function + c". "Function-Key" 262144
					 * (stateMask) + "c" 99 (keyCode)
					 */
					tableViewer.copyToClipboard(titles);
					//
				} else if(e.keyCode == 127 && e.stateMask == 0) {
					/*
					 * Press "DEL" button.
					 */
					deleteSelectedQuantitationResults();
				}
			}
		});
		initContextMenu();
	}

	public void setFocus() {

		tableViewer.getControl().setFocus();
	}

	public void update(IPeak peak, boolean forceReload) {

		if(peak != null) {
			tableViewer.setInput(peak);
		}
	}

	public void clear() {

		tableViewer.setInput(null);
	}

	public ExtendedTableViewer getTableViewer() {

		return tableViewer;
	}

	// -----------------------------------------private methods
	private void initContextMenu() {

		MenuManager menuManager = new MenuManager("#PopUpMenu", "org.eclipse.chemclipse.msd.swt.ui.components.peak.peakquantitationentriesui.popup");
		menuManager.setRemoveAllWhenShown(true);
		/*
		 * Copy to clipboard
		 */
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {

				IAction action = new Action() {

					@Override
					public void run() {

						super.run();
						tableViewer.copyToClipboard(titles);
					}
				};
				action.setText("Copy selection to clipboard");
				manager.add(action);
			}
		});
		/*
		 * Delete selected targets
		 */
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {

				IAction action = new Action() {

					@Override
					public void run() {

						super.run();
						deleteSelectedQuantitationResults();
					}
				};
				action.setText("Delete selected quantitation results");
				manager.add(action);
			}
		});
		Menu menu = menuManager.createContextMenu(tableViewer.getControl());
		tableViewer.getControl().setMenu(menu);
	}

	/*
	 * Delete the selected targets after confirming the message box.
	 */
	private void deleteSelectedQuantitationResults() {

		Shell shell = Display.getCurrent().getActiveShell();
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
		messageBox.setText("Delete Selected Quantitation Results");
		messageBox.setMessage("Do you really want to delete the selected quantitation results?");
		int decision = messageBox.open();
		if(SWT.YES == decision) {
			/*
			 * Delete the selected items.
			 */
			Table table = tableViewer.getTable();
			int[] indices = table.getSelectionIndices();
			/*
			 * Delete the selected targets. Make a distinction between: -
			 * IChromatogram - IChromatogramPeak Don't delete entries in cause
			 * they are temporary: - IMassSpectrumIdentificationResult
			 */
			Object input = tableViewer.getInput();
			if(input instanceof IPeak) {
				List<IQuantitationEntry> quantitationEntriesToRemove = getQuantitationEntryList(table, indices);
				IPeak peak = (IPeak)input;
				peak.removeQuantitationEntries(quantitationEntriesToRemove);
			}
			/*
			 * Delete targets in table.
			 */
			table.remove(indices);
		}
	}

	private List<IQuantitationEntry> getQuantitationEntryList(Table table, int[] indices) {

		List<IQuantitationEntry> quantitationEntryList = new ArrayList<IQuantitationEntry>();
		for(int index : indices) {
			/*
			 * Get the selected item.
			 */
			TableItem tableItem = table.getItem(index);
			Object object = tableItem.getData();
			if(object instanceof IQuantitationEntry) {
				IQuantitationEntry target = (IQuantitationEntry)object;
				quantitationEntryList.add(target);
			}
		}
		return quantitationEntryList;
	}
}