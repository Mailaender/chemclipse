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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.model.IChromatogramInputEntry;
import org.eclipse.chemclipse.converter.model.IChromatogramOutputEntry;
import org.eclipse.chemclipse.chromatogram.msd.process.model.IChromatogramProcessEntry;
import org.eclipse.chemclipse.chromatogram.xxd.report.model.IChromatogramReportSupplierEntry;

/**
 * @author Matthias Mailänder
 * 
 */
public class BatchProcessJob implements IBatchProcessJob {

	private List<IMassSpectrumInputEntry> massSpectrumInputEntries;
	private List<IMassSpectrumProcessEntry> massSpectrumProcessEntries;
	private List<IMassSpectrumOutputEntry> massSpectrumOutputEntries;
	private List<IMassSpectrumReportSupplierEntry> massSpectrumReportEntries;

	/**
	 * Creates a new batch process job.
	 */
	public BatchProcessJob() {
		/*
		 * Why are ArrayLists used here?
		 * The entries shall be processed in the order the user has chosen them.
		 */
		massSpectrumInputEntries = new ArrayList<IMassSpectrumInputEntry>();
		massSpectrumProcessEntries = new ArrayList<IMassSpectrumProcessEntry>();
		massSpectrumOutputEntries = new ArrayList<IMassSpectrumOutputEntry>();
		massSpectrumReportEntries = new ArrayList<IMassSpectrumReportSupplierEntry>();
	}

	@Override
	public List<IMassSpectrumInputEntry> getMassSpectrumInputEntries() {

		return massSpectrumInputEntries;
	}

	@Override
	public List<IMassSpectrumProcessEntry> getMassSpectrumProcessEntries() {

		return massSpectrumProcessEntries;
	}

	@Override
	public List<IMassSpectrumOutputEntry> getMassSpectrumOutputEntries() {

		return massSpectrumOutputEntries;
	}

	@Override
	public List<IMassSpectrumReportSupplierEntry> getMassSpectrumReportEntries() {

		return massSpectrumReportEntries;
	}
}
