/*******************************************************************************
 * Copyright (c) 2015, 2024 Lablicate GmbH.
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

import org.eclipse.chemclipse.msd.model.core.IRegularMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.MassSpectrumType;

import junit.framework.TestCase;

public class RegularMassSpectrum_2_Test extends TestCase {

	private IRegularMassSpectrum massSpectrum;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		massSpectrum = new RegularMassSpectrum();
	}

	@Override
	protected void tearDown() throws Exception {

		massSpectrum = null;
		super.tearDown();
	}

	public void test_1() {

		massSpectrum.setMassSpectrometer((short)1);
		assertEquals(1, massSpectrum.getMassSpectrometer());
	}

	public void test_2() {

		massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		assertEquals(MassSpectrumType.PROFILE, massSpectrum.getMassSpectrumType());
	}

	public void test_3() {

		massSpectrum.setMassSpectrumType(MassSpectrumType.PROFILE);
		assertEquals("Profile", massSpectrum.getMassSpectrumType().label());
	}

	public void test_4() {

		massSpectrum.setTimeSegmentId(2);
		assertEquals(2, massSpectrum.getTimeSegmentId());
	}

	public void test_5() {

		massSpectrum.setCycleNumber(2);
		assertEquals(2, massSpectrum.getCycleNumber());
	}
}
