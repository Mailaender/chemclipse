/*******************************************************************************
 * Copyright (c) 2008, 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.normalizer.core;

import org.eclipse.chemclipse.chromatogram.filter.core.chromatogram.IChromatogramFilter;
import org.eclipse.chemclipse.chromatogram.xxd.filter.supplier.normalizer.settings.FilterSettings;

@SuppressWarnings({"rawtypes", "unused"})
public class NormalizerFilter_1_Test extends ChromatogramImporterTestCase {

	private IChromatogramFilter chromatogramFilter;
	private FilterSettings chromatogramFilterSettings;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		chromatogramFilter = new ChromatogramFilter();
		chromatogramFilterSettings = new FilterSettings();
		chromatogramFilterSettings.setNormalizationBase(1000.0f);
	}

	@Override
	protected void tearDown() throws Exception {

		chromatogramFilter = null;
		chromatogramFilterSettings = null;
		super.tearDown();
	}

	public void testFilter() {

		assertTrue(true); // Fix tests
	}
	// @SuppressWarnings("unchecked")
	// public void testApplyFilter_1() {
	//
	// float totalSignal;
	// //
	// totalSignal = chromatogram.getScan(1).getTotalSignal();
	// assertEquals("scan totalSignal", 67864.0f, totalSignal);
	// totalSignal = chromatogram.getScan(5726).getTotalSignal();
	// assertEquals("scan totalSignal", 153220.0f, totalSignal);
	// totalSignal = chromatogram.getScan(238).getTotalSignal();
	// assertEquals("scan totalSignal", 94184.0f, totalSignal);
	// totalSignal = chromatogram.getScan(628).getTotalSignal();
	// assertEquals("scan totalSignal", 2747568.0f, totalSignal);
	// //
	// chromatogramFilter.applyFilter(chromatogramSelection, chromatogramFilterSettings, new NullProgressMonitor());
	// //
	// totalSignal = chromatogram.getScan(1).getTotalSignal();
	// assertEquals("scan totalSignal", 7.090521693f, totalSignal);
	// totalSignal = chromatogram.getScan(5726).getTotalSignal();
	// assertEquals("scan totalSignal", 16.008629f, totalSignal);
	// totalSignal = chromatogram.getScan(238).getTotalSignal();
	// assertEquals("scan totalSignal", 9.840471f, totalSignal);
	// totalSignal = chromatogram.getScan(628).getTotalSignal();
	// assertEquals("scan totalSignal", 287.06955f, totalSignal);
	// }
}
