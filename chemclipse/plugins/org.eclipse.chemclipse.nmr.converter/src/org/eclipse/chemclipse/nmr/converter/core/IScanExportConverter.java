/*******************************************************************************
 * Copyright (c) 2018, 2022 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Alexander Kerner - Generics
 * Christoph Läubrich - rework interface
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.converter.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.chemclipse.converter.core.IExportConverter;
import org.eclipse.chemclipse.model.core.IComplexSignalMeasurement;
import org.eclipse.chemclipse.processing.core.IMessageConsumer;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IScanExportConverter extends IExportConverter {

	default void convert(File file, IComplexSignalMeasurement<?> measurement, IMessageConsumer messageConsumer, IProgressMonitor monitor) throws IOException {

		try (FileOutputStream fout = new FileOutputStream(file)) {
			convert(fout, measurement, messageConsumer, monitor);
		}
	}

	/**
	 * Converts the given {@link IComplexSignalMeasurement} and writes them to the {@link OutputStream}
	 * 
	 * @param stream
	 *            the stream the data should be written to
	 * @param measurement
	 *            the measurement to write
	 * @param messageConsumer
	 *            a {@link IMessageConsumer} that can be used to inform the caller about info/warning/errors
	 * @param monitor
	 *            a {@link IProgressMonitor} to use for reporting progress or <code>null</code> if no progress is desired
	 * @throws IOException
	 *             if any {@link IOException} occurs while writing to the stream, other errors/problems should be reported to the {@link IMessageConsumer}
	 */
	void convert(OutputStream stream, IComplexSignalMeasurement<?> measurement, IMessageConsumer messageConsumer, IProgressMonitor monitor) throws IOException;
}
