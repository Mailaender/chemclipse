/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.swt.columns;

import java.util.Comparator;
import java.util.function.Function;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * 
 * @author Christoph Läubrich
 *
 * @param <ColumnType>
 *            the Datatype of the column
 */
public interface ColumnDefinition<DataType, ColumnType> extends Function<DataType, ColumnType> {

	/**
	 * 
	 * @return the {@link ColumnLabelProvider} to use for this column or <code>null</code> if no special {@link ColumnLabelProvider} is desired
	 */
	ColumnLabelProvider getLabelProvider();

	/**
	 * 
	 * @return a {@link Comparator} to use to compare columns or <code>null</code> if no sorting is desired
	 */
	Comparator<ColumnType> getComparator();

	String getTitle();

	int getWidth();
}
