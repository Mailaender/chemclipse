/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.pcr.converter.supplier.rdml.model;

import java.util.Comparator;

import org.eclipse.chemclipse.pcr.model.core.IWell;

public class WellComparator implements Comparator<IWell> {

	@Override
	public int compare(IWell firstWell, IWell secondWell) {

		int columnComparison = Integer.compare(firstWell.getPosition().getColumn(), secondWell.getPosition().getColumn());
		if(columnComparison != 0) {
			return columnComparison;
		} else {
			return firstWell.getPosition().getRow().compareTo(secondWell.getPosition().getRow());
		}
	}
}
