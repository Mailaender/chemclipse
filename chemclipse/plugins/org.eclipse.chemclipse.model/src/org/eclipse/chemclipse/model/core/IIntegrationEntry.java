/*******************************************************************************
 * Copyright (c) 2013, 2023 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.io.Serializable;

public interface IIntegrationEntry extends Serializable {

	double getSignal();

	double getIntegratedArea();

	/**
	 * This information is transient.
	 * 
	 * @return {@link IntegrationType}
	 */
	IntegrationType getIntegrationType();

	/**
	 * This information is transient.
	 * 
	 * @param integrationType
	 */
	void setIntegrationType(IntegrationType integrationType);
}