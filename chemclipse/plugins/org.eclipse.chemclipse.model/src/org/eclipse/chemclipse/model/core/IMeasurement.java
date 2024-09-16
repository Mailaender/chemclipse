/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - add default file / dirty methods
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.io.File;
import java.io.Serializable;

public interface IMeasurement extends IMeasurementInfo, IMeasurementResultSupport, Serializable {

	/**
	 * 
	 * @return the file this measurement was loaded from or <code>null</code> if no file could be determined
	 */
	default File getFile() {

		return null;
	}

	default boolean isDirty() {

		return false;
	}

	default int getModCount() {

		return 0;
	}
}
