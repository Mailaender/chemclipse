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
package org.eclipse.chemclipse.xxd.converter.supplier.chemclipse.internal.methods;

import java.io.File;
import java.io.IOException;

import org.eclipse.chemclipse.model.methods.ProcessMethod;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IMethodWriter {

	void convert(File file, ProcessMethod processMethod, IProgressMonitor monitor) throws IOException;
}
