/*******************************************************************************
 * Copyright (c) 2011, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.internal.converter;

import java.io.File;

import org.eclipse.chemclipse.msd.converter.supplier.matlab.parafac.TestPathHelper;

import junit.framework.TestCase;

public class SpecificationValidator_1_Test extends TestCase {

	private File file;
	private String spec;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		spec = TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_SPEC);
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void testValidateAgilentSpecification_1() {

		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_1));
		file = SpecificationValidator.validateSpecification(file);
		assertEquals("File", spec, file.getAbsolutePath());
	}

	public void testValidateAgilentSpecification_3() {

		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_3));
		file = SpecificationValidator.validateSpecification(file);
		assertEquals("File", spec, file.getAbsolutePath());
	}

	public void testValidateAgilentSpecification_4() {

		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.VALIDATOR_TEST_4));
		file = SpecificationValidator.validateSpecification(file);
		assertEquals("File", spec, file.getAbsolutePath());
	}
}
