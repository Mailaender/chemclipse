/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.cml.model;

import org.eclipse.chemclipse.wsd.model.core.implementation.SpectrumWSD;

public class VendorSpectrumWSD extends SpectrumWSD implements IVendorSpectrumWSD {

	private static final long serialVersionUID = 3417781730819762939L;
	//
	private String casNumber = "";

	@Override
	public String getCasNumber() {

		return casNumber;
	}

	@Override
	public void setCasNumber(String casNumber) {

		this.casNumber = casNumber;
	}
}