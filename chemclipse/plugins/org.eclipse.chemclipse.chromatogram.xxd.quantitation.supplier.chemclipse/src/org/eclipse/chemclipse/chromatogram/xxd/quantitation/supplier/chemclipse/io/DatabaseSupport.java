/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.io;

import java.io.File;

import org.eclipse.chemclipse.chromatogram.xxd.quantitation.supplier.chemclipse.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.converter.quantitation.QuantDBConverter;
import org.eclipse.chemclipse.model.quantitation.IQuantitationDatabase;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.NullProgressMonitor;

public class DatabaseSupport {

	/**
	 * Tries to load the quantitation database from the given file.
	 * Result could be null if the operation failed.
	 *
	 * @param file
	 * @return
	 */
	public IQuantitationDatabase load() {

		IQuantitationDatabase quantitationDatabase = null;
		File file = new File(PreferenceSupplier.getSelectedQuantitationDatabase());
		//
		if(file.exists()) {
			IProcessingInfo<IQuantitationDatabase> processingInfo = QuantDBConverter.convert(file, new NullProgressMonitor());
			quantitationDatabase = processingInfo.getProcessingResult();
		}
		//
		return quantitationDatabase;
	}

	public void save(IQuantitationDatabase quantitationDatabase) {

		if(quantitationDatabase != null) {
			File file = quantitationDatabase.getFile();
			String converterId = quantitationDatabase.getConverterId();
			if(file != null && file.exists() && !"".equals(converterId)) {
				QuantDBConverter.convert(file, quantitationDatabase, converterId, new NullProgressMonitor());
			}
		}
	}
}
