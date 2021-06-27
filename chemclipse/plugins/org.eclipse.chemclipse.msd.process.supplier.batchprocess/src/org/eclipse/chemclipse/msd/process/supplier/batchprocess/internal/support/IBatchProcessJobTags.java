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
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.internal.support;

/**
 * @author Matthias Mailänder
 * 
 */
public interface IBatchProcessJobTags {

	String UTF8 = "UTF-8";
	/*
	 * Start and Stop Element /
	 */
	String BATCH_PROCESS_JOB = "BatchProcessJob";
	/*
	 * Header
	 */
	String HEADER = "Header";
	String REPORT_FOLDER = "ReportFolder";
	String OVERRIDE_REPORT = "OverrideReport";
	/*
	 * Mass Spectrum Input Entries
	 */
	String MASSSPECTRUM_INPUT_ENTRIES = "InputEntries";
	String MASSSPECTRUM_INPUT_ENTRY = "InputEntry";
	/*
	 * Mass Spectrum Process Entries
	 */
	String MASSSPECTRUM_PROCESS_ENTRIES = "ProcessEntries";
	String MASSSPECTRUM_PROCESS_ENTRY = "ProcessEntry";
	String PROCESSOR_TYPE = "processorType";
	String PROCESSOR_ID = "processorId";
	/*
	 * Mass Spectrum Output Entries
	 */
	String MASSSPECTRUM_OUTPUT_ENTRIES = "OutputEntries";
	String MASSSPECTRUM_OUTPUT_ENTRY = "OutputEntry";
	String MASSSPECTRUM_CONVERTER_ID = "converterId";
	/*
	 * Mass Spectrum Report Entries
	 */
	String MASSSPECTRUM_REPORT_ENTRIES = "ReportEntries";
	String MASSSPECTRUM_REPORT_ENTRY = "ReportEntry";
	String MASSSPECTRUM_REPORT_SUPPLIER_ID = "reportSupplierId";
}
