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
package org.eclipse.chemclipse.msd.baseline.correction;

import org.eclipse.chemclipse.chromatogram.xxd.baseline.detector.core.BaselineDetector;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.IProcessingMessage;
import org.eclipse.chemclipse.processing.core.MessageType;
import org.eclipse.chemclipse.processing.core.ProcessingMessage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

/**
 * Based on the baseline detection model.
 * 
 * @author Matthias Mailänder
 */
public class BaselineCorrection {

	private static final Logger logger = Logger.getLogger(BaselineDetector.class);
	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.msd.baseline.correction.baselineCorrectionSupplier";
	/*
	 * These are the attributes of the extension point elements.
	 */
	private static final String ID = "id";
	private static final String DESCRIPTION = "description";
	private static final String DETECTOR_NAME = "detectorName";
	private static final String BASELINE_CORRECTION = "baselineCorrection";
	/*
	 * Processing Info
	 */
	private static final String NO_CORRECTION_AVAILABLE = "There is no baseline corrrection available.";

	/**
	 * This class has only static methods.
	 */
	private BaselineCorrection() {
	}

	/**
	 * Use this method to call an extension point which sets a baseline to the
	 * given mass spectrum.
	 * 
	 * @param mass
	 *            spectrum
	 * @param baselineDetectorSettings
	 * @param detectorId
	 * @param monitor
	 * @return IBaselineDetectorProcessingInfo
	 */
	public static IBaselineCorrectionProcessingInfo setBaseline(IScanMSD massSpectrum, IBaselineCorrectionSettings baselineDetectorSettings, final String detectorId, IProgressMonitor monitor) {

		IBaselineCorrectionProcessingInfo processingInfo;
		IBaselineCorrection detector = getBaselineCorrection(detectorId);
		if(detector != null) {
			processingInfo = detector.setBaseline(massSpectrum, baselineDetectorSettings, monitor);
		} else {
			processingInfo = getNoCorrectionAvailableProcessingInfo();
		}
		return processingInfo;
	}

	/**
	 * See also other set baseline method. This method needs no settings.
	 * 
	 * @param chromatogramSelection
	 * @param detectorId
	 * @param monitor
	 * @return IBaselineDetectorProcessingInfo
	 */
	public static IBaselineCorrectionProcessingInfo setBaseline(IScanMSD massSpectrum, final String detectorId, IProgressMonitor monitor) {

		IBaselineCorrectionProcessingInfo processingInfo;
		IBaselineCorrection correction = getBaselineCorrection(detectorId);
		if(correction != null) {
			processingInfo = correction.setBaseline(massSpectrum, monitor);
		} else {
			processingInfo = getNoCorrectionAvailableProcessingInfo();
		}
		return processingInfo;
	}

	public static IBaselineCorrectionSupport getBaselineCorrectionSupport() {

		BaselineCorrectionSupplier supplier;
		BaselineCorrectionSupport baselineCorrectionSupport = new BaselineCorrectionSupport();
		/*
		 * Search in the extension registry and fill the comparison support
		 * object with supplier information.
		 */
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : extensions) {
			supplier = new BaselineCorrectionSupplier();
			supplier.setId(element.getAttribute(ID));
			supplier.setDescription(element.getAttribute(DESCRIPTION));
			supplier.setDetectorName(element.getAttribute(DETECTOR_NAME));
			baselineCorrectionSupport.add((IBaselineCorrectionSupplier)supplier);
		}
		return baselineCorrectionSupport;
	}

	// --------------------------------------------private methods
	private static IBaselineCorrection getBaselineCorrection(final String correctionId) {

		IConfigurationElement element;
		element = getConfigurationElement(correctionId);
		IBaselineCorrection instance = null;
		if(element != null) {
			try {
				instance = (IBaselineCorrection)element.createExecutableExtension(BASELINE_CORRECTION);
			} catch(CoreException e) {
				logger.warn(e);
			}
		}
		return instance;
	}

	/**
	 * Returns an IBaselineDetector instance or null if none is available.
	 * 
	 * @param correctionId
	 * @return IConfigurationElement
	 */
	private static IConfigurationElement getConfigurationElement(final String correctionId) {

		if("".equals(correctionId)) {
			return null;
		}
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : elements) {
			if(element.getAttribute(ID).equals(correctionId)) {
				return element;
			}
		}
		return null;
	}

	// --------------------------------------------private methods
	private static IBaselineCorrectionProcessingInfo getNoCorrectionAvailableProcessingInfo() {

		IBaselineCorrectionProcessingInfo processingInfo = new BaselineCorrectionProcessingInfo();
		IProcessingMessage processingMessage = new ProcessingMessage(MessageType.ERROR, "Baseline Detection", NO_CORRECTION_AVAILABLE);
		processingInfo.addMessage(processingMessage);
		return processingInfo;
	}
}
