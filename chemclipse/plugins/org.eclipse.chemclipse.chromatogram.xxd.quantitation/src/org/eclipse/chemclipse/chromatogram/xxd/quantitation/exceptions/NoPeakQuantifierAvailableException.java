/*******************************************************************************
 * Copyright (c) 2013, 2022 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.quantitation.exceptions;

public class NoPeakQuantifierAvailableException extends Exception {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -984735869392879575L;

	public NoPeakQuantifierAvailableException() {

		super();
	}

	public NoPeakQuantifierAvailableException(String message) {

		super(message);
	}
}
