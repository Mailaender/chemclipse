/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.pcr.converter.core;

import java.io.File;
import java.util.List;

import org.eclipse.chemclipse.converter.core.Converter;
import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;
import org.eclipse.chemclipse.converter.exceptions.NoConverterAvailableException;
import org.eclipse.chemclipse.converter.scan.IScanConverterSupport;
import org.eclipse.chemclipse.converter.scan.ScanConverterSupport;
import org.eclipse.chemclipse.converter.scan.ScanSupplier;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.pcr.model.core.IPlate;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.chemclipse.processing.core.ProcessingInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

public class PlateConverterPCR {

	private static final Logger logger = Logger.getLogger(PlateConverterPCR.class);
	private static final String EXTENSION_POINT = "org.eclipse.chemclipse.pcr.converter.plateSupplier";

	/**
	 * This class has only static methods.
	 */
	private PlateConverterPCR() {
	}

	public static IProcessingInfo convert(final File file, final String converterId, final IProgressMonitor monitor) {

		IProcessingInfo processingInfo;
		/*
		 * Do not use a safe runnable here, because an object must
		 * be returned or null.
		 */
		IPlateImportConverter importConverter = getPlateImportConverter(converterId);
		if(importConverter != null) {
			processingInfo = importConverter.convert(file, monitor);
		} else {
			processingInfo = getProcessingError(file);
		}
		return processingInfo;
	}

	public static IProcessingInfo convert(final File file, final IProgressMonitor monitor) {

		return getPlate(file, false, monitor);
	}

	/**
	 * If no suitable parser was found, null will be returned.
	 * 
	 * @param file
	 * @param overview
	 * @param monitor
	 * @return {@link IProcessingInfo}
	 */
	private static IProcessingInfo getPlate(final File file, boolean overview, IProgressMonitor monitor) {

		IProcessingInfo processingInfo;
		IScanConverterSupport converterSupport = getScanConverterSupport();
		try {
			List<String> availableConverterIds = converterSupport.getAvailableConverterIds(file);
			for(String converterId : availableConverterIds) {
				/*
				 * Do not use a safe runnable here, because an object must
				 * be returned or null.
				 */
				IPlateImportConverter importConverter = getPlateImportConverter(converterId);
				if(importConverter != null) {
					processingInfo = importConverter.convert(file, monitor);
					if(!processingInfo.hasErrorMessages()) {
						Object object = processingInfo.getProcessingResult();
						if(object instanceof IPlate) {
							return processingInfo;
						}
					}
				}
			}
		} catch(NoConverterAvailableException e) {
			logger.info(e);
		}
		return getProcessingError(file);
	}

	public static IProcessingInfo convert(final File file, final IPlate plate, final String converterId, final IProgressMonitor monitor) {

		IProcessingInfo processingInfo;
		/*
		 * Do not use a safe runnable here, because an object must
		 * be returned or null.
		 */
		IPlateExportConverter exportConverter = getPlateExportConverter(converterId);
		if(exportConverter != null) {
			processingInfo = exportConverter.convert(file, plate, monitor);
		} else {
			processingInfo = getProcessingError(file);
		}
		return processingInfo;
	}

	private static IPlateImportConverter getPlateImportConverter(final String converterId) {

		IConfigurationElement element;
		element = getConfigurationElement(converterId);
		IPlateImportConverter instance = null;
		if(element != null) {
			try {
				instance = (IPlateImportConverter)element.createExecutableExtension(Converter.IMPORT_CONVERTER);
			} catch(CoreException e) {
				logger.warn(e);
			}
		}
		return instance;
	}

	private static IPlateExportConverter getPlateExportConverter(final String converterId) {

		IConfigurationElement element;
		element = getConfigurationElement(converterId);
		IPlateExportConverter instance = null;
		if(element != null) {
			try {
				instance = (IPlateExportConverter)element.createExecutableExtension(Converter.EXPORT_CONVERTER);
			} catch(CoreException e) {
				logger.warn(e);
			}
		}
		return instance;
	}

	private static IConfigurationElement getConfigurationElement(final String converterId) {

		if("".equals(converterId)) {
			return null;
		}
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : elements) {
			if(element.getAttribute(Converter.ID).equals(converterId)) {
				return element;
			}
		}
		return null;
	}

	public static IScanConverterSupport getScanConverterSupport() {

		ScanSupplier supplier;
		ScanConverterSupport converterSupport = new ScanConverterSupport();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = registry.getConfigurationElementsFor(EXTENSION_POINT);
		for(IConfigurationElement element : extensions) {
			/*
			 * Set the values to the ChromatogramSupplier instance first before
			 * validating them.<br/> Why? Because the return value of
			 * element.getAttribute(...) could be null. If the element is null
			 * and stored in a ChromatogramSupplier instance it will be at least
			 * "".
			 */
			supplier = new ScanSupplier();
			supplier.setFileExtension(element.getAttribute(Converter.FILE_EXTENSION));
			supplier.setFileName(element.getAttribute(Converter.FILE_NAME));
			supplier.setDirectoryExtension(element.getAttribute(Converter.DIRECTORY_EXTENSION));
			/*
			 * Check if these values contain not allowed characters. If yes than
			 * do not add the supplier to the supported converter list.
			 */
			if(Converter.isValid(supplier.getFileExtension()) && Converter.isValid(supplier.getFileName()) && Converter.isValid(supplier.getDirectoryExtension())) {
				supplier.setId(element.getAttribute(Converter.ID));
				supplier.setDescription(element.getAttribute(Converter.DESCRIPTION));
				supplier.setFilterName(element.getAttribute(Converter.FILTER_NAME));
				supplier.setExportable(Boolean.valueOf(element.getAttribute(Converter.IS_EXPORTABLE)));
				supplier.setImportable(Boolean.valueOf(element.getAttribute(Converter.IS_IMPORTABLE)));
				supplier.setMagicNumberMatcher(getMagicNumberMatcher(element));
				converterSupport.add(supplier);
			}
		}
		return converterSupport;
	}

	private static IMagicNumberMatcher getMagicNumberMatcher(IConfigurationElement element) {

		IMagicNumberMatcher magicNumberMatcher;
		try {
			magicNumberMatcher = (IMagicNumberMatcher)element.createExecutableExtension(Converter.IMPORT_MAGIC_NUMBER_MATCHER);
		} catch(Exception e) {
			magicNumberMatcher = null;
		}
		return magicNumberMatcher;
	}

	private static IProcessingInfo getProcessingError(File file) {

		IProcessingInfo processingInfo = new ProcessingInfo();
		processingInfo.addErrorMessage("Plate Converter", "No suitable converter was found for: " + file);
		return processingInfo;
	}
}
