/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.converter.supplier.mzml.model;

import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.tsd.model.core.AbstractChromatogramTSD;
import org.eclipse.chemclipse.tsd.model.core.TypeTSD;

public class VendorChromatogram extends AbstractChromatogramTSD implements IVendorChromatogram {

	private static final long serialVersionUID = -1668241738371276152L;

	@Override
	public String getName() {

		return extractNameFromFile("IMS-qTOF");
	}

	@Override
	public String getLabelAxisX() {

		return "Ion [m/z]";
	}

	@Override
	public String getLabelAxisY() {

		return "Drift Time [ms]";
	}

	@Override
	public TypeTSD getTypeTSD() {

		return TypeTSD.GC_IMS; // TODO LC
	}

	@Override
	public double getPeakIntegratedArea() {

		return 0;
	}

	@Override
	public void fireUpdate(IChromatogramSelection chromatogramSelection) {

	}
}
