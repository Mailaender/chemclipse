/*******************************************************************************
 * Copyright (c) 2019, 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * jan - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.core.preprocessing;

public interface IDataModificator extends IPreprocessing {

	boolean isModifyOnlySelectedVariable();

	void setModifyOnlySelectedVariable(boolean modifyOnlySelectedVariable);

	boolean isRemoveUselessVariables();

	void setRemoveUselessVariables(boolean removeUselessVariables);
}
