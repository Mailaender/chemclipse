/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.core.massspectrum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.msd.filter.exceptions.NoMassSpectrumFilterSupplierAvailableException;
import org.eclipse.chemclipse.chromatogram.msd.filter.settings.IMassSpectrumFilterSettings;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.supplier.ScanProcessSupplier;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.processing.core.ICategories;
import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.processing.supplier.IProcessTypeSupplier;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = IProcessTypeSupplier.class)
public class MassSpectrumFilterProcessSupplier implements IProcessTypeSupplier {

	@Override
	public String getCategory() {

		return ICategories.MASS_SPECTRUM_FILTER;
	}

	@Override
	public Collection<IProcessSupplier<?>> getProcessorSuppliers() {

		try {
			IMassSpectrumFilterSupport support = MassSpectrumFilter.getMassSpectrumFilterSupport();
			List<IProcessSupplier<?>> list = new ArrayList<>();
			for(String processorId : support.getAvailableFilterIds()) {
				IMassSpectrumFilterSupplier supplier = support.getFilterSupplier(processorId);
				list.add(new MassSpectrumFilterProcessorSupplier(supplier, this));
			}
			return list;
		} catch(NoMassSpectrumFilterSupplierAvailableException e) {
			return Collections.emptyList();
		}
	}

	private static final class MassSpectrumFilterProcessorSupplier extends ScanProcessSupplier<IMassSpectrumFilterSettings> {

		@SuppressWarnings("unchecked")
		public MassSpectrumFilterProcessorSupplier(IMassSpectrumFilterSupplier supplier, IProcessTypeSupplier parent) {

			super(supplier.getId(), supplier.getFilterName(), supplier.getDescription(), (Class<IMassSpectrumFilterSettings>)supplier.getSettingsClass(), parent, DataType.MALDI);
			getLiteratureReferences().addAll(supplier.getLiteratureReferences());
		}

		@Override
		public IScan apply(IScan scan, IMassSpectrumFilterSettings processSettings, IMessageConsumer messageConsumer, IProgressMonitor monitor) {

			if(scan instanceof IScanMSD scanMSD) {
				if(processSettings instanceof IMassSpectrumFilterSettings) {
					messageConsumer.addMessages(MassSpectrumFilter.applyFilter(scanMSD, processSettings, getId(), monitor));
				}
			} else {
				messageConsumer.addWarnMessage(getName(), "Only MSD scans supported. Processing skipped");
			}
			return scan;
		}
	}
}
