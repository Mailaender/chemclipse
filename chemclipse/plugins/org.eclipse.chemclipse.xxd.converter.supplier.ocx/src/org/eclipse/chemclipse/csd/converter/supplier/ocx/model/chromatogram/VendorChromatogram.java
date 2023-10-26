/*******************************************************************************
 * Copyright (c) 2014, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.ocx.model.chromatogram;

import org.eclipse.chemclipse.csd.model.core.AbstractChromatogramCSD;

public class VendorChromatogram extends AbstractChromatogramCSD implements IVendorChromatogram {

	private static final long serialVersionUID = 5173736989548346927L;

	public VendorChromatogram() {
		super();
	}

	@Override
	public String getName() {

		return extractNameFromFile("Chromatogram");
	}
}
