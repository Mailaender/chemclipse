/*******************************************************************************
 * Copyright (c) 2018, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder differentiate transmission vs absorbance
 *******************************************************************************/
package org.eclipse.chemclipse.xir.model.implementation;

import org.eclipse.chemclipse.model.core.AbstractSignal;
import org.eclipse.chemclipse.xir.model.core.ISignalXIR;

public class SignalXIR extends AbstractSignal implements ISignalXIR, Comparable<ISignalXIR> {

	private static final long serialVersionUID = -2575735757102126907L;
	//
	private double wavenumber = 0.0d; // 1/cm
	private double absorbance = 0.0d;
	private double transmission = 0.0d;
	private double scattering = 0.0d;

	public SignalXIR() {

	}

	public SignalXIR(double wavenumber, double absorbance, double transmission) {

		this(wavenumber, absorbance, transmission, 0.0d);
	}

	public SignalXIR(double wavenumber, double absorbance, double transmission, double scattering) {

		this.wavenumber = wavenumber;
		this.absorbance = absorbance;
		this.transmission = transmission;
		this.scattering = scattering;
	}

	@Override
	public double getX() {

		return wavenumber;
	}

	@Override
	public double getY() {

		if(transmission > 0) {
			return transmission;
		} else {
			if(absorbance > 0) {
				return absorbance;
			} else {
				return scattering;
			}
		}
	}

	@Override
	public double getWavenumber() {

		return wavenumber;
	}

	@Override
	public void setWavenumber(double wavelength) {

		if(wavelength >= 0) {
			this.wavenumber = wavelength;
		}
	}

	@Override
	public double getTransmission() {

		if(transmission > 0) {
			return transmission;
		}
		//
		if(absorbance > 0) {
			return 100 / Math.pow(10, absorbance);
		}
		//
		return 100;
	}

	@Override
	public void setTransmission(double transmission) {

		this.transmission = transmission;
	}

	@Override
	public double getAbsorbance() {

		if(absorbance > 0) {
			return absorbance;
		} else if(transmission > 0) {
			return Math.log(1 / transmission);
		}
		//
		return 0;
	}

	@Override
	public void setAbsorbance(double absorbance) {

		this.absorbance = absorbance;
	}

	@Override
	public double getScattering() {

		return scattering;
	}

	@Override
	public void setScattering(double scattering) {

		this.scattering = scattering;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(wavenumber);
		result = prime * result + (int)(temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		SignalXIR other = (SignalXIR)obj;
		return (Double.doubleToLongBits(wavenumber) == Double.doubleToLongBits(other.wavenumber));
	}

	@Override
	public String toString() {

		return "SignalXIR [wavenumber=" + wavenumber + ", absorbance=" + absorbance + ", transmission=" + transmission + ", scattering=" + scattering + "]";
	}

	@Override
	public int compareTo(ISignalXIR signalXIR) {

		if(signalXIR != null) {
			return Double.compare(wavenumber, signalXIR.getWavenumber());
		} else {
			return 0;
		}
	}
}