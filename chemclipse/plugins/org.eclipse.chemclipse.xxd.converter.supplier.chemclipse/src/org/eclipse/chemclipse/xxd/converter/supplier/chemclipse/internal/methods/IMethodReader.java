/*******************************************************************************
 * Copyright (c) 2018, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - javadoc
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.methods;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.model.methods.IProcessMethod;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IMethodReader {

	/**
	 * try to convert the given file
	 * 
	 * @param file
	 * @param monitor
	 * @return <code>null</code> if this reader is not valid for the given file
	 * @throws IOException
	 */
	IProcessMethod convert(File file, IProgressMonitor monitor) throws IOException;
}
