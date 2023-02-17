/*******************************************************************************
 * Copyright (c) 2019, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactoring Observable
 *******************************************************************************/
package org.eclipse.chemclipse.support.ui.swt.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.chemclipse.rcp.ui.icons.core.ApplicationImageFactory;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImage;
import org.eclipse.chemclipse.rcp.ui.icons.core.IApplicationImageProvider;
import org.eclipse.chemclipse.support.ui.l10n.SupportMessages;
import org.eclipse.chemclipse.support.ui.swt.ControlBuilder;
import org.eclipse.chemclipse.support.ui.swt.columns.ColumnDefinitionProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ListEdit<V> extends EditValue<List<V>> {

	private static final long serialVersionUID = 4293060148111233267L;
	private static final String LIST_EDIT = "ListEdit"; //$NON-NLS-1$
	//
	private final TableViewer tableViewer;
	private final List<V> initialValues = new ArrayList<>();
	private final List<V> currentValues = new CopyOnWriteArrayList<V>();
	private final Action addAction;
	private final Action deleteAction;
	private final Action editAction;
	private boolean edited;

	public ListEdit(Composite parent, ColumnDefinitionProvider columnDefinitionProvider, ListEditModel<V> editModel) {

		Composite container = ControlBuilder.createContainer(parent, 2);
		ControlBuilder.gridData(container).heightHint = 200;
		tableViewer = ControlBuilder.createTable(container, false);
		ControlBuilder.maximize(tableViewer.getControl());
		initialValues.addAll(editModel.list());
		currentValues.addAll(initialValues);
		ControlBuilder.createColumns(tableViewer, columnDefinitionProvider, false);
		tableViewer.setInput(currentValues);
		ToolBarManager toolbar = new ToolBarManager(SWT.VERTICAL);
		//
		addAction = new Action(SupportMessages.add, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_ADD, IApplicationImageProvider.SIZE_16x16)) {

			@Override
			public void run() {

				V newItem = editModel.create();
				if(newItem != null) {
					currentValues.add(newItem);
					tableViewer.refresh();
					updateChange(this, LIST_EDIT, currentValues, currentValues);
				}
				updateButtons(editModel);
			}
		};
		//
		deleteAction = new Action(SupportMessages.remove, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_REMOVE, IApplicationImageProvider.SIZE_16x16)) {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				boolean changed = false;
				Object[] array = tableViewer.getStructuredSelection().toArray();
				for(Object object : array) {
					if(currentValues.remove(object)) {
						editModel.delete((V)object);
						changed = true;
					}
				}
				if(changed) {
					tableViewer.refresh();
					updateChange(this, LIST_EDIT, currentValues, currentValues);
				}
				updateButtons(editModel);
			}
		};
		//
		editAction = new Action(SupportMessages.remove, ApplicationImageFactory.getInstance().getImageDescriptor(IApplicationImage.IMAGE_EDIT, IApplicationImageProvider.SIZE_16x16)) {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				Object element = tableViewer.getStructuredSelection().getFirstElement();
				if(element != null && editModel.edit((V)element)) {
					edited = true;
					tableViewer.refresh();
					updateChange(this, LIST_EDIT, currentValues, currentValues);
				}
				updateButtons(editModel);
			}
		};
		//
		updateButtons(editModel);
		toolbar.add(addAction);
		toolbar.add(editAction);
		toolbar.add(deleteAction);
		ControlBuilder.gridData(toolbar.createControl(container)).verticalAlignment = SWT.TOP;
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent sce) {

				deleteAction.setEnabled(!tableViewer.getStructuredSelection().isEmpty());
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void updateButtons(ListEditModel<V> editModel) {

		addAction.setEnabled(editModel.canCreate());
		IStructuredSelection selection = getTableViewer().getStructuredSelection();
		if(selection.isEmpty()) {
			deleteAction.setEnabled(false);
			editAction.setEnabled(false);
		} else {
			boolean canDelete = true;
			Object[] array = selection.toArray();
			for(Object o : array) {
				if(canDelete && !editModel.canDelete((V)o)) {
					canDelete = false;
					break;
				}
			}
			deleteAction.setEnabled(canDelete);
			editAction.setEnabled(array.length == 1 && editModel.canEdit((V)array[0]));
		}
	}

	@Override
	public boolean isEdited() {

		return edited || !initialValues.equals(currentValues);
	}

	@Override
	public List<V> getValue() {

		return currentValues;
	}

	@Override
	public Control getControl() {

		return getTableViewer().getControl();
	}

	public TableViewer getTableViewer() {

		return tableViewer;
	}
}
