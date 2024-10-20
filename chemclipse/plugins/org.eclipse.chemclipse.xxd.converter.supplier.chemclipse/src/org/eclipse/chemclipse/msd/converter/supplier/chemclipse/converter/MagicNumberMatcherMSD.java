/*******************************************************************************
 * Copyright (c) 2016, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.chemclipse.converter;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.chemclipse.converter.core.AbstractMagicNumberMatcher;
import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.IFormat;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.SpecificationValidator;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.preferences.PreferenceSupplier;

public class MagicNumberMatcherMSD extends AbstractMagicNumberMatcher implements IMagicNumberMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean isForceLoadAlternateDetector = PreferenceSupplier.isForceLoadAlternateDetector();
		boolean isValidFormat = false;
		try {
			file = SpecificationValidator.validateSpecification(file);
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			exitloop:
			while(zipEntries.hasMoreElements()) {
				/*
				 * Check each file.
				 */
				ZipEntry zipEntry = zipEntries.nextElement();
				if(zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					if(isForceLoadAlternateDetector) {
						/*
						 * Legacy:
						 * DIR_CHROMATOGRAM
						 * Versions <= 0.9.0.3
						 */
						if(name.equals(IFormat.DIR_CHROMATOGRAM_FID) || name.equals(IFormat.DIR_CHROMATOGRAM_CSD) || name.equals(IFormat.DIR_CHROMATOGRAM_MSD) || name.equals(IFormat.DIR_CHROMATOGRAM_WSD) || name.equals(IFormat.DIR_CHROMATOGRAM)) {
							isValidFormat = true;
							break exitloop;
						}
					} else {
						if(name.equals(IFormat.DIR_CHROMATOGRAM_MSD) || name.equals(IFormat.DIR_CHROMATOGRAM)) {
							isValidFormat = true;
							break exitloop;
						}
					}
				}
			}
			zipFile.close();
		} catch(Exception e) {
			// Print no exception.
		}
		return isValidFormat;
	}
}
