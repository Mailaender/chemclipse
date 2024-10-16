/*******************************************************************************
 * Copyright (c) 2015, 2018 Michael Chang.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Michael Chang - initial API and implementation
 * Philip Wenig - improvements
 *******************************************************************************/
package org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.converter.io.AbstractChromatogramWriter;
import org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.internal.io.ChromatogramWriter_1005;
import org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.internal.io.ChromatogramWriter_1006;
import org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.internal.io.ChromatogramWriter_1007;
import org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.internal.io.ChromatogramWriter_1100;
import org.eclipse.chemclipse.wsd.converter.supplier.chemclipse.internal.io.ChromatogramWriter_1300;
import org.eclipse.chemclipse.wsd.model.core.IChromatogramWSD;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.support.IFormat;
import org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.preferences.PreferenceSupplier;
import org.eclipse.core.runtime.IProgressMonitor;

public class ChromatogramWriterWSD extends AbstractChromatogramWriter implements IChromatogramWSDZipWriter {

	@Override
	public void writeChromatogram(File file, IChromatogramWSD chromatogram, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotWriteableException, IOException {

		IChromatogramWSDZipWriter chromatogramWriter = getChromatogramWriter(chromatogram, monitor);
		chromatogramWriter.writeChromatogram(file, chromatogram, monitor);
	}

	@Override
	public void writeChromatogram(ZipOutputStream zipOutputStream, String directoryPrefix, IChromatogramWSD chromatogram, IProgressMonitor monitor) throws IOException {

		IChromatogramWSDZipWriter chromatogramWriter = getChromatogramWriter(chromatogram, monitor);
		chromatogramWriter.writeChromatogram(zipOutputStream, "", chromatogram, monitor);
	}

	private IChromatogramWSDZipWriter getChromatogramWriter(IChromatogramWSD chromatogram, IProgressMonitor monitor) {

		String versionSave = PreferenceSupplier.getChromatogramVersionSave();
		IChromatogramWSDZipWriter chromatogramWriter;
		/*
		 * Check the requested version of the file to be exported.
		 * TODO Optimize
		 */
		if(versionSave.equals(IFormat.CHROMATOGRAM_VERSION_1005)) {
			chromatogramWriter = new ChromatogramWriter_1005();
		} else if(versionSave.equals(IFormat.CHROMATOGRAM_VERSION_1006)) {
			chromatogramWriter = new ChromatogramWriter_1006();
		} else if(versionSave.equals(IFormat.CHROMATOGRAM_VERSION_1007)) {
			chromatogramWriter = new ChromatogramWriter_1007();
		} else if(versionSave.equals(IFormat.CHROMATOGRAM_VERSION_1100)) {
			chromatogramWriter = new ChromatogramWriter_1100();
		} else {
			chromatogramWriter = new ChromatogramWriter_1300();
		}
		//
		return chromatogramWriter;
	}
}
