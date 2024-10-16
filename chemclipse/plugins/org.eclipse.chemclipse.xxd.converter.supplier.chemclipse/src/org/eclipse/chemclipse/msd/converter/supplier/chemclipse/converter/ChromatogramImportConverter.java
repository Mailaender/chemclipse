/*******************************************************************************
 * Copyright (c) 2008, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.chemclipse.converter;

import java.io.File;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramImportConverter;
import org.eclipse.chemclipse.converter.chromatogram.IChromatogramImportConverter;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.chemclipse.io.ChromatogramReaderMSD;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.IConstants;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.SpecificationValidator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

public class ChromatogramImportConverter extends AbstractChromatogramImportConverter implements IChromatogramImportConverter {

	private static final Logger logger = Logger.getLogger(ChromatogramImportConverter.class);

	@Override
	public IProcessingInfo convert(File file, IProgressMonitor monitor) {

		SubMonitor subMonitor = SubMonitor.convert(monitor, "Convert file " + file.getName(), 100);
		try {
			subMonitor.subTask("Validate");
			IProcessingInfo processingInfo = super.validate(file);
			if(!processingInfo.hasErrorMessages()) {
				/*
				 * Read the chromatogram.
				 */
				file = SpecificationValidator.validateSpecification(file);
				subMonitor.worked(1);
				IChromatogramMSDReader reader = new ChromatogramReaderMSD();
				monitor.subTask(IConstants.IMPORT_CHROMATOGRAM);
				try {
					IChromatogramMSD chromatogram = reader.read(file, subMonitor.split(99));
					processingInfo.setProcessingResult(chromatogram);
				} catch(Exception e) {
					logger.warn(e);
					processingInfo.addErrorMessage(IConstants.DESCRIPTION_IMPORT, "Something has definitely gone wrong with the file: " + file.getAbsolutePath());
				}
			}
			return processingInfo;
		} finally {
			SubMonitor.done(subMonitor);
		}
	}

	@Override
	public IProcessingInfo convertOverview(File file, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			file = SpecificationValidator.validateSpecification(file);
			IChromatogramMSDReader reader = new ChromatogramReaderMSD();
			try {
				IChromatogramOverview chromatogramOverview = reader.readOverview(file, monitor);
				processingInfo.setProcessingResult(chromatogramOverview);
			} catch(Exception e) {
				logger.warn(e);
				processingInfo.addErrorMessage(IConstants.DESCRIPTION_IMPORT, "Something has definitely gone wrong with the file: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}
}
