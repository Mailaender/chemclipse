/*******************************************************************************
 * Copyright (c) 2022, 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.eclipse.chemclipse.model.core.IMassSpectrumPeak;

/**
 * An interface for single MALDI-TOF MS spectra which contain additional metadata.
 * 
 * @author Matthias Mailänder
 */
public interface IStandaloneMassSpectrum extends IRegularMassSpectrum {

	/**
	 * Returns the file, see setFile().
	 * May return null.
	 * 
	 * @return File
	 */
	File getFile();

	/**
	 * Set the file of the mass spectrum, e.g. if it is a MALDI-MS record.
	 * 
	 * @param file
	 */
	void setFile(File file);

	/**
	 * Returns the name of the mass spectrum, if it's e.g. a MALDI-MS record.
	 * 
	 * @return String
	 */
	String getName();

	String getSampleName();

	void setSampleName(String name);

	String getDescription();

	void setDescription(String description);

	String getOperator();

	void setOperator(String operator);

	Date getDate();

	void setDate(Date date);

	String getInstrument();

	void setInstrument(String instrument);

	List<IMassSpectrumPeak> getPeaks();
}
