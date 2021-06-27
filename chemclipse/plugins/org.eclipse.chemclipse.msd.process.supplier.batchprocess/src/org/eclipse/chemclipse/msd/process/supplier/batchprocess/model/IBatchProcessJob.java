/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.model;

import java.util.List;

/**
 * @author Matthias Mailänder
 * 
 */
public interface IBatchProcessJob {

	/**
	 * Returns the list of mass spectra input entries.
	 * 
	 * @return List<IMassSpectrumInputEntry>
	 */
	List<IMassSpectrumInputEntry> getMassSpectrumInputEntries();

	/**
	 * Returns the list of all entries to process each mass spectrum.
	 * 
	 * @return List<IMassSpectrumProcessEntry>
	 */
	List<IMassSpectrumProcessEntry> getMassSpectrumProcessEntries();

	/**
	 * Returns the mass spectrum output entries.
	 * 
	 * @return List<IMassSpectrumOutputEntry>
	 */
	List<IMassSpectrumOutputEntry> getMassSpectrumOutputEntries();

	/**
	 * Returns the mass spectrum report entries.
	 * 
	 * @return List<IMassSpectrumReportSupplierEntry>
	 */
	List<IMassSpectrumReportSupplierEntry> getMassSpectrumReportEntries();
}
