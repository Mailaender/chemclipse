/*******************************************************************************
 * Copyright (c) 2016, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.ui.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.chromatogram.xxd.calculator.supplier.amdiscalri.impl.AlkaneIdentifier;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.exceptions.ReferenceMustNotBeNullException;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.IPeakComparisonResult;
import org.eclipse.chemclipse.model.identifier.IPeakLibraryInformation;
import org.eclipse.chemclipse.model.identifier.PeakComparisonResult;
import org.eclipse.chemclipse.model.identifier.PeakLibraryInformation;
import org.eclipse.chemclipse.model.implementation.IdentificationTarget;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.wizards.AbstractExtendedWizardPage;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.PeakTableRetentionIndexViewerUI;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.PeakTargetsViewerUI;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

public class PagePeakAssignment extends AbstractExtendedWizardPage {

	private static final Logger logger = Logger.getLogger(PagePeakAssignment.class);
	//
	private IRetentionIndexWizardElements wizardElements;
	private PeakTableRetentionIndexViewerUI peakTableViewerUI;
	private PeakTargetsViewerUI targetsViewerUI;
	private Combo comboStartIndexName;
	private Label labelIndexRange;
	private Combo comboStopIndexName;
	private Button buttonPrevious;
	private Text textCurrentIndexName;
	private Button buttonNext;
	private String[] availableStandards;
	private int indexSelectedStandard;
	//
	private String databaseName;
	//
	private static final int ACTION_INCREASE_INDEX = 1;
	private static final int ACTION_DECREASE_INDEX = 2;

	public PagePeakAssignment(IRetentionIndexWizardElements wizardElements) {
		//
		super(PagePeakAssignment.class.getName());
		setTitle("Peak Assigment");
		setDescription("Please assign the alkanes.");
		this.wizardElements = wizardElements;
		availableStandards = wizardElements.getAvailableStandards();
		//
		File file = new File(AlkaneIdentifier.getDatabase());
		databaseName = file.getName();
	}

	@Override
	public boolean canFinish() {

		if(getMessage() == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setDefaultValues() {

	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void setVisible(boolean visible) {

		super.setVisible(visible);
		if(visible) {
			IChromatogramSelection chromatogramSelection = wizardElements.getChromatogramSelection();
			if(chromatogramSelection != null && chromatogramSelection.getChromatogram() != null) {
				/*
				 * Set the start index.
				 */
				indexSelectedStandard = getStartIndex(availableStandards, wizardElements.getStartIndexName());
				if(indexSelectedStandard > -1) {
					textCurrentIndexName.setText(availableStandards[indexSelectedStandard]);
				} else {
					textCurrentIndexName.setText("");
				}
				/*
				 * Set the first selection.
				 */
				IChromatogram chromatogram = chromatogramSelection.getChromatogram();
				List<? extends IPeak> peaks = chromatogram.getPeaks();
				peakTableViewerUI.setInput(peaks);
				peakTableViewerUI.getTable().setSelection(0);
				//
				if(peaks.size() > 0) {
					IPeak peak = peaks.get(0);
					Set<IIdentificationTarget> targets = peak.getTargets();
					targetsViewerUI.setInput(targets);
					targetsViewerUI.getTable().setSelection(0);
				}
			} else {
				peakTableViewerUI.setInput(null);
				textCurrentIndexName.setText("");
			}
			//
			updateLabel();
			validateSelection();
		}
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		//
		createRetentionIndexField(composite);
		createAutoAssignField(composite);
		createPeakTableField(composite);
		createTargetSpinnerField(composite);
		createAssignIndexField(composite);
		createPeakTargetsField(composite);
		//
		validateSelection();
		setControl(composite);
	}

	private void createRetentionIndexField(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridDataComposite = new GridData(GridData.FILL_HORIZONTAL);
		gridDataComposite.grabExcessHorizontalSpace = true;
		gridDataComposite.horizontalSpan = 3;
		composite.setLayoutData(gridDataComposite);
		composite.setLayout(new GridLayout(2, true));
		//
		comboStartIndexName = new Combo(composite, SWT.NONE);
		comboStartIndexName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboStartIndexName.setItems(availableStandards);
		comboStartIndexName.setToolTipText("Select the start index.");
		comboStartIndexName.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				wizardElements.setStartIndexName(comboStartIndexName.getText().trim());
				validateIndexSelection();
				updateLabel();
			}
		});
		//
		comboStopIndexName = new Combo(composite, SWT.NONE);
		comboStopIndexName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboStopIndexName.setItems(availableStandards);
		comboStopIndexName.setToolTipText("Select the stop index.");
		comboStopIndexName.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				wizardElements.setStopIndexName(comboStopIndexName.getText().trim());
				validateIndexSelection();
				updateLabel();
			}
		});
		//
		labelIndexRange = new Label(composite, SWT.NONE);
		labelIndexRange.setText("");
		GridData gridDataLabel = new GridData(GridData.FILL_HORIZONTAL);
		gridDataLabel.grabExcessHorizontalSpace = true;
		gridDataLabel.horizontalSpan = 2;
		labelIndexRange.setLayoutData(gridDataLabel);
	}

	private void createAutoAssignField(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("Auto Assign Standards");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public void widgetSelected(SelectionEvent e) {

				MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
				messageBox.setText("Auto assign standards");
				messageBox.setMessage("Would you like to set all standards automatically?");
				if(messageBox.open() == SWT.YES) {
					IChromatogramSelection chromatogramSelection = wizardElements.getChromatogramSelection();
					if(chromatogramSelection != null && chromatogramSelection.getChromatogram() != null) {
						IChromatogram chromatogram = chromatogramSelection.getChromatogram();
						List<? extends IPeak> chromatogramPeaks = chromatogram.getPeaks();
						List<IRetentionIndexEntry> retentionIndexEntries = wizardElements.getSelectedRetentionIndexEntries();
						//
						if(chromatogramPeaks.size() == retentionIndexEntries.size()) {
							for(int i = 0; i < chromatogramPeaks.size(); i++) {
								IPeak chromatogramPeak = chromatogramPeaks.get(i);
								IRetentionIndexEntry retentionIndexEntry = retentionIndexEntries.get(i);
								setPeakTarget(chromatogramPeak, retentionIndexEntry.getName(), true);
							}
							/*
							 * Set the first peak.
							 */
							peakTableViewerUI.getTable().setSelection(0);
							chromatogramSelection.reset();
							IPeak selectedPeak = getSelectedPeak();
							if(selectedPeak != null) {
								targetsViewerUI.setInput(selectedPeak.getTargets());
								targetsViewerUI.getTable().setSelection(0);
							}
						} else {
							String message = "The number of peaks (" + chromatogramPeaks.size() + ") and selected standards (" + retentionIndexEntries.size() + ") is unequal.";
							updateStatus(message);
						}
					}
				}
			}
		});
	}

	private void createPeakTableField(Composite parent) {

		peakTableViewerUI = new PeakTableRetentionIndexViewerUI(parent, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 100;
		peakTableViewerUI.getTable().setLayoutData(gridData);
		peakTableViewerUI.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IPeak selectedPeak = getSelectedPeak();
				if(selectedPeak != null) {
					targetsViewerUI.setInput(selectedPeak.getTargets());
					targetsViewerUI.getTable().setSelection(0);
				}
			}
		});
	}

	private void createTargetSpinnerField(Composite parent) {

		buttonPrevious = new Button(parent, SWT.PUSH);
		buttonPrevious.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PREVIOUS, IApplicationImage.SIZE_16x16));
		buttonPrevious.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setCurrentIndexName(ACTION_DECREASE_INDEX);
			}
		});
		//
		textCurrentIndexName = new Text(parent, SWT.BORDER);
		textCurrentIndexName.setText("");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		textCurrentIndexName.setLayoutData(gridData);
		//
		buttonNext = new Button(parent, SWT.PUSH);
		buttonNext.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_NEXT, IApplicationImage.SIZE_16x16));
		buttonNext.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				setCurrentIndexName(ACTION_INCREASE_INDEX);
			}
		});
	}

	private void createAssignIndexField(Composite parent) {

		Button buttonAdd = new Button(parent, SWT.PUSH);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		buttonAdd.setLayoutData(gridData);
		buttonAdd.setText("Replace peak targets by selected index");
		buttonAdd.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EXECUTE_ADD, IApplicationImage.SIZE_16x16));
		buttonAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IPeak selectedPeak = getSelectedPeak();
				if(selectedPeak != null) {
					String name = textCurrentIndexName.getText().trim();
					setPeakTarget(selectedPeak, name, true);
				}
			}
		});
	}

	private void setPeakTarget(IPeak peak, String name, boolean deleteOtherTargets) {

		/*
		 * Delete all other targets.
		 */
		if(deleteOtherTargets) {
			peak.getTargets().clear();
		}
		//
		try {
			float FACTOR = 100.0f;
			IPeakLibraryInformation libraryInformation = new PeakLibraryInformation();
			libraryInformation.setName(name);
			libraryInformation.setDatabase(databaseName); // Important, otherwise LibraryService fails.
			IPeakComparisonResult comparisonResult = new PeakComparisonResult(FACTOR, FACTOR, FACTOR, FACTOR, FACTOR);
			IIdentificationTarget peakTarget = new IdentificationTarget(libraryInformation, comparisonResult);
			peakTarget.setIdentifier(AlkaneIdentifier.IDENTIFIER);
			peak.getTargets().add(peakTarget);
			targetsViewerUI.setInput(peak.getTargets());
			/*
			 * Go to the next index.
			 */
			setCurrentIndexName(ACTION_INCREASE_INDEX);
		} catch(ReferenceMustNotBeNullException e1) {
			logger.warn(e1);
		}
	}

	private void createPeakTargetsField(Composite parent) {

		targetsViewerUI = new PeakTargetsViewerUI(parent, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 100;
		targetsViewerUI.getTable().setLayoutData(gridData);
		targetsViewerUI.getControl().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if(e.keyCode == 127 && e.stateMask == 0) {
					/*
					 * Press "DEL" button.
					 */
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					messageBox.setText("Delete identification(s)");
					messageBox.setMessage("Would you like to delete the identification(s)?");
					if(messageBox.open() == SWT.YES) {
						/*
						 * Delete the identifications.
						 */
						IPeak chromatogramPeakMSD = getSelectedPeak();
						if(chromatogramPeakMSD != null) {
							Table table = targetsViewerUI.getTable();
							int[] indices = table.getSelectionIndices();
							List<IIdentificationTarget> targetsToRemove = getPeakTargetList(table, indices);
							chromatogramPeakMSD.getTargets().removeAll(targetsToRemove);
							targetsViewerUI.setInput(chromatogramPeakMSD.getTargets());
							validateSelection();
						}
					}
				}
			}
		});
	}

	/*
	 * May return null.
	 */
	private IPeak getSelectedPeak() {

		Table table = peakTableViewerUI.getTable();
		int index = table.getSelectionIndex();
		Object object = peakTableViewerUI.getElementAt(index);
		if(object instanceof IPeak) {
			return (IPeak)object;
		} else {
			return null;
		}
	}

	/*
	 * If no index is found, -1 will be returned.
	 */
	private int getStartIndex(String[] availableStandards, String startIndexName) {

		for(int i = 0; i < availableStandards.length; i++) {
			if(availableStandards[i].equals(startIndexName)) {
				return i;
			}
		}
		return -1;
	}

	private void setCurrentIndexName(int action) {

		switch(action) {
			case ACTION_INCREASE_INDEX:
				buttonPrevious.setEnabled(true);
				indexSelectedStandard++;
				if(indexSelectedStandard >= availableStandards.length) {
					indexSelectedStandard = availableStandards.length - 1;
					buttonNext.setEnabled(false);
				}
				break;
			case ACTION_DECREASE_INDEX:
				buttonNext.setEnabled(true);
				indexSelectedStandard--;
				if(indexSelectedStandard < 0) {
					indexSelectedStandard = 0;
					buttonPrevious.setEnabled(false);
				}
				break;
		}
		//
		textCurrentIndexName.setText(availableStandards[indexSelectedStandard]);
	}

	private List<IIdentificationTarget> getPeakTargetList(Table table, int[] indices) {

		List<IIdentificationTarget> targetList = new ArrayList<IIdentificationTarget>();
		for(int index : indices) {
			/*
			 * Get the selected item.
			 */
			TableItem tableItem = table.getItem(index);
			Object object = tableItem.getData();
			if(object instanceof IIdentificationTarget) {
				IIdentificationTarget target = (IIdentificationTarget)object;
				targetList.add(target);
			}
		}
		return targetList;
	}

	private void validateSelection() {

		String message = null;
		/*
		 * Updates the status
		 */
		updateStatus(message);
	}

	private void updateLabel() {

		List<IRetentionIndexEntry> retentionIndexEntries = wizardElements.getSelectedRetentionIndexEntries();
		int size = wizardElements.getChromatogramSelection().getChromatogram().getPeaks().size();
		labelIndexRange.setText("Number of peaks (" + size + ") -> Selected Standards (" + retentionIndexEntries.size() + ")");
	}

	private void validateIndexSelection() {

		String message = null;
		/*
		 * Start index
		 */
		if(message == null) {
			String startIndexName = wizardElements.getStartIndexName();
			if(startIndexName.equals("")) {
				message = "Please select and start index.";
			} else {
				if(getComboIndex(startIndexName) == -1) {
					message = "The select start index is not valid.";
				}
			}
		}
		/*
		 * Stop index
		 */
		if(message == null) {
			String stopIndexName = wizardElements.getStopIndexName();
			if(stopIndexName.equals("")) {
				message = "Please select and stop index.";
			} else {
				if(getComboIndex(stopIndexName) == -1) {
					message = "The select stop index is not valid.";
				}
			}
		}
		/*
		 * Updates the status
		 */
		updateStatus(message);
	}

	private int getComboIndex(String name) {

		for(int i = 0; i < availableStandards.length; i++) {
			if(availableStandards[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}
}
