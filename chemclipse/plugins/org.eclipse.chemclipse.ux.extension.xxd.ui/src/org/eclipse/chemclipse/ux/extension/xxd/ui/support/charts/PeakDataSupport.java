/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.preferences.PreferenceSupplierModel;
import org.eclipse.chemclipse.wsd.model.core.IPeakWSD;

public class PeakDataSupport {

	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0##");

	public String getPeakLabel(IPeak peak) {

		StringBuilder builder = new StringBuilder();
		if(peak != null) {
			//
			IPeakModel peakModel = peak.getPeakModel();
			IScan scan = peakModel.getPeakMaximum();
			//
			builder.append("Peak");
			builder.append(" | ");
			builder.append("Center RT: ");
			builder.append(decimalFormat.format(scan.getRetentionTime() / IChromatogram.MINUTE_CORRELATION_FACTOR));
			builder.append(" | ");
			builder.append("Center RI: ");
			if(PreferenceSupplierModel.showRetentionIndexWithoutDecimals()) {
				builder.append(Integer.toString((int)scan.getRetentionIndex()));
			} else {
				builder.append(decimalFormat.format(scan.getRetentionIndex()));
			}
			//
			builder.append(" | ");
			builder.append("Signal: ");
			builder.append((int)peak.getIntegratedArea());
		} else {
			builder.append("No peak has been selected yet.");
		}
		return builder.toString();
	}

	public String getType(IPeak peak) {

		if(peak instanceof IPeakCSD) {
			return "[CSD]";
		} else if(peak instanceof IPeakMSD) {
			return "[MSD]";
		} else if(peak instanceof IPeakWSD) {
			return "[WSD]";
		} else {
			return "";
		}
	}
}
