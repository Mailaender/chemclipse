/*******************************************************************************
 * Copyright (c) 2010, 2024 Lablicate GmbH.
 *
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.PathResolver;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.TestPathHelper;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.core.selection.ChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.ocx.versions.VersionConstants;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Ignore;

import junit.framework.TestCase;

@Ignore
public class ChromatogramImporterTestCase extends TestCase {

	protected IChromatogramMSD chromatogram;
	protected IChromatogramSelectionMSD chromatogramSelection;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		/*
		 * Import
		 */
		File fileImport = new File(PathResolver.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_CHROMATOGRAM_1));
		IProcessingInfo<IChromatogramMSD> processingInfo = ChromatogramConverterMSD.getInstance().convert(fileImport, VersionConstants.CONVERTER_ID_CHROMATOGRAM, new NullProgressMonitor());
		chromatogram = processingInfo.getProcessingResult();
		chromatogramSelection = new ChromatogramSelectionMSD(chromatogram);
	}

	@Override
	protected void tearDown() throws Exception {

		chromatogram = null;
		chromatogramSelection = null;
		super.tearDown();
	}
}
