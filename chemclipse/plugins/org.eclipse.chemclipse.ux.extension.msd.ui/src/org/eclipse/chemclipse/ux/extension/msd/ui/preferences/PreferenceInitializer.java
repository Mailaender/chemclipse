/*******************************************************************************
 * Copyright (c) 2008, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.preferences;

import org.eclipse.chemclipse.ux.extension.msd.ui.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_OVERLAY_X_OFFSET, 0);
		store.setDefault(PreferenceConstants.P_OVERLAY_Y_OFFSET, 0);
		store.setDefault(PreferenceSupplier.P_SELECTED_ORGANIC_COMPOUND, PreferenceSupplier.P_ORGANIC_COMPOUND_HYDROCARBONS);
		store.setDefault(PreferenceSupplier.P_MAGNIFICATION_FACTOR, PreferenceSupplier.DEF_MAGNIFICATION_FACTOR);
		store.setDefault(PreferenceSupplier.P_USE_PROFILE_MASS_SPECTRUM_VIEW, PreferenceSupplier.DEF_USE_PROFILE_MASS_SPECTRUM_VIEW);
		//
		store.setDefault(PreferenceSupplier.P_PATH_OPEN_CHROMATOGRAMS, PreferenceSupplier.DEF_PATH_OPEN_CHROMATOGRAMS);
	}
}
