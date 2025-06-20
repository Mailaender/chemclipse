/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.converter.supplier.mzml.core;

import org.eclipse.chemclipse.converter.core.IFileContentMatcher;
import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;
import org.eclipse.chemclipse.model.settings.IProcessSettings;
import org.eclipse.chemclipse.tsd.converter.core.IExportConverterTSD;
import org.eclipse.chemclipse.tsd.converter.core.IImportConverterTSD;
import org.eclipse.chemclipse.tsd.converter.service.IConverterServiceTSD;
import org.eclipse.chemclipse.tsd.converter.supplier.mzml.converter.ChromatogramImportConverter;
import org.eclipse.chemclipse.tsd.converter.supplier.mzml.converter.FileContentMatcher;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.converter.MagicNumberMatcher;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(service = {IConverterServiceTSD.class}, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class ChromatogramConverter implements IConverterServiceTSD {

	@Override
	public String getId() {

		return "org.eclipse.chemclipse.tsd.converter.supplier.mzml";
	}

	@Override
	public String getDescription() {

		return "LC-IMS qTOF Import Converter for mzML files.";
	}

	@Override
	public String getFilterName() {

		return "LC-IMS qTOF Import Converter (*.mzML)";
	}

	@Override
	public String getFileExtension() {

		return ".mzML";
	}

	@Override
	public String getFileName() {

		return "";
	}

	@Override
	public String getDirectoryExtension() {

		return "";
	}

	@Override
	public IImportConverterTSD getImportConverter() {

		return new ChromatogramImportConverter();
	}

	@Override
	public IExportConverterTSD getExportConverter() {

		return null;
	}

	@Override
	public IMagicNumberMatcher getMagicNumberMatcher() {

		return new MagicNumberMatcher();
	}

	@Override
	public IFileContentMatcher getFileContentMatcher() {

		return new FileContentMatcher();
	}

	@Override
	public IProcessSettings getProcessSettings() {

		return null;
	}
}