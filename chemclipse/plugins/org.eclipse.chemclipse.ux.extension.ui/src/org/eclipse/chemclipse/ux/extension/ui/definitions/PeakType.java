/*******************************************************************************
 * Copyright (c) 2016, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.definitions;

import org.eclipse.chemclipse.csd.model.core.IPeakCSD;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.msd.model.core.IPeakMSD;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.chemclipse.ux.extension.ui.Activator;
import org.eclipse.chemclipse.wsd.model.core.IPeakWSD;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PeakType implements EventHandler {

	public static final String PEAK_TYPE = "org.eclipse.chemclipse.ux.extension.ui.definitions.peakType";
	public static final String PEAK_SELECTION = "org.eclipse.chemclipse.ux.extension.ui.peakSelection";
	//
	public static final String PEAK_TYPE_MSD = "PEAK_TYPE_MSD";
	public static final String PEAK_TYPE_CSD = "PEAK_TYPE_CSD";
	public static final String PEAK_TYPE_WSD = "PEAK_TYPE_WSD";
	public static final String PEAK_TYPE_XXD = "PEAK_TYPE_XXD";
	public static final String PEAK_TYPE_NONE = "PEAK_TYPE_NONE";

	@Override
	public void handleEvent(Event event) {

		String topic = event.getTopic();
		Object property = event.getProperty(IChemClipseEvents.EVENT_BROKER_DATA);
		/*
		 * Get the peak type.
		 */
		IPeak peakSelection = null;
		String peakType = PEAK_TYPE_NONE;
		//
		if(topic.equals(IChemClipseEvents.TOPIC_PEAK_XXD_UPDATE_SELECTION)) {
			if(property instanceof IPeakMSD peakMSD) {
				peakSelection = peakMSD;
				peakType = PEAK_TYPE_MSD;
			} else if(property instanceof IPeakCSD peakCSD) {
				peakSelection = peakCSD;
				peakType = PEAK_TYPE_CSD;
			} else if(property instanceof IPeakWSD peakWSD) {
				peakSelection = peakWSD;
				peakType = PEAK_TYPE_WSD;
			} else {
				peakSelection = null;
				peakType = PEAK_TYPE_NONE;
			}
		} else if(topic.equals(IChemClipseEvents.TOPIC_EDITOR_CHROMATOGRAM_CLOSE)) {
			peakSelection = null;
			peakType = PEAK_TYPE_NONE;
		}
		/*
		 * Set the type, see:
		 * org.eclipse.core.expressions.definitions
		 * isPeakTypeMSD
		 */
		IEclipseContext eclipseContext = Activator.getDefault().getEclipseContext();
		if(eclipseContext != null) {
			eclipseContext.set(PEAK_SELECTION, peakSelection);
			eclipseContext.set(PEAK_TYPE, peakType);
		}
	}

	/**
	 * Get the current peak selection.
	 * 
	 * @return IPeak
	 */
	public static IPeak getSelectedPeak() {

		IEclipseContext eclipseContext = Activator.getDefault().getEclipseContext();
		Object object = eclipseContext.get(PEAK_SELECTION);
		//
		IPeak selectedPeak = null;
		if(object instanceof IPeak peak) {
			selectedPeak = peak;
		}
		//
		return selectedPeak;
	}

	/**
	 * Get the current peak selection.
	 * 
	 * @return IPeakMSD
	 */
	public static IPeakMSD getSelectedPeakMSD() {

		IEclipseContext eclipseContext = Activator.getDefault().getEclipseContext();
		Object object = eclipseContext.get(PEAK_SELECTION);
		//
		IPeakMSD peakSelectedMSD = null;
		if(object instanceof IPeakMSD peakMSD) {
			peakSelectedMSD = peakMSD;
		}
		//
		return peakSelectedMSD;
	}

	/**
	 * Get the current peak selection.
	 * 
	 * @return IPeakCSD
	 */
	public static IPeakCSD getSelectedPeakCSD() {

		IEclipseContext eclipseContext = Activator.getDefault().getEclipseContext();
		Object object = eclipseContext.get(PEAK_SELECTION);
		//
		IPeakCSD selectedPeakCSD = null;
		if(object instanceof IPeakCSD peakCSD) {
			selectedPeakCSD = peakCSD;
		}
		//
		return selectedPeakCSD;
	}

	/**
	 * Get the current peak selection.
	 * 
	 * @return IPeakWSD
	 */
	public static IPeakWSD getSelectedPeakWSD() {

		IEclipseContext eclipseContext = Activator.getDefault().getEclipseContext();
		Object object = eclipseContext.get(PEAK_SELECTION);
		//
		IPeakWSD selectedPeakWSD = null;
		if(object instanceof IPeakWSD peakWSD) {
			selectedPeakWSD = peakWSD;
		}
		//
		return selectedPeakWSD;
	}
}
