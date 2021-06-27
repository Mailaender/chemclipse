/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.model;

import java.io.File;

/**
 * @author Matthias Mailänder
 * 
 */
public class MassSpectrumInputEntry implements IMassSpectrumInputEntry {

	private String inputFile = "";

	/**
	 * Set the mass spectrum input file.
	 * 
	 * @param inputFile
	 */
	public MassSpectrumInputEntry(String inputFile) {
		if(inputFile != null) {
			this.inputFile = inputFile;
		}
	}

	@Override
	public String getInputFile() {

		return inputFile;
	}

	// TODO JUnit Windows/Linux/Mac
	@Override
	public String getName() {

		File file = new File(inputFile);
		return file.getName();
	}
}
