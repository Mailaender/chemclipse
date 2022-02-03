/*******************************************************************************
 * Copyright (c) 2022 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.rcp.app.undo;

import org.eclipse.chemclipse.support.preferences.SupportPreferences;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.commands.operations.UndoContext;

public final class UndoContextFactory {

	private static IUndoContext undoContext;

	public static IUndoContext getUndoContext() {

		if(undoContext == null) {
			undoContext = new UndoContext();
		}
		OperationHistoryFactory.getOperationHistory().setLimit(undoContext, SupportPreferences.getUndoLimit());
		return undoContext;
	}

	private UndoContextFactory() {

		// may not be instantiated
	}
}
