/*******************************************************************************
 * Copyright (c) 2018, 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - add support for name editing, improve classifier support
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;

public class PeakScanListEditingSupport extends EditingSupport {

	private CellEditor cellEditor;
	private final ExtendedTableViewer tableViewer;
	private final String column;

	public PeakScanListEditingSupport(ExtendedTableViewer tableViewer, String column) {

		super(tableViewer);
		this.column = column;
		if(PeakScanListLabelProvider.ACTIVE_FOR_ANALYSIS.equals(column)) {
			this.cellEditor = new CheckboxCellEditor(tableViewer.getTable());
		} else {
			this.cellEditor = new TextCellEditor(tableViewer.getTable());
		}
		this.tableViewer = tableViewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {

		if(column == PeakScanListLabelProvider.ACTIVE_FOR_ANALYSIS) {
			return (element instanceof IPeak);
		} else {
			return tableViewer.isEditEnabled();
		}
	}

	@Override
	protected Object getValue(Object element) {

		if(element instanceof IPeak) {
			IPeak peak = (IPeak)element;
			switch(column) {
				case PeakScanListLabelProvider.ACTIVE_FOR_ANALYSIS:
					return peak.isActiveForAnalysis();
			}
		}
		return false;
	}

	@Override
	protected void setValue(Object element, Object value) {

		if(element instanceof IPeak) {
			IPeak peak = (IPeak)element;
			switch(column) {
				case PeakScanListLabelProvider.ACTIVE_FOR_ANALYSIS:
					peak.setActiveForAnalysis((boolean)value);
					break;
			}
			tableViewer.refresh(element);
		}
	}
}
