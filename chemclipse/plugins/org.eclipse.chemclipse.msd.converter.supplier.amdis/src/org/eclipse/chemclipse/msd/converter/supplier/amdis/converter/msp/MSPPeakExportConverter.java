/*******************************************************************************
 * Copyright (c) 2012, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.amdis.converter.msp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IPeaks;
import org.eclipse.chemclipse.msd.converter.peak.AbstractPeakExportConverter;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.internal.converter.SpecificationValidatorMSP;
import org.eclipse.chemclipse.msd.converter.supplier.amdis.io.PeakWriterMSP;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * NAME FIELD:
 * If the mass spectrum is a type of IRegularLibraryMassSpectrum, than getLibraryInformation().getName() will be used,
 * otherwise massSpectrum.getIdentifier().
 * 
 * @author chemclipse
 * 
 */
public class MSPPeakExportConverter extends AbstractPeakExportConverter {

	private static final Logger logger = Logger.getLogger(MSPPeakExportConverter.class);
	private static final String DESCRIPTION = "AMDIS MSP Peak Export";

	@Override
	public IProcessingInfo convert(File file, IPeakMSD peak, boolean append, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		/*
		 * Checks that file and mass spectrum are not null.
		 */
		file = SpecificationValidatorMSP.validateSpecification(file);
		IProcessingInfo processingInfoValidate = validate(file, peak);
		if(processingInfoValidate.hasErrorMessages()) {
			processingInfo.addMessages(processingInfoValidate);
		} else {
			try {
				/*
				 * Convert the mass spectrum.
				 */
				PeakWriterMSP peakWriter = new PeakWriterMSP();
				peakWriter.write(file, peak, append, monitor);
				processingInfo.setProcessingResult(file);
			} catch(FileNotFoundException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "The file couldn't be found: " + file.getAbsolutePath());
			} catch(FileIsNotWriteableException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "The file is not writeable: " + file.getAbsolutePath());
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "Something has gone completely wrong: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo convert(File file, IPeaks peaks, boolean append, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		/*
		 * Checks that file and mass spectra are not null.
		 */
		file = SpecificationValidatorMSP.validateSpecification(file);
		IProcessingInfo processingInfoValidate = validate(file, peaks);
		if(processingInfoValidate.hasErrorMessages()) {
			processingInfo.addMessages(processingInfoValidate);
		} else {
			try {
				/*
				 * Convert the mass spectra.
				 */
				PeakWriterMSP peakWriter = new PeakWriterMSP();
				peakWriter.write(file, peaks, append, monitor);
				processingInfo.setProcessingResult(file);
			} catch(FileNotFoundException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "The file couldn't be found: " + file.getAbsolutePath());
			} catch(FileIsNotWriteableException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "The file is not writeable: " + file.getAbsolutePath());
			} catch(IOException e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "Something has gone completely wrong: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}

	private IProcessingInfo validate(File file, IPeakMSD peak) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addMessages(super.validate(file));
		processingInfo.addMessages(super.validate(peak));
		return processingInfo;
	}

	private IProcessingInfo validate(File file, IPeaks peaks) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addMessages(super.validate(file));
		processingInfo.addMessages(super.validate(peaks));
		return processingInfo;
	}
}
