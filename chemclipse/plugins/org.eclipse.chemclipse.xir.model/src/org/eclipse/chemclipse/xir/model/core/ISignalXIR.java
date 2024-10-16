/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xir.model.core;

import java.io.Serializable;

import org.eclipse.chemclipse.model.core.ISignal;

public interface ISignalXIR extends ISignal, Serializable {

	double getWavelength();

	void setWavelength(double wavelength);

	double getIntensity();

	void setIntensity(double intensity);
}