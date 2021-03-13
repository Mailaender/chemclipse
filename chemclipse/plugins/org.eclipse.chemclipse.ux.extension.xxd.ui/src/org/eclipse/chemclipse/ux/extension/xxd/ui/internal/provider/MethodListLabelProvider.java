/*******************************************************************************
 * Copyright (c) 2018, 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - don't use private ProcessTypeSupport, add tooltip message in case of error
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiFunction;

import org.eclipse.chemclipse.processing.methods.IProcessEntry;
import org.eclipse.chemclipse.processing.methods.IProcessMethod;
import org.eclipse.chemclipse.processing.methods.ProcessEntryContainer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.ProcessSupplierContext;
import org.eclipse.chemclipse.processing.supplier.ProcessorPreferences;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.graphics.Image;

public class MethodListLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String[] TITLES = {//
			"", //
			"Name", //
			"Description", //
			"Type", //
			"Settings", //
			"ID" //
	};
	//
	public static final int[] BOUNDS = {//
			50, //
			250, //
			250, //
			160, //
			300, //
			110 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			if(element instanceof IProcessEntry) {
				IStatus status = validate((IProcessEntry)element);
				if(status.matches(IStatus.ERROR)) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_STATUS_ERROR, IApplicationImage.SIZE_16x16);
				} else if(status.matches(IStatus.WARNING)) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_STATUS_WARN, IApplicationImage.SIZE_16x16);
				} else if(status.matches(IStatus.INFO)) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_STATUS_EMPTY, IApplicationImage.SIZE_16x16);
				} else if(status.isOK()) {
					return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_STATUS_OK, IApplicationImage.SIZE_16x16);
				}
			} else if(element instanceof IProcessMethod) {
				return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_METHOD, IApplicationImage.SIZE_16x16);
			} else if(element instanceof ProcessEntryContainer) {
				return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_FOLDER_OPENED, IApplicationImage.SIZE_16x16);
			}
		}
		return null;
	}

	@Override
	public String getToolTipText(Object element) {

		if(element instanceof IProcessEntry) {
			IStatus status = validate((IProcessEntry)element);
			if(!status.isOK()) {
				return status.getMessage();
			}
		}
		return super.getToolTipText(element);
	}

	private final ProcessSupplierContext processTypeSupport;
	private BiFunction<IProcessEntry, ProcessSupplierContext, ProcessorPreferences<?>> preferencesSupplier;

	public MethodListLabelProvider(ProcessSupplierContext processTypeSupport, BiFunction<IProcessEntry, ProcessSupplierContext, ProcessorPreferences<?>> preferencesSupplier) {

		this.processTypeSupport = processTypeSupport;
		this.preferencesSupplier = preferencesSupplier;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return "";
		}
		if(element instanceof IProcessEntry) {
			IProcessEntry entry = (IProcessEntry)element;
			ProcessSupplierContext supplierContext = IProcessEntry.getContext(entry, processTypeSupport);
			IProcessSupplier<?> supplier = supplierContext.getSupplier(entry.getProcessorId());
			switch(columnIndex) {
				case 1:
					return entry.getName();
				case 2:
					return entry.getDescription();
				case 3: {
					if(supplier != null) {
						return Arrays.toString(supplier.getSupportedDataTypes().toArray());
					}
				}
					break;
				case 4:
					if(supplier != null) {
						if(supplier.getSettingsParser().getInputValues().isEmpty() || preferencesSupplier == null) {
							return "not configurable";
						} else {
							ProcessorPreferences<?> preferences = preferencesSupplier.apply(entry, supplierContext);
							if(preferences.isUseSystemDefaults()) {
								return "defaults";
							} else {
								String text = preferences.getUserSettingsAsString();
								if(text.startsWith("{")) {
									text = text.substring(1);
								}
								if(text.endsWith("}")) {
									text = text.substring(0, text.length() - 1);
								}
								return text.replace("\"", "");
							}
						}
					}
					break;
				case 5:
					return entry.getProcessorId();
				default:
					break;
			}
		} else if(element instanceof ProcessEntryContainer) {
			ProcessEntryContainer method = (ProcessEntryContainer)element;
			switch(columnIndex) {
				case 1:
					return method.getName();
				case 2:
					return method.getDescription();
				default:
					return "";
			}
		}
		return "n/a";
	}

	@Override
	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PROCESS_CONTROL, IApplicationImage.SIZE_16x16);
	}

	private IStatus validate(IProcessEntry processEntry) {

		if(processEntry == null) {
			return ValidationStatus.error("Entry is null");
		}
		//
		ProcessorPreferences<?> preferences = processEntry.getPreferences(IProcessEntry.getContext(processEntry, processTypeSupport));
		if(preferences == null) {
			return ValidationStatus.error("Processor " + processEntry.getName() + " not avaiable");
		}
		//
		if(preferences.getSupplier().getSettingsClass() == null) {
			return ValidationStatus.warning("Processor " + processEntry.getName() + " has no settingsclass");
		}
		//
		if(preferences.isUseSystemDefaults()) {
			return ValidationStatus.info("Processor " + processEntry.getName() + " uses system default settings");
		} else {
			try {
				preferences.getUserSettings();
			} catch(IOException e) {
				return ValidationStatus.error("Loading settings for Processor " + processEntry.getName() + "failed", e);
			}
			return ValidationStatus.ok();
		}
	}
}
