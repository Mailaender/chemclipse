/*******************************************************************************
 * Copyright (c) 2014, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.alfassi.comparator;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;

public class GeometricDistanceMassSpectrumComparator_3_Test extends MassSpectrumSetTestCase {

	private MassSpectrumComparator comparator;
	private IProcessingInfo<IComparisonResult> processingInfo;
	private IComparisonResult result;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		//
		IScanMSD unknown = sinapylAclohol.getMassSpectrum();
		IScanMSD reference = syringylAcetone.getMassSpectrum();
		//
		comparator = new MassSpectrumComparator();
		processingInfo = comparator.compare(unknown, reference);
		result = processingInfo.getProcessingResult();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertFalse(processingInfo.hasErrorMessages());
	}

	public void test2() {

		assertEquals(61.818207f, result.getMatchFactor());
	}

	public void test3() {

		assertEquals(61.820198f, result.getReverseMatchFactor());
	}
}
