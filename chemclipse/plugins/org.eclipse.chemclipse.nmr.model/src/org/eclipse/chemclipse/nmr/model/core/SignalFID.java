/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * jan - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.nmr.model.core;

import java.util.Objects;

import org.apache.commons.math3.complex.Complex;

public class SignalFID implements ISignalFID {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2563457000126174921L;
	private long time;
	private Complex intesityFID;
	private Complex intensity;

	public SignalFID() {

		super();
	}

	public SignalFID(long time, Complex intesityFID) {

		this();
		this.time = time;
		this.intesityFID = intesityFID;
		this.intensity = intesityFID;
	}

	@Override
	public double getX() {

		return time;
	}

	@Override
	public double getY() {

		return intensity.getReal();
	}

	@Override
	public Complex getIntensity() {

		return intensity;
	}

	@Override
	public void setIntensity(Complex intensity) {

		this.intensity = intensity;
	}

	@Override
	public void resetIntensity() {

		intensity = intesityFID;
	}

	@Override
	public void setIntensityFID(Complex magnitude) {

		this.intesityFID = magnitude;
	}

	@Override
	public Complex getIntensityFID() {

		return intesityFID;
	}

	@Override
	public long getTime() {

		return time;
	}

	@Override
	public void setTime(long nanoseconds) {

		this.time = nanoseconds;
	}

	@Override
	public int compareTo(ISignalFID o) {

		if(o != null) {
			return Long.compare(time, o.getTime());
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {

		return Objects.hash(time);
	}
}
