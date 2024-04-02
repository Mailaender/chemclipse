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
package org.eclipse.chemclipse.msd.model.implementation;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IIonBounds;

import junit.framework.TestCase;

/**
 * Test the method normalize() and normalize(float base) of mass spectrum. +
 * ion = new DefaultIon(45.5f, 78500.2f); +
 * massSpectrum.addIon(ion); + ion = new
 * DefaultIon(104.1f, 120000.4f); +
 * massSpectrum.addIon(ion); + ion = new
 * DefaultIon(32.6f, 890520.4f); +
 * massSpectrum.addIon(ion); + ion = new
 * DefaultIon(105.7f, 120000.4f); +
 * massSpectrum.addIon(ion); + ion = new
 * DefaultIon(28.2f, 33000.5f); +
 * massSpectrum.addIon(ion);
 * 
 * @author eselmeister
 */
public class MassSpectrum_15_Test extends TestCase {

	private ScanMSD massSpectrum;
	private Ion ion;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		massSpectrum = new ScanMSD();
		ion = new Ion(45.5f, 78500.2f);
		massSpectrum.addIon(ion);
		ion = new Ion(104.1f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(32.6f, 890520.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(105.7f, 120000.4f);
		massSpectrum.addIon(ion);
		ion = new Ion(28.2f, 33000.5f);
		massSpectrum.addIon(ion);
		massSpectrum.normalize(1000.0f);
	}

	@Override
	protected void tearDown() throws Exception {

		massSpectrum = null;
		ion = null;
		super.tearDown();
	}

	public void testGetNormalizationBase_1() {

		assertEquals("normalizationBase", 1000.0f, massSpectrum.getNormalizationBase());
	}

	public void testIsNormalized_1() {

		assertTrue("isNormalized", massSpectrum.isNormalized());
	}

	public void testGetIons_1() {

		assertEquals("normalizationBase", 1000.0f, massSpectrum.getNormalizationBase());
		assertTrue("isNormalized", massSpectrum.isNormalized());
		assertEquals("getIons", 5, massSpectrum.getIons().size());
	}

	public void testGetTotalSignal_1() {

		assertEquals("getTotalSignal", 1394.7147f, massSpectrum.getTotalSignal());
	}

	public void testGetExtractedIonSignal_1() {

		assertEquals("getExtractedIonSignal", 0.0f, massSpectrum.getExtractedIonSignal().getAbundance(0));
	}

	public void testGetExtractedIonSignal_2() {

		assertEquals("getExtractedIonSignal", 134.75312f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(104));
	}

	public void testGetExtractedIonSignal_3() {

		assertEquals("getExtractedIonSignal", 1000.0f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(33));
	}

	public void testGetExtractedIonSignal_4() {

		assertEquals("getExtractedIonSignal", 134.75312f, massSpectrum.getExtractedIonSignal(25, 120).getAbundance(106));
	}

	public void testGetBasePeak_1() {

		assertEquals("getBasePeak", 32.599998474121094d, massSpectrum.getBasePeak());
	}

	public void testGetBasePeakAbundance_1() {

		assertEquals("getBasePeakAbundance", 1000.0f, massSpectrum.getBasePeakAbundance());
	}

	public void testGetHighestAbundance_1() {

		assertEquals("getHighestAbundance", 1000.0f, massSpectrum.getHighestAbundance().getAbundance());
	}

	public void testGetHighestAbundance_2() {

		assertEquals("getHighestAbundance", 32.599998474121094d, massSpectrum.getHighestAbundance().getIon());
	}

	public void testGetHighestIon_1() {

		assertEquals("getHighestIon", 134.75311f, massSpectrum.getHighestIon().getAbundance());
	}

	public void testGetHighestIon_2() {

		assertEquals("getHighestIon", 105.69999694824219d, massSpectrum.getHighestIon().getIon());
	}

	public void testGetLowestAbundance_1() {

		assertEquals("getLowestAbundance", 37.057545f, massSpectrum.getLowestAbundance().getAbundance());
	}

	public void testGetLowestAbundance_2() {

		assertEquals("getLowestAbundance", 28.200000762939453d, massSpectrum.getLowestAbundance().getIon());
	}

	public void testGetLowestIon_1() {

		assertEquals("getLowestIon", 37.057545f, massSpectrum.getLowestIon().getAbundance());
	}

	public void testGetLowestIon_2() {

		assertEquals("getLowestIon", 28.200000762939453d, massSpectrum.getLowestIon().getIon());
	}

	public void testGetIonBounds_1() {

		IIonBounds bounds = massSpectrum.getIonBounds();
		assertEquals("getLowestIon().getAbundance()", 37.057545f, bounds.getLowestIon().getAbundance());
		assertEquals("getLowestIon().getIon()", 28.200000762939453d, bounds.getLowestIon().getIon());
		assertEquals("getHighestIon().getAbundance()", 134.75312f, bounds.getHighestIon().getAbundance());
		assertEquals("getHighestIon().getIon()", 105.69999694824219d, bounds.getHighestIon().getIon());
	}

	public void testGetNumberOfIons_1() {

		assertEquals("getNumberOfIons", 5, massSpectrum.getNumberOfIons());
	}

	public void testGetIon_1() {

		IIon ion;
		ion = massSpectrum.getIon(5);
		assertEquals("getIon", null, ion);
		ion = massSpectrum.getIon(46);
		assertTrue("getIon", ion != null);
		assertEquals("getIon(46) abundance", 88.15093f, ion.getAbundance());
		assertEquals("getIon(46) ion", 46.0d, ion.getIon());
	}
}
