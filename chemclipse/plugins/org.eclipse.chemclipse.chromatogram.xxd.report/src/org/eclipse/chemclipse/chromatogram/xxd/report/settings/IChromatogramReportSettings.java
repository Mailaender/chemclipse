/*******************************************************************************
 * Copyright (c) 2012, 2022 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - add support for append, exportfolder and filename pattern
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.report.settings;

import java.io.File;

import org.eclipse.chemclipse.model.settings.IProcessSettings;

public interface IChromatogramReportSettings extends IProcessSettings {

	File getExportFolder();

	boolean isAppend();

	String getFileNamePattern();
}