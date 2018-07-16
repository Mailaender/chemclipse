/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.core;

import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.result.ResultStatus;
import org.eclipse.chemclipse.chromatogram.filter.settings.IChromatogramFilterSettings;
import org.eclipse.chemclipse.chromatogram.msd.filter.core.chromatogram.AbstractChromatogramFilterMSD;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.exceptions.FilterException;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.internal.core.support.Denoising;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.result.DenoisingFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.result.IDenoisingFilterResult;
import org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising.settings.ISupplierFilterSettings;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.implementation.MeasurementResult;
import org.eclipse.chemclipse.msd.model.core.ICombinedMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.msd.model.core.support.IMarkedIons;
import org.eclipse.chemclipse.msd.model.core.support.MarkedIons;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.chemclipse.support.util.IonSettingUtil;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramFilter extends AbstractChromatogramFilterMSD {

	private IMarkedIons ionsToRemove;
	private IMarkedIons ionsToPreserve;
	private boolean adjustThresholdTransitions;
	private int numberOfUsedIonsForCoefficient;
	private int segmentWidth;

	@Override
	public IProcessingInfo applyFilter(IChromatogramSelectionMSD chromatogramSelection, IChromatogramFilterSettings chromatogramFilterSettings, IProgressMonitor monitor) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addMessages(validate(chromatogramSelection, chromatogramFilterSettings));
		if(processingInfo.hasErrorMessages()) {
			return processingInfo;
		}
		/*
		 * Try to denoise the chromatogram selection.
		 */
		setFilterSettings(chromatogramFilterSettings);
		IDenoisingFilterResult chromatogramFilterResult;
		try {
			List<ICombinedMassSpectrum> noiseMassSpectra = Denoising.applyDenoisingFilter(chromatogramSelection, ionsToRemove, ionsToPreserve, adjustThresholdTransitions, numberOfUsedIonsForCoefficient, segmentWidth, monitor);
			IMeasurementResult measurementResult = new MeasurementResult("MS Denoising Filter", "org.eclipse.chemclipse.chromatogram.msd.filter.supplier.denoising", "This list contains the calculated noise mass spectra.", noiseMassSpectra);
			chromatogramSelection.getChromatogram().addMeasurementResult(measurementResult);
			chromatogramFilterResult = new DenoisingFilterResult(ResultStatus.OK, "The chromatogram selection has been denoised successfully.", noiseMassSpectra);
		} catch(FilterException e) {
			chromatogramFilterResult = new DenoisingFilterResult(ResultStatus.EXCEPTION, e.getMessage());
		}
		processingInfo.setProcessingResult(chromatogramFilterResult);
		return processingInfo;
	}

	// TODO JUnit
	@Override
	public IProcessingInfo applyFilter(IChromatogramSelectionMSD chromatogramSelection, IProgressMonitor monitor) {

		IChromatogramFilterSettings chromatogramFilterSettings = PreferenceSupplier.getChromatogramFilterSettings();
		return applyFilter(chromatogramSelection, chromatogramFilterSettings, monitor);
	}

	private void setFilterSettings(IChromatogramFilterSettings chromatogramFilterSettings) {

		/*
		 * Get the excluded ions instance.
		 */
		ISupplierFilterSettings settings;
		if(chromatogramFilterSettings instanceof ISupplierFilterSettings) {
			settings = (ISupplierFilterSettings)chromatogramFilterSettings;
		} else {
			settings = PreferenceSupplier.getChromatogramFilterSettings();
		}
		IonSettingUtil settingIon = new IonSettingUtil();
		ionsToRemove = new MarkedIons(settingIon.extractIons(settingIon.deserialize(settings.getIonsToRemove())));
		ionsToPreserve = new MarkedIons(settingIon.extractIons(settingIon.deserialize(settings.getIonsToPreserve())));
		adjustThresholdTransitions = settings.isAdjustThresholdTransitions();
		numberOfUsedIonsForCoefficient = settings.getNumberOfUsedIonsForCoefficient();
		segmentWidth = settings.getSegmentWidth();
	}
}
