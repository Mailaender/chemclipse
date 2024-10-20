/*******************************************************************************
 * Copyright (c) 2017, 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.types;

import org.eclipse.chemclipse.processing.DataCategory;

public enum DataType {
	AUTO_DETECT, // Auto-Detect
	MSD_NOMINAL, // Quadrupole, Ion Trap
	MSD_TANDEM, // MS/MS
	MSD_HIGHRES, // Orbitrap, TOF
	MSD, // mass selective data
	CSD, // current selective data
	WSD, // wavelength selective data
	XIR, // Infrared detectors, FTIR, NIR, MIR
	NMR, // Nuclear magnetic resonance
	CAL, // Retention Index Calibration
	PCR, // Polymerase Chain Reaction
	SEQ, // Sequences
	MTH, // Methods
	QDB // Quantitation Databases
	;

	public static DataType fromDataCategory(DataCategory category) {

		switch(category) {
			case MSD:
				return DataType.MSD;
			case WSD:
				return DataType.WSD;
			case CSD:
				return DataType.CSD;
			case FID:
			case NMR:
				return DataType.NMR;
			default:
				return DataType.AUTO_DETECT;
		}
	}

	public DataCategory toDataCategory() {

		switch(this) {
			case MSD:
				return DataCategory.MSD;
			case WSD:
				return DataCategory.WSD;
			case CSD:
				return DataCategory.CSD;
			case NMR:
				return DataCategory.NMR;
			default:
				return DataCategory.AUTO_DETECT;
		}
	}
}
