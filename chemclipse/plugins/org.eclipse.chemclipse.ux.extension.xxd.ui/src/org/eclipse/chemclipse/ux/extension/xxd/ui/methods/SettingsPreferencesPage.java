/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.methods;

import java.util.List;

import org.eclipse.chemclipse.support.settings.parser.InputValue;
import org.eclipse.chemclipse.xxd.process.support.ProcessorPreferences;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class SettingsPreferencesPage extends WizardPage {

	private List<InputValue> values;
	private ProcessorPreferences preferences;
	private boolean isDontAskAgain;
	private boolean isUseSystemDefaults;
	private String jsonSettings;

	public SettingsPreferencesPage(List<InputValue> values, ProcessorPreferences preferences) {
		super(SettingsPreferencesPage.class.getName());
		this.values = values;
		this.preferences = preferences;
	}

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Button buttonDefault = new Button(composite, SWT.RADIO);
		buttonDefault.setText("Use System Options");
		Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonUser = new Button(composite, SWT.RADIO);
		buttonUser.setText("Use Specific Options");
		SettingsUI settingsUI = new SettingsUI(composite, values);
		settingsUI.setLayoutData(new GridData(GridData.FILL_BOTH));
		Listener validationListener = new Listener() {

			@Override
			public void handleEvent(Event event) {

				if(buttonUser.getSelection()) {
					String validate = settingsUI.validate();
					setErrorMessage(validate);
					setPageComplete(validate == null);
				} else {
					setErrorMessage(null);
					setPageComplete(true);
				}
				try {
					jsonSettings = settingsUI.getJsonSettings();
				} catch(Exception e) {
					jsonSettings = null;
				}
			}
		};
		SelectionListener radioButtonListener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				settingsUI.setEnabled(buttonUser.getSelection());
				validationListener.handleEvent(null);
				isUseSystemDefaults = buttonDefault.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		};
		buttonDefault.addSelectionListener(radioButtonListener);
		buttonUser.addSelectionListener(radioButtonListener);
		Button buttonDontAskAgain = new Button(composite, SWT.CHECK);
		buttonDontAskAgain.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true));
		buttonDontAskAgain.setText("Remeber my decision and don't ask again");
		buttonDontAskAgain.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				isDontAskAgain = buttonDontAskAgain.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		if(preferences.isUseSystemDefaults()) {
			buttonDefault.setSelection(true);
		} else {
			buttonUser.setSelection(true);
		}
		buttonDontAskAgain.setSelection(isDontAskAgain = !preferences.isAskForSettings());
		radioButtonListener.widgetSelected(null);
		settingsUI.addWidgetListener(validationListener);
		setControl(composite);
	}

	public boolean getIsDontAskAgainEdited() {

		return isDontAskAgain;
	}

	public String getJsonSettingsEdited() {

		return jsonSettings;
	}

	public boolean getIsUseSystemDefaultsEdited() {

		return isUseSystemDefaults;
	}
}