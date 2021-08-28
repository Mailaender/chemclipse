/*******************************************************************************
 * Copyright (c) 2008, 2021 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.numeric.core;

import junit.framework.TestCase;

public class PointYComparator_2_Test extends TestCase {

	private IPoint point1;
	private double x1 = 24.3;
	private double y1 = 456.7;
	private IPoint point2;
	private double x2 = 25.3;
	private double y2 = 457.7;
	private PointYComparator pointYComparator;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		point1 = new Point(x1, y1);
		point2 = new Point(x2, y2);
		pointYComparator = new PointYComparator();
	}

	@Override
	protected void tearDown() throws Exception {

		point1 = null;
		point2 = null;
		pointYComparator = null;
		super.tearDown();
	}

	public void testComparator_1() {

		assertEquals("Compare", -1, pointYComparator.compare(point1, point2));
	}

	public void testComparator_2() {

		assertEquals("Compare", 1, pointYComparator.compare(point2, point1));
	}
}
