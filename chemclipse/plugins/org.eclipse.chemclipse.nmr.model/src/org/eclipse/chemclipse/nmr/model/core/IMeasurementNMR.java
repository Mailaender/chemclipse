/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Christoph Läubrich - cleanup API
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.model.core;

import org.eclipse.chemclipse.model.core.IMeasurement;

public interface IMeasurementNMR extends IMeasurement {

	Double getProcessingParameters(String name);

	IScanNMR getScanMNR();

	IScanFID getScanFID();
}
