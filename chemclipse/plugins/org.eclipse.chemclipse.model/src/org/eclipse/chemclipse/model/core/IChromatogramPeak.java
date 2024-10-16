/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexander Kerner - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

public interface IChromatogramPeak extends IPeak {

	/**
	 * Returns the signal to noise ratio of the peak.
	 *
	 * @return float
	 */
	float getSignalToNoiseRatio();
}
