/*******************************************************************************
 * Copyright (c) 2014, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.swt.ui.internal.provider;

import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class SynonymsListTableComparator extends AbstractRecordTableComparator implements IRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof String synonym1 && e2 instanceof String synonym2) {
			switch(getPropertyIndex()) {
				case 0: // Synonym
					sortOrder = synonym2.compareTo(synonym1);
					break;
				default:
					sortOrder = 0;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}
}
