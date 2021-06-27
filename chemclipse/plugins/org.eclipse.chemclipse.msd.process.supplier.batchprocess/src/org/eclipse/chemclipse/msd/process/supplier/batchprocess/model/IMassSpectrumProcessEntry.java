/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.model;

/**
 * @author Matthias Mailänder
 * 
 */
public interface IMassSpectrumProcessEntry {

	/**
	 * Returns the process category.
	 * 
	 * @return String
	 */
	String getProcessCategory();

	/**
	 * Sets the process category.
	 * 
	 * @param processCategory
	 */
	void setProcessCategory(String processCategory);

	/**
	 * Return the processor id.
	 * 
	 * @return String
	 */
	String getProcessorId();

	/**
	 * Set the processor id.
	 * 
	 * @param processorId
	 */
	void setProcessorId(String processorId);
}
