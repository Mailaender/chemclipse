/*******************************************************************************
 * Copyright (c) 2012, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.identifier.supplier.nist.ui.dialogs;

import org.eclipse.chemclipse.msd.identifier.supplier.nist.preferences.PreferenceSupplier;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class OpenGUIDialog extends Dialog {

	private String title = "";
	private String message = "";
	private Label messageLabel;
	private Button showGUIDialog;

	public OpenGUIDialog(Shell parentShell, String title, String message) {
		super(parentShell);
		if(title != null) {
			this.title = title;
		}
		if(message != null) {
			this.message = message;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		if(title != null) {
			shell.setText(title);
		}
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void buttonPressed(int buttonId) {

		/*
		 * Set values only if the OK button has been clicked.
		 */
		if(buttonId == IDialogConstants.OK_ID) {
			/*
			 * Check if the user would like to see the message again.
			 */
			PreferenceSupplier.setShowGUIDialog(showGUIDialog.getSelection());
		}
		super.buttonPressed(buttonId);
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea(Composite parent) {

		Composite composite = (Composite)super.createDialogArea(parent);
		GridData layoutData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		layoutData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		createMessage(composite);
		createCheckboxes(composite, layoutData);
		return composite;
	}

	/**
	 * Sets the dialog message.
	 */
	private void createMessage(Composite parent) {

		if(message != null) {
			GridData layoutData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
			layoutData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
			layoutData.minimumHeight = 100;
			messageLabel = new Label(parent, SWT.WRAP);
			messageLabel.setLayoutData(layoutData);
			messageLabel.setText(message);
			messageLabel.setFont(parent.getFont());
		}
	}

	/**
	 * Creates the check boxes.
	 * 
	 * @param parent
	 * @param layoutData
	 */
	private void createCheckboxes(Composite parent, GridData layoutData) {

		showGUIDialog = new Button(parent, SWT.CHECK);
		showGUIDialog.setText("Show this dialog.");
		showGUIDialog.setLayoutData(layoutData);
		showGUIDialog.setSelection(PreferenceSupplier.isShowGUIDialog());
	}
}
