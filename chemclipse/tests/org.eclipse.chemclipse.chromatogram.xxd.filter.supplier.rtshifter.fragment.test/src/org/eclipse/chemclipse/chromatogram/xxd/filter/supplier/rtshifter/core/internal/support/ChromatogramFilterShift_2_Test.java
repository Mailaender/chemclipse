/*******************************************************************************
 * Copyright (c) 2011, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.core.internal.support;

import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.exceptions.FilterException;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.rtshifter.settings.FilterSettingsShift;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;

public class ChromatogramFilterShift_2_Test extends ChromatogramTestCase {

	private IChromatogramMSD chromatogram;
	private IChromatogramSelectionMSD chromatogramSelection;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		chromatogram = getChromatogram();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testShiftForward_1() {

		IRegularMassSpectrum scan;
		/*
		 * PRE TESTS
		 */
		assertEquals(10, chromatogram.getNumberOfScans());
		assertEquals(1500, chromatogram.getScanDelay());
		scan = chromatogram.getSupplierScan(1);
		assertEquals(1500, scan.getRetentionTime());
		scan = chromatogram.getSupplierScan(10);
		assertEquals(10500, scan.getRetentionTime());
	}

	public void testShiftForward_2() {

		IRegularMassSpectrum scan;
		try {
			FilterSettingsShift supplierFilterSettings = new FilterSettingsShift(0, true);
			RetentionTimeShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(1500, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(1500, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(10500, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}

	public void testShiftForward_3() {

		IRegularMassSpectrum scan;
		try {
			FilterSettingsShift supplierFilterSettings = new FilterSettingsShift(1000, true);
			RetentionTimeShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(2500, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(2500, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(11500, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}

	public void testShiftForward_4() {

		IRegularMassSpectrum scan;
		try {
			FilterSettingsShift supplierFilterSettings = new FilterSettingsShift(1499, true);
			RetentionTimeShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(2999, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(2999, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(11999, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}

	public void testShiftForward_5() {

		IRegularMassSpectrum scan;
		try {
			FilterSettingsShift supplierFilterSettings = new FilterSettingsShift(1500, true);
			RetentionTimeShifter.shiftRetentionTimes(chromatogramSelection, supplierFilterSettings);
			assertEquals(10, chromatogram.getNumberOfScans());
			assertEquals(3000, chromatogram.getScanDelay());
			scan = chromatogram.getSupplierScan(1);
			assertEquals(3000, scan.getRetentionTime());
			scan = chromatogram.getSupplierScan(10);
			assertEquals(12000, scan.getRetentionTime());
		} catch(FilterException e) {
			assertTrue("FilterException", false);
		}
	}
}
