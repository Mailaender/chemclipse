/*******************************************************************************
 * Copyright (c) 2013, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 * Christoph Läubrich - set size of wizard
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.handlers;

import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;

public class OpenHandlerWSD extends AbstractOpenHandler {

	@Override
	protected DataType getDataType() {

		return DataType.WSD;
	}

	@Override
	protected String getPreferenceKey() {

		return PreferenceSupplier.P_FILTER_PATH_CHROMATOGRAM_WSD;
	}
}
