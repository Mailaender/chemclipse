/*******************************************************************************
 * Copyright (c) 2008, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.xic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.model.exceptions.AnalysisSupportException;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IVendorMassSpectrum;
import org.eclipse.chemclipse.msd.model.exceptions.NoExtractedIonSignalStoredException;
import org.eclipse.chemclipse.msd.model.implementation.ChromatogramMSD;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.VendorMassSpectrum;

import junit.framework.TestCase;

/**
 * @author eselmeister
 */
public class ExtractedIonSignalsModifier_2_Test extends TestCase {

	private IChromatogramMSD chromatogram;
	private IVendorMassSpectrum supplierMassSpectrum;
	private IIon supplierIon;
	private IExtractedIonSignals extractedIonSignals;
	private IExtractedIonSignal extractedIonSignal;
	private List<Integer> scans;
	private List<Integer> ions;
	private IExtractedIonSignalExtractor extractedIonSignalExtractor;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		/*
		 * Build a chromatogram and add scans with ions and no
		 * abundance.
		 */
		chromatogram = new ChromatogramMSD();
		for(int scan = 1; scan <= 100; scan++) {
			supplierMassSpectrum = new VendorMassSpectrum();
			for(int ion = 32; ion <= 104; ion++) {
				supplierIon = new Ion(ion, ion * 2);
				supplierMassSpectrum.addIon(supplierIon);
			}
			chromatogram.addScan(supplierMassSpectrum);
		}
		extractedIonSignalExtractor = new ExtractedIonSignalExtractor(chromatogram);
		extractedIonSignals = extractedIonSignalExtractor.getExtractedIonSignals();
		scans = new ArrayList<Integer>();
		scans.add(4);
		scans.add(7);
		scans.add(9);
		ions = new ArrayList<Integer>();
		ions.add(45);
		ions.add(76);
		ions.add(102);
		/*
		 * Set some ions to 0 to test the adjustment method.
		 */
		for(int scan : scans) {
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
			for(int ion : ions) {
				extractedIonSignal.setAbundance(ion, 0.0f, true);
			}
		}
	}

	@Override
	protected void tearDown() throws Exception {

		extractedIonSignal = null;
		extractedIonSignals = null;
		chromatogram = null;
		supplierIon = null;
		supplierMassSpectrum = null;
		scans = null;
		ions = null;
		super.tearDown();
	}

	public void testInitialization_1() {

		try {
			/*
			 * Test that the values are zero.
			 */
			for(int scan : scans) {
				extractedIonSignal = extractedIonSignals.getExtractedIonSignal(scan);
				for(int ion : ions) {
					assertEquals("TotalIonSignal before", 0.0f, extractedIonSignal.getAbundance(ion));
				}
			}
		} catch(NoExtractedIonSignalStoredException e) {
			assertTrue("NoExtractedIonSignalStoredException", false);
		}
	}

	public void testAdjustThresholdTransitions_1() {

		try {
			ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
			/*
			 * Test the adjusted value.
			 */
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(4);
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(45));
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(76));
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(102));
		} catch(AnalysisSupportException e) {
			assertTrue("AnalysisSupportException", false);
		} catch(NoExtractedIonSignalStoredException e) {
			assertTrue("NoExtractedIonSignalStoredException", false);
		}
	}

	public void testAdjustThresholdTransitions_2() {

		try {
			ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
			/*
			 * Test the adjusted value.
			 */
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(7);
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(45));
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(76));
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(102));
		} catch(AnalysisSupportException e) {
			assertTrue("AnalysisSupportException", false);
		} catch(NoExtractedIonSignalStoredException e) {
			assertTrue("NoExtractedIonSignalStoredException", false);
		}
	}

	public void testAdjustThresholdTransitions_3() {

		try {
			ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
			/*
			 * Test the adjusted value.
			 */
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(9);
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(45));
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(76));
			assertEquals("TotalIonSignal after", 35.054245f, extractedIonSignal.getAbundance(102));
		} catch(AnalysisSupportException e) {
			assertTrue("AnalysisSupportException", false);
		} catch(NoExtractedIonSignalStoredException e) {
			assertTrue("NoExtractedIonSignalStoredException", false);
		}
	}

	public void testAdjustThresholdTransitions_4() {

		try {
			ExtractedIonSignalsModifier.adjustThresholdTransitions(extractedIonSignals);
			/*
			 * Test the adjusted value.
			 */
			extractedIonSignal = extractedIonSignals.getExtractedIonSignal(5);
			assertEquals("TotalIonSignal after", 90.0f, extractedIonSignal.getAbundance(45));
			assertEquals("TotalIonSignal after", 152.0f, extractedIonSignal.getAbundance(76));
			assertEquals("TotalIonSignal after", 204.0f, extractedIonSignal.getAbundance(102));
		} catch(AnalysisSupportException e) {
			assertTrue("AnalysisSupportException", false);
		} catch(NoExtractedIonSignalStoredException e) {
			assertTrue("NoExtractedIonSignalStoredException", false);
		}
	}
}
