/*******************************************************************************
 * Copyright (c) 2022, 2024 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzml.converter;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.mzml.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzml.converter.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

import junit.framework.TestCase;

public class ChromatogramImportConverterTinyProteoWizard10_ITest extends TestCase {

	private IVendorChromatogram chromatogram;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		File importFile = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_TINY_PWIZ_1_0));
		ChromatogramImportConverter converter = new ChromatogramImportConverter();
		IProcessingInfo<IChromatogramMSD> processingInfo = converter.convert(importFile, new NullProgressMonitor());
		chromatogram = (VendorChromatogram)processingInfo.getProcessingResult();
	}

	@Test
	public void testInstrument() {

		assertEquals("LCQ Deca", chromatogram.getInstrument());
	}

	@Test
	public void testSample() {

		assertEquals("Sample1", chromatogram.getSampleName());
	}

	@Test
	public void testOperator() {

		assertEquals("William Pennington, Higglesworth University, 12 Higglesworth Avenue, 12045, HI, USA, http://www.higglesworth.edu/, wpennington@higglesworth.edu", chromatogram.getOperator());
	}

	@Test
	public void testEditHistory() {

		assertEquals("deisotoping", chromatogram.getEditHistory().get(0).getDescription());
		assertEquals("charge deconvolution", chromatogram.getEditHistory().get(1).getDescription());
		assertEquals("peak picking", chromatogram.getEditHistory().get(2).getDescription());
		assertEquals("Conversion to mzML", chromatogram.getEditHistory().get(3).getDescription());
	}

	@Test
	public void testNumberOfScans() {

		assertEquals("NumberOfScans", 3, chromatogram.getNumberOfScans());
	}

	@Test
	public void testStartRetentionTime() {

		assertEquals("Start RT", 353430, chromatogram.getStartRetentionTime());
	}

	@Test
	public void testStopRetentionTime() {

		assertEquals("Stop RT", 42050, chromatogram.getStopRetentionTime());
	}

	@Test
	public void testTotalSignal() {

		assertEquals("Total Signal", 350.0f, chromatogram.getTotalSignal());
	}

	@Test
	public void testMaxIonAbundance() {

		assertEquals("Max Signal", 20.0f, chromatogram.getMaxIonAbundance());
	}

	@Test
	public void testFirstScan()  {

		IRegularMassSpectrum massSpectrum = (IRegularMassSpectrum)chromatogram.getScan(1);
		assertEquals("Ions", 15, massSpectrum.getNumberOfIons());
		assertEquals(15f, massSpectrum.getIon(0).getAbundance());
		assertEquals(14f, massSpectrum.getIon(1).getAbundance());
		assertEquals(13f, massSpectrum.getIon(2).getAbundance());
		assertEquals(12f, massSpectrum.getIon(3).getAbundance());
		assertEquals(11f, massSpectrum.getIon(4).getAbundance());
		assertEquals(10f, massSpectrum.getIon(5).getAbundance());
		assertEquals(9f, massSpectrum.getIon(6).getAbundance());
		assertEquals(8f, massSpectrum.getIon(7).getAbundance());
		assertEquals(7f, massSpectrum.getIon(8).getAbundance());
		assertEquals(6f, massSpectrum.getIon(9).getAbundance());
		assertEquals(5f, massSpectrum.getIon(10).getAbundance());
		assertEquals(4f, massSpectrum.getIon(11).getAbundance());
		assertEquals(3f, massSpectrum.getIon(12).getAbundance());
		assertEquals(2f, massSpectrum.getIon(13).getAbundance());
		assertEquals(1f, massSpectrum.getIon(14).getAbundance());
	}
}
