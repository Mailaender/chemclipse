/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.converter.sequence;

import org.eclipse.chemclipse.converter.core.IMagicNumberMatcher;

public interface ISequenceSupplierSetter extends ISequenceSupplier {

	void setId(final String id);

	void setDescription(final String description);

	void setFilterName(final String filterName);

	void setFileExtension(final String fileExtension);

	void setFileName(final String fileName);

	void setMagicNumberMatcher(final IMagicNumberMatcher magicNumberMatcher);
}