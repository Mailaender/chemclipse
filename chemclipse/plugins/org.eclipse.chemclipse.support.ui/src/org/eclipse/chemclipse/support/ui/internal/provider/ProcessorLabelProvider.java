/*******************************************************************************
 * Copyright (c) 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.internal.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.processing.DataCategory;
import org.eclipse.chemclipse.processing.supplier.IProcessSupplier;
import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.support.ui.processors.Processor;
import org.eclipse.chemclipse.support.ui.provider.AbstractChemClipseLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ProcessorLabelProvider extends AbstractChemClipseLabelProvider {

	public static final String NAME = "Name";
	public static final String DATA_TYPE = "Data Type";
	public static final String CATEGORY = "Category";
	public static final String DESCRIPTION = "Description";
	public static final String ID = "ID";
	//
	public static final String[] TITLES = { //
			NAME, //
			DATA_TYPE, //
			CATEGORY, //
			DESCRIPTION, //
			ID //
	};
	//
	public static final int[] BOUNDS = { //
			200, //
			100, //
			100, //
			100, //
			100 //
	};

	public static String getDataTypes(Set<DataCategory> dataTypes) {

		List<String> types = new ArrayList<>();
		for(DataCategory dataType : dataTypes) {
			types.add(dataType.name());
		}
		//
		Collections.sort(types);
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = types.iterator();
		while(iterator.hasNext()) {
			builder.append(iterator.next());
			if(iterator.hasNext()) {
				builder.append(", ");
			}
		}
		//
		return builder.toString();
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		if(columnIndex == 0) {
			if(element instanceof Processor) {
				Processor processor = (Processor)element;
				return ApplicationImageFactory.getInstance().getImage(processor.getImageFileName(), IApplicationImage.SIZE_16x16);
			}
		}
		//
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String text = "";
		if(element instanceof Processor) {
			Processor processor = (Processor)element;
			IProcessSupplier<?> processSupplier = processor.getProcessSupplier();
			//
			switch(columnIndex) {
				case 0:
					text = processSupplier.getName();
					break;
				case 1:
					text = getDataTypes(processSupplier.getSupportedDataTypes());
					break;
				case 2:
					text = processSupplier.getCategory();
					break;
				case 3:
					text = processSupplier.getDescription();
					break;
				case 4:
					text = processSupplier.getId();
					break;
				default:
					text = "n.v.";
			}
		}
		return text;
	}
}
