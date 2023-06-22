/*******************************************************************************
 * Copyright (c) 2016, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.csd.converter.supplier.chemclipse.converter;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;
import org.eclipse.chemclipse.converter.core.IFileContentMatcher;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.IFormat;

public class FileContentMatcherCSD extends AbstractFileContentMatcher implements IFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean isValidFormat = false;
		try (ZipFile zipFile = new ZipFile(file)) {
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			exitloop:
			while(zipEntries.hasMoreElements()) {
				/*
				 * Check each file.
				 */
				ZipEntry zipEntry = zipEntries.nextElement();
				if(zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					/*
					 * Legacy:
					 * DIR_CHROMATOGRAM
					 * Versions <= 0.9.0.3
					 */
					if(name.equals(IFormat.DIR_CHROMATOGRAM_FID) || name.equals(IFormat.DIR_CHROMATOGRAM_CSD)) {
						isValidFormat = true;
						break exitloop;
					}
				}
			}
		} catch(Exception e) {
			// Print no exception.
		}
		return isValidFormat;
	}
}