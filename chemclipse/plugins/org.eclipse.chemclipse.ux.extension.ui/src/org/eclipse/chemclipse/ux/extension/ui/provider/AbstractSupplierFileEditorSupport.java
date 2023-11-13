/*******************************************************************************
 * Copyright (c) 2013, 2023 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.ui.provider;

import java.util.List;

import org.eclipse.chemclipse.processing.converter.AbstractSupplierFileIdentifier;
import org.eclipse.chemclipse.processing.converter.ISupplier;

public abstract class AbstractSupplierFileEditorSupport extends AbstractSupplierFileIdentifier implements ISupplierFileEditorSupport {

	protected AbstractSupplierFileEditorSupport(List<ISupplier> suppliers) {

		super(suppliers);
	}
}
