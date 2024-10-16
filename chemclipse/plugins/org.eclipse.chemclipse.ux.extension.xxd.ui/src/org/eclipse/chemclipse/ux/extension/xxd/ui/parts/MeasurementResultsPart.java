/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IMeasurementResult;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.AbstractDataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.IDataUpdateSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.support.charts.ChromatogramDataSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.swt.ExtendedMeasurementResultUI;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;

public class MeasurementResultsPart extends AbstractDataUpdateSupport implements IDataUpdateSupport {

	private ExtendedMeasurementResultUI extendedMeasurementResultUI;

	@Inject
	public MeasurementResultsPart(Composite parent, MPart part) {
		super(part);
		extendedMeasurementResultUI = new ExtendedMeasurementResultUI(parent);
	}

	@Focus
	public void setFocus() {

		updateObjects(getObjects(), getTopic());
	}

	@Override
	public void registerEvents() {

		registerEvent(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UPDATE_SELECTION, IChemClipseEvents.PROPERTY_CHROMATOGRAM_SELECTION_XXD);
		registerEvent(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UNLOAD_SELECTION, IChemClipseEvents.PROPERTY_CHROMATOGRAM_SELECTION_XXD);
	}

	@Override
	public void updateObjects(List<Object> objects, String topic) {

		Collection<IMeasurementResult> results = Collections.emptyList();
		String infoLabel = "";
		if(!isUnloadEvent(topic)) {
			if(objects.size() == 1) {
				Object object = objects.get(0);
				if(object instanceof IChromatogramSelection<?, ?>) {
					IChromatogramSelection<?, ?> selection = (IChromatogramSelection<?, ?>)object;
					IChromatogram<?> chromatogram = selection.getChromatogram();
					results = new ArrayList<>(chromatogram.getMeasurementResults());
					infoLabel = ChromatogramDataSupport.getChromatogramLabel(chromatogram);
				}
			}
		}
		extendedMeasurementResultUI.update(results, infoLabel);
	}

	private boolean isUnloadEvent(String topic) {

		if(topic.equals(IChemClipseEvents.TOPIC_CHROMATOGRAM_XXD_UNLOAD_SELECTION)) {
			return true;
		}
		return false;
	}
}
