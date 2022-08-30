/*******************************************************************************
 * Copyright (c) 2016, 2022 Lablicate GmbH.
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

import org.eclipse.chemclipse.model.columns.IRetentionIndexEntry;
import org.eclipse.chemclipse.model.core.AbstractChromatogram;
import org.eclipse.chemclipse.model.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.text.ValueFormat;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class RetentionIndexLabelProvider extends LabelProvider implements ITableLabelProvider {

	public static final String RETENTION_TIME = "Retention Time (Minutes)";
	public static final String RETENTION_INDEX = "Retention Index";
	public static final String NAME = "Name";
	//
	public static final String[] TITLES = { //
			RETENTION_TIME, //
			RETENTION_INDEX, //
			NAME //
	};
	//
	public static final int[] BOUNDS = { //
			200, //
			150, //
			200 //
	};
	//
	private DecimalFormat decimalFormat = ValueFormat.getDecimalFormatEnglish("0.0000");

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

		String text = "";
		if(element instanceof IRetentionIndexEntry) {
			IRetentionIndexEntry retentionIndexEntry = (IRetentionIndexEntry)element;
			switch(columnIndex) {
				case 0:
					text = decimalFormat.format(retentionIndexEntry.getRetentionTime() / AbstractChromatogram.MINUTE_CORRELATION_FACTOR);
					break;
				case 1:
					if(PreferenceSupplier.showRetentionIndexWithoutDecimals()) {
						text = Integer.toString((int)retentionIndexEntry.getRetentionIndex());
					} else {
						text = decimalFormat.format(retentionIndexEntry.getRetentionIndex());
					}
					break;
				case 2:
					text = retentionIndexEntry.getName();
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}

	public Image getImage(Object element) {

		return ApplicationImageFactory.getInstance().getImage(IApplicationImage.IMAGE_PEAK, IApplicationImage.SIZE_16x16);
	}
}