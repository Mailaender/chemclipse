/*******************************************************************************
 * Copyright (c) 2013, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import java.text.DecimalFormat;

import org.eclipse.chemclipse.model.quantitation.IQuantitationPeak;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.PeakDataSupport;
import org.eclipse.swt.graphics.Image;

public class QuantPeaksLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String CONCENTRATION = "Concentration";
	public static final String CONCENTRATION_UNIT = "Unit";
	//
	private PeakDataSupport peakDataSupport = new PeakDataSupport();
	//
	public static final String[] TITLES = { //
			CONCENTRATION, //
			CONCENTRATION_UNIT, //
			"Type" //
	};
	//
	public static final int[] BOUNDS = { //
			100, //
			100, //
			100 //
	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			return getImage(element);
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		DecimalFormat decimalFormat = getDecimalFormat();
		String text = "";
		if(element instanceof IQuantitationPeak) {
			IQuantitationPeak entry = (IQuantitationPeak)element;
			switch(columnIndex) {
				case 0:
					text = decimalFormat.format(entry.getConcentration());
					break;
				case 1:
					text = entry.getConcentrationUnit();
					break;
				case 2:
					text = peakDataSupport.getType(entry.getReferencePeak());
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_QUANTIFY_SELECTED_PEAK, IApplicationImage.SIZE_16x16);
	}
}
