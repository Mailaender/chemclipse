/*******************************************************************************
 * Copyright (c) 2017, 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - content-proposal support
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.ITargetSupplier;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.targets.ITarget;
import org.eclipse.chemclipse.model.updates.ITargetUpdateListener;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.support.ui.events.IKeyEventProcessor;
import org.eclipse.chemclipse.support.ui.menu.ITableMenuEntry;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.support.ui.swt.IColumnMoveListener;
import org.eclipse.chemclipse.support.ui.swt.ITableSettings;
import org.eclipse.chemclipse.support.ui.workbench.DisplayUtils;
import org.eclipse.chemclipse.swt.ui.components.ISearchListener;
import org.eclipse.chemclipse.swt.ui.components.SearchSupportUI;
import org.eclipse.chemclipse.swt.ui.preferences.PreferencePageSWT;
import org.eclipse.chemclipse.ux.extension.ui.support.PartSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.ListSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceConstants;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageLists;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferencePageTargets;
import org.eclipse.chemclipse.ux.extension.xxd.ui.targets.ComboTarget;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtchart.extensions.core.IKeyboardSupport;

public class ExtendedTargetsUI {

	private static final String MENU_CATEGORY_TARGETS = "Targets";
	//
	private final Map<String, Object> map = new HashMap<String, Object>();
	//
	private Label labelTargetOption;
	private Label labelInfo;
	private Composite toolbarInfo;
	private Composite toolbarSearch;
	private Composite toolbarModify;
	private ComboTarget comboTarget;
	private Button buttonAddTarget;
	private Button buttonDeleteTarget;
	private TargetsListUI targetListUI;
	/*
	 * IScan,
	 * IPeak,
	 * IChromatogram
	 */
	private Object object;
	//
	private boolean showChromatogramTargets = false;
	private final ListSupport listSupport = new ListSupport();

	@Inject
	public ExtendedTargetsUI(Composite parent) {

		initialize(parent);
	}

	@Focus
	public void setFocus() {

		updateTargets();
	}

	public void update(Object object) {

		if(object instanceof IChromatogram) {
			if(showChromatogramTargets) {
				this.object = object;
				updateTargets();
			}
		} else if(object != null) {
			if(!showChromatogramTargets) {
				this.object = object;
				updateTargets();
			}
		} else {
			this.object = object;
			updateTargets();
		}
	}

	private void initialize(Composite parent) {

		parent.setLayout(new GridLayout(1, true));
		//
		createToolbarMain(parent);
		toolbarInfo = createToolbarInfo(parent);
		toolbarSearch = createToolbarSearch(parent);
		toolbarModify = createToolbarModify(parent);
		targetListUI = createTargetTable(parent);
		//
		PartSupport.setCompositeVisibility(toolbarInfo, true);
		PartSupport.setCompositeVisibility(toolbarSearch, false);
		PartSupport.setCompositeVisibility(toolbarModify, false);
		//
		targetListUI.setEditEnabled(false);
		applySettings();
	}

	private void createToolbarMain(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		// gridData.horizontalAlignment = SWT.END;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(7, false));
		//
		createTargetOptionLabel(composite);
		createButtonToggleToolbarInfo(composite);
		createButtonToggleOptionTargets(composite);
		createButtonToggleToolbarSearch(composite);
		createButtonToggleToolbarModify(composite);
		createButtonToggleEditModus(composite);
		createSettingsButton(composite);
		//
		updateTargetOptionLabel();
	}

	private void createTargetOptionLabel(Composite parent) {

		labelTargetOption = new Label(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		labelTargetOption.setLayoutData(gridData);
	}

	private Button createButtonToggleToolbarInfo(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle info toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarInfo);
				if(visible) {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImage.SIZE_16x16));
				} else {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_INFO, IApplicationImage.SIZE_16x16));
				}
			}
		});
		//
		return button;
	}

	private Button createButtonToggleOptionTargets(Composite parent) {

		Image imageChromatogram = ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CHROMATOGRAM, IApplicationImage.SIZE_16x16);
		Image imageScan = ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_MASS_SPECTRUM, IApplicationImage.SIZE_16x16);
		//
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle whether to display chromatogram or scan/peak targets.");
		button.setText("");
		button.setImage(showChromatogramTargets ? imageChromatogram : imageScan);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				showChromatogramTargets = !showChromatogramTargets;
				button.setImage(showChromatogramTargets ? imageChromatogram : imageScan);
				updateTargetOptionLabel();
			}
		});
		//
		return button;
	}

	private Button createButtonToggleToolbarSearch(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle search toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SEARCH, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarSearch);
				if(visible) {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SEARCH, IApplicationImage.SIZE_16x16));
				} else {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_SEARCH, IApplicationImage.SIZE_16x16));
				}
			}
		});
		//
		return button;
	}

	private Button createButtonToggleToolbarModify(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Toggle modify toolbar.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_DEFAULT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean visible = PartSupport.toggleCompositeVisibility(toolbarModify);
				if(visible) {
					comboTarget.updateContentProposals();
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_ACTIVE, IApplicationImage.SIZE_16x16));
				} else {
					button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_DEFAULT, IApplicationImage.SIZE_16x16));
				}
			}
		});
		//
		return button;
	}

	private Button createButtonToggleEditModus(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Enable/disable to edit the table.");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_EDIT_ENTRY_DEFAULT, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean editEnabled = !targetListUI.isEditEnabled();
				targetListUI.setEditEnabled(editEnabled);
				button.setImage(ApplicationImageFactory.getInstance().getImage((editEnabled) ? IApplicationImage.IMAGE_EDIT_ENTRY_ACTIVE : IApplicationImage.IMAGE_EDIT_ENTRY_DEFAULT, IApplicationImage.SIZE_16x16));
				updateInput();
			}
		});
		//
		return button;
	}

	private void createSettingsButton(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Open the Settings");
		button.setText("");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_CONFIGURE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				PreferenceManager preferenceManager = new PreferenceManager();
				preferenceManager.addToRoot(new PreferenceNode("1", new PreferencePageTargets()));
				preferenceManager.addToRoot(new PreferenceNode("2", new PreferencePageSWT()));
				preferenceManager.addToRoot(new PreferenceNode("3", new PreferencePageLists()));
				//
				PreferenceDialog preferenceDialog = new PreferenceDialog(e.display.getActiveShell(), preferenceManager);
				preferenceDialog.create();
				preferenceDialog.setMessage("Settings");
				if(preferenceDialog.open() == Window.OK) {
					try {
						applySettings();
					} catch(Exception e1) {
						MessageDialog.openError(e.display.getActiveShell(), "Settings", "Something has gone wrong to apply the settings.");
					}
				}
			}
		});
	}

	private Composite createToolbarInfo(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(1, false));
		//
		labelInfo = new Label(composite, SWT.NONE);
		labelInfo.setText("");
		labelInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//
		return composite;
	}

	private Composite createToolbarSearch(Composite parent) {

		SearchSupportUI searchSupportUI = new SearchSupportUI(parent, SWT.NONE);
		searchSupportUI.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchSupportUI.setSearchListener(new ISearchListener() {

			@Override
			public void performSearch(String searchText, boolean caseSensitive) {

				targetListUI.setSearchText(searchText, caseSensitive);
			}
		});
		//
		return searchSupportUI;
	}

	private Composite createToolbarModify(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(3, false));
		//
		comboTarget = createComboTarget(composite);
		buttonAddTarget = createButtonAdd(composite);
		buttonDeleteTarget = createButtonDelete(composite);
		//
		return composite;
	}

	private void setEditWidgetStatus(boolean enabled) {

		comboTarget.setEnabled(enabled);
		buttonAddTarget.setEnabled(enabled);
		buttonDeleteTarget.setEnabled(enabled);
	}

	private ComboTarget createComboTarget(Composite parent) {

		ComboTarget comboTarget = new ComboTarget(parent, SWT.NONE);
		comboTarget.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboTarget.setTargetUpdateListener(new ITargetUpdateListener() {

			@Override
			public void update(IIdentificationTarget identificationTarget) {

				if(identificationTarget != null) {
					setTarget(identificationTarget);
				}
			}
		});
		return comboTarget;
	}

	private Button createButtonAdd(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Add the target.");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_ADD, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				IIdentificationTarget identificationTarget = comboTarget.createTarget();
				if(identificationTarget != null) {
					setTarget(identificationTarget);
				}
			}
		});
		return button;
	}

	private Button createButtonDelete(Composite parent) {

		Button button = new Button(parent, SWT.PUSH);
		button.setText("");
		button.setToolTipText("Delete the selected target(s).");
		button.setImage(ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_DELETE, IApplicationImage.SIZE_16x16));
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				deleteTargets(e.display.getActiveShell());
			}
		});
		return button;
	}

	private TargetsListUI createTargetTable(Composite parent) {

		TargetsListUI listUI = new TargetsListUI(parent, SWT.BORDER);
		listUI.setEditingSupport();
		Table table = listUI.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		listUI.getControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {

				propagateTarget();
			}
		});
		/*
		 * Set/Save the column order.
		 */
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String preferenceName = PreferenceConstants.P_COLUMN_ORDER_TARGET_LIST;
		listSupport.setColumnOrder(table, preferenceStore.getString(preferenceName));
		listUI.addColumnMoveListener(new IColumnMoveListener() {

			@Override
			public void handle() {

				String columnOrder = listSupport.getColumnOrder(table);
				preferenceStore.setValue(preferenceName, columnOrder);
			}
		});
		/*
		 * Add the delete targets support.
		 */
		Shell shell = listUI.getTable().getShell();
		ITableSettings tableSettings = listUI.getTableSettings();
		addDeleteMenuEntry(shell, tableSettings);
		addVerifyTargetsMenuEntry(tableSettings);
		addUnverifyTargetsMenuEntry(tableSettings);
		addKeyEventProcessors(shell, tableSettings);
		listUI.applySettings(tableSettings);
		//
		return listUI;
	}

	private void addDeleteMenuEntry(Shell shell, ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Delete Target(s)";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_TARGETS;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				deleteTargets(shell);
			}
		});
	}

	private void addVerifyTargetsMenuEntry(ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Verify Target(s) Check";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_TARGETS;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				verifyTargets(true);
			}
		});
	}

	private void addUnverifyTargetsMenuEntry(ITableSettings tableSettings) {

		tableSettings.addMenuEntry(new ITableMenuEntry() {

			@Override
			public String getName() {

				return "Verify Target(s) Uncheck";
			}

			@Override
			public String getCategory() {

				return MENU_CATEGORY_TARGETS;
			}

			@Override
			public void execute(ExtendedTableViewer extendedTableViewer) {

				verifyTargets(false);
			}
		});
	}

	private void addKeyEventProcessors(Shell shell, ITableSettings tableSettings) {

		tableSettings.addKeyEventProcessor(new IKeyEventProcessor() {

			@Override
			public void handleEvent(ExtendedTableViewer extendedTableViewer, KeyEvent e) {

				if(e.keyCode == SWT.DEL) {
					/*
					 * DEL
					 */
					deleteTargets(shell);
				} else if(e.keyCode == IKeyboardSupport.KEY_CODE_LC_I && (e.stateMask & SWT.CTRL) == SWT.CTRL) {
					if((e.stateMask & SWT.ALT) == SWT.ALT) {
						/*
						 * CTRL + ALT + I
						 */
						verifyTargets(false);
					} else {
						/*
						 * CTRL + I
						 */
						verifyTargets(true);
					}
				} else {
					propagateTarget();
				}
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void verifyTargets(boolean verified) {

		Iterator iterator = targetListUI.getStructuredSelection().iterator();
		while(iterator.hasNext()) {
			Object object = iterator.next();
			if(object instanceof IIdentificationTarget) {
				IIdentificationTarget identificationTarget = (IIdentificationTarget)object;
				identificationTarget.setManuallyVerified(verified);
			}
		}
		updateTargets();
	}

	private void propagateTarget() {

		Table table = targetListUI.getTable();
		int index = table.getSelectionIndex();
		if(index >= 0) {
			TableItem tableItem = table.getItem(index);
			Object object = tableItem.getData();
			if(object instanceof IIdentificationTarget) {
				/*
				 * Fire updates
				 */
				IEventBroker eventBroker = Activator.getDefault().getEventBroker();
				if(eventBroker != null) {
					IScanMSD massSpectrum = getMassSpectrum();
					IIdentificationTarget target = (IIdentificationTarget)object;
					//
					DisplayUtils.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {

							eventBroker.send(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGET_UPDATE, target);
						}
					});
					//
					if(massSpectrum != null) {
						DisplayUtils.getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {

								/*
								 * Send the identification target update to let e.g. the molecule renderer react on an update.
								 */
								map.clear();
								IIdentificationTarget identificationTarget = (IIdentificationTarget)object;
								map.put(IChemClipseEvents.PROPERTY_IDENTIFICATION_TARGET_MASS_SPECTRUM_UNKNOWN, massSpectrum);
								map.put(IChemClipseEvents.PROPERTY_IDENTIFICATION_TARGET_ENTRY, identificationTarget);
								eventBroker.send(IChemClipseEvents.TOPIC_IDENTIFICATION_TARGET_MASS_SPECTRUM_UNKNOWN_UPDATE, map);
							}
						});
					}
				}
			}
		}
	}

	private void applySettings() {

		comboTarget.updateContentProposals();
	}

	private void updateTargets() {

		updateInput();
		updateWidgets();
		//
		targetListUI.sortTable();
		Table table = targetListUI.getTable();
		if(table.getItemCount() > 0) {
			table.setSelection(0);
			//
			IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
			boolean propagateTargetOnUpdate = preferenceStore.getBoolean(PreferenceConstants.P_PROPAGATE_TARGET_ON_UPDATE);
			if(propagateTargetOnUpdate) {
				DisplayUtils.getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {

						propagateTarget();
					}
				});
			}
		}
	}

	private void updateInput() {

		if(object instanceof ITargetSupplier) {
			String editInformation = targetListUI.isEditEnabled() ? "Edit is enabled." : "Edit is disabled.";
			labelInfo.setText("Targets - " + editInformation);
			ITargetSupplier targetSupplier = (ITargetSupplier)object;
			targetListUI.setInput(targetSupplier.getTargets());
			setEditWidgetStatus(true);
		} else {
			labelInfo.setText("No target data has been selected yet.");
			targetListUI.clear();
			setEditWidgetStatus(false);
			PartSupport.setCompositeVisibility(toolbarModify, false);
		}
	}

	private void updateWidgets() {

		boolean enabled = (object == null) ? false : true;
		comboTarget.setEnabled(enabled);
		buttonAddTarget.setEnabled(enabled);
		buttonDeleteTarget.setEnabled(enabled);
	}

	@SuppressWarnings("rawtypes")
	private void deleteTargets(Shell shell) {

		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setText("Delete Target(s)");
		messageBox.setMessage("Would you like to delete the selected target(s)?");
		if(messageBox.open() == SWT.YES) {
			/*
			 * Delete Target
			 */
			Iterator iterator = targetListUI.getStructuredSelection().iterator();
			while(iterator.hasNext()) {
				Object object = iterator.next();
				if(object instanceof ITarget) {
					deleteTarget((ITarget)object);
				}
			}
			updateTargets();
		}
	}

	private void deleteTarget(ITarget target) {

		if(object instanceof ITargetSupplier) {
			ITargetSupplier targetSupplier = (ITargetSupplier)object;
			targetSupplier.getTargets().remove(target);
		}
		/*
		 * Don't do an table update here, cause this method could be called several times in a loop.
		 */
	}

	private void setTarget(IIdentificationTarget identificationTarget) {

		if(object instanceof ITargetSupplier) {
			ITargetSupplier targetSupplier = (ITargetSupplier)object;
			targetSupplier.getTargets().add(identificationTarget);
		}
		//
		comboTarget.setText("");
		updateTargets();
	}

	/**
	 * May return null.
	 * 
	 * @return IScanMSD
	 */
	private IScanMSD getMassSpectrum() {

		if(object instanceof IScanMSD) {
			return (IScanMSD)object;
		} else if(object instanceof IPeakMSD) {
			IPeakMSD peakMSD = (IPeakMSD)object;
			return peakMSD.getExtractedMassSpectrum();
		} else {
			return null;
		}
	}

	private void updateTargetOptionLabel() {

		String text = showChromatogramTargets ? "Chromatogram Targets (Active)" : "Targets (Active)";
		labelTargetOption.setText(text);
	}
}
