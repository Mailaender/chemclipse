/*******************************************************************************
 * Copyright (c) 2016, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.csv.heatmap;

import java.io.File;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramImportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramImportConverter;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramImportConverter extends AbstractChromatogramImportConverter implements IChromatogramImportConverter {

	private static final String DESCRIPTION = "CSV Heatmap Result Import Converter";

	@Override
	public IProcessingInfo convert(File file, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addErrorMessage(DESCRIPTION, "The heatmap converter does not support to import heatmap result files.");
		return processingInfo;
	}

	@Override
	public IProcessingInfo convertOverview(File file, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addErrorMessage(DESCRIPTION, "The heatmap converter does not support to import heatmap result files.");
		return processingInfo;
	}
}
