/*******************************************************************************
 * Copyright (c) 2014, 2016 Dr. Philip Wenig.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.massbank.converter;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.chemclipse.msd.converter.processing.massspectrum.IMassSpectrumImportConverterProcessingInfo;
import org.eclipse.chemclipse.msd.converter.supplier.massbank.TestPathHelper;
import org.eclipse.chemclipse.msd.model.core.IMassSpectra;

import junit.framework.TestCase;

public class MassBankImportConverter_1_ITest extends TestCase {

	private MassBankImportConverter converter;
	private File file;
	private IMassSpectra massSpectra;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CE000001));
		converter = new MassBankImportConverter();
		IMassSpectrumImportConverterProcessingInfo processingInfo = converter.convert(file, new NullProgressMonitor());
		massSpectra = processingInfo.getMassSpectra();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	public void test1() {

		assertEquals(1, massSpectra.size());
	}
}
