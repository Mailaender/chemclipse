/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;

public class MassSpectrumPeak implements IMassSpectrumPeak {

	private double mz;
	private double intensity;
	private double sn;
	private double fwhm;
	private double baseline;
	private int charge;
	private final Set<String> classifier = new LinkedHashSet<>();
	private Set<IIdentificationTarget> identificationTargets = new HashSet<>();

	@Override
	public double getIon() {

		return mz;
	}

	@Override
	public void setIon(double mz) {

		this.mz = mz;
	}

	@Override
	public double getAbundance() {

		return intensity;
	}

	@Override
	public void setAbundance(double intensity) {

		this.intensity = intensity;
	}

	@Override
	public double getSignalToNoise() {

		return sn;
	}

	@Override
	public void setSignalToNoise(double sn) {

		this.sn = sn;
	}

	@Override
	public Set<IIdentificationTarget> getTargets() {

		return identificationTargets;
	}

	@Override
	public Collection<String> getClassifier() {

		return Collections.unmodifiableCollection(classifier);
	}

	@Override
	public void addClassifier(String classifier) {

		if(classifier != null && !classifier.trim().isEmpty()) {
			this.classifier.add(classifier.trim());
		}
	}

	@Override
	public void removeClassifier(String classifier) {

		this.classifier.remove(classifier);
	}

	@Override
	public double getFWHM() {

		return fwhm;
	}

	@Override
	public void setFWHM(double fwhm) {

		this.fwhm = fwhm;
	}

	@Override
	public double getBaseline() {

		return baseline;
	}

	@Override
	public void setBaseline(double baseline) {

		this.baseline = baseline;
	}

	@Override
	public int getCharge() {

		return charge;
	}

	@Override
	public void setCharge(int charge) {

		this.charge = charge;
	}
}
