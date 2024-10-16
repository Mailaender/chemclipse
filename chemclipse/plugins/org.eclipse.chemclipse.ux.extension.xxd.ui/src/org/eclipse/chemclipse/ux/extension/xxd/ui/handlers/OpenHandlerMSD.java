/*******************************************************************************
 * Copyright (c) 2013, 2019 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - set size of wizard
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.handlers;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;

public class OpenHandlerMSD extends AbstractOpenHandler {

	@Override
	protected DataType getDataType() {

		return DataType.MSD;
	}

	@Override
	protected String getPreferenceKey() {

		return PreferenceSupplier.P_FILTER_PATH_CHROMATOGRAM_MSD;
	}
}
