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
package org.eclipse.chemclipse.tsd.converter.supplier.mzml.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.eclipse.chemclipse.converter.exceptions.UnknownVersionException;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.tsd.converter.core.AbstractImportConverter;
import org.eclipse.chemclipse.tsd.converter.supplier.mzml.io.ChromatogramReaderVersion110;
import org.eclipse.chemclipse.tsd.model.core.IChromatogramTSD;
import org.eclipse.chemclipse.xxd.converter.supplier.mzml.io.XmlReader110;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramImportConverter extends AbstractImportConverter {

	@Override
	public IChromatogramTSD convert(InputStream inputStream, IProgressMonitor monitor) throws IOException {

		return getChromatogramReader(inputStream).read(inputStream, monitor);
	}

	@Override
	public IChromatogramOverview convertOverview(InputStream inputStream, IProgressMonitor monitor) throws IOException {

		return getChromatogramReader(inputStream).readOverview(inputStream, monitor);
	}

	private ChromatogramReaderVersion110 getChromatogramReader(InputStream inputStream) throws IOException {

		InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		char[] charBuffer = new char[500];
		int charsRead = reader.read(charBuffer, 0, 500);
		String header = new String(charBuffer, 0, charsRead);
		if(header.contains(XmlReader110.VERSION)) {
			return new ChromatogramReaderVersion110();
		} else {
			throw new UnknownVersionException();
		}
	}
}
