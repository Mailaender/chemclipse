/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - improvements
 *******************************************************************************/
package org.eclipse.chemclipse.model.statistics;

public interface ISampleData<T> {

	double getData();

	double getModifiedData();

	default boolean isEmpty() {

		return Double.isNaN(getData());
	}

	void setModifiedData(double normalizedData);

	/**
	 * This is the data source.
	 * 
	 * @return T
	 */
	T getDataObject();
}