/*******************************************************************************
 * Copyright (c) 2008, 2022 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core.support;

import java.util.Set;

import org.eclipse.chemclipse.model.core.MarkedTraceModus;

import junit.framework.TestCase;

public class SelectedIons_1_Test extends TestCase {

	private IMarkedIons selectedIons;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		selectedIons = new MarkedIons(MarkedTraceModus.INCLUDE);
	}

	@Override
	protected void tearDown() throws Exception {

		selectedIons = null;
		super.tearDown();
	}

	public void testContains_1() {

		assertFalse("contains", selectedIons.getIonsNominal().contains(5));
	}

	public void testContains_2() {

		selectedIons.add(new MarkedIon(5));
		assertTrue("contains", selectedIons.getIonsNominal().contains(5));
	}

	public void testContains_3() {

		selectedIons.add(new MarkedIon(5));
		selectedIons.remove(new MarkedIon(5));
		assertFalse("contains", selectedIons.getIonsNominal().contains(5));
	}

	public void testContains_4() {

		selectedIons.add(new MarkedIon(10));
		selectedIons.add(new MarkedIon(5));
		selectedIons.add(new MarkedIon(20));
		assertTrue("contains", selectedIons.getIonsNominal().contains(20));
	}

	public void testContains_5() {

		selectedIons.add(10, 12);
		Set<Integer> selectedIonsNominal = selectedIons.getIonsNominal();
		assertFalse("contains", selectedIonsNominal.contains(20));
		assertTrue("contains", selectedIonsNominal.contains(10));
		assertTrue("contains", selectedIonsNominal.contains(11));
		assertTrue("contains", selectedIonsNominal.contains(12));
	}

	public void testContains_6() {

		selectedIons.add(12, 10);
		Set<Integer> selectedIonsNominal = selectedIons.getIonsNominal();
		assertFalse("contains", selectedIonsNominal.contains(20));
		assertTrue("contains", selectedIonsNominal.contains(10));
		assertTrue("contains", selectedIonsNominal.contains(11));
		assertTrue("contains", selectedIonsNominal.contains(12));
	}

	public void testContains_7() {

		selectedIons.add(12, 12);
		Set<Integer> selectedIonsNominal = selectedIons.getIonsNominal();
		assertFalse("contains", selectedIonsNominal.contains(20));
		assertFalse("contains", selectedIonsNominal.contains(10));
		assertFalse("contains", selectedIonsNominal.contains(11));
		assertTrue("contains", selectedIonsNominal.contains(12));
	}

	public void testSize_8() {

		selectedIons.add(12, 12);
		assertEquals("size", 1, selectedIons.getIonsNominal().size());
	}

	public void testSize_9() {

		selectedIons.add(new MarkedIon(58));
		selectedIons.add(new MarkedIon(48));
		selectedIons.add(new MarkedIon(372));
		assertEquals("size", 3, selectedIons.getIonsNominal().size());
	}

	public void testSize_10() {

		assertEquals("size", 0, selectedIons.getIonsNominal().size());
	}
}
