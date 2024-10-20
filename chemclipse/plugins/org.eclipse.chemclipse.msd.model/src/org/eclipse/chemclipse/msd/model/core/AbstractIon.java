/*******************************************************************************
 * Copyright (c) 2008, 2018 Lablicate GmbH.
 *
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.model.core;

import org.eclipse.chemclipse.model.exceptions.AbundanceLimitExceededException;
import org.eclipse.chemclipse.msd.model.exceptions.IonIsNullException;
import org.eclipse.chemclipse.msd.model.exceptions.IonLimitExceededException;
import org.eclipse.chemclipse.msd.model.exceptions.IonTransitionIsNullException;
import org.eclipse.core.runtime.Platform;

/**
 * All ions implement the interface Serializable to enable an
 * automated storage in disk.<br/>
 * The serialization of ions is controlled by the corresponding mass
 * spectrum.
 *
 * @author eselmeister
 * @author <a href="mailto:alexander.kerner@openchrom.net">Alexander Kerner</a>
 * @see AbstractScanMSD
 */
public abstract class AbstractIon implements IIon {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -2481473608026036078L;
	private double ion = 0.0d;
	private float abundance = 0.0f;
	private static final int MAX_PRECISION = 6;
	private IIonTransition ionTransition;

	public AbstractIon(double ion) throws IonLimitExceededException {
		setIon(ion);
	}

	public AbstractIon(double ion, float abundance) throws AbundanceLimitExceededException, IonLimitExceededException {
		/*
		 * Why is setIon(ion) ... used here instead of this.ion = ion?<br/> The
		 * methods setIon(float ion) and setAbundance(float abundance) are
		 * overridden by AbstractSupplierIon. Why?<br/> We do not
		 * actually know which range of ion and abundance values each supplier
		 * does support. Therefore the methods are overridden in
		 * AbstractSupplierIon.<br/> Depending on the value range each
		 * implementation of ISupplierIon has declared, the values will
		 * be accepted or an exception will be thrown.
		 */
		setIon(ion);
		setAbundance(abundance);
	}

	public AbstractIon(double ion, float abundance, IIonTransition ionTransition) throws AbundanceLimitExceededException, IonLimitExceededException, IonTransitionIsNullException {
		/*
		 * Why is setIon(ion) ... used here instead of this.ion = ion?<br/> The
		 * methods setIon(float ion) and setAbundance(float abundance) are
		 * overridden by AbstractSupplierIon. Why?<br/> We do not
		 * actually know which range of ion and abundance values each supplier
		 * does support. Therefore the methods are overridden in
		 * AbstractSupplierIon.<br/> Depending on the value range each
		 * implementation of ISupplierIon has declared, the values will
		 * be accepted or an exception will be thrown.
		 */
		setIon(ion);
		setAbundance(abundance);
		if(ionTransition != null) {
			this.ionTransition = ionTransition;
		} else {
			throw new IonTransitionIsNullException("The given ion transition instance should be not null.");
		}
	}

	public AbstractIon(IIon ion) throws AbundanceLimitExceededException, IonLimitExceededException, IonIsNullException {
		/*
		 * Why is setIon(ion) ... used here instead of this.ion = ion?<br/> The
		 * methods setIon(float ion) and setAbundance(float abundance) are
		 * overridden by AbstractSupplierIon. Why?<br/> We do not
		 * actually know which range of ion and abundance values each supplier
		 * does support. Therefore the methods are overridden in
		 * AbstractSupplierIon.<br/> Depending on the value range each
		 * implementation of ISupplierIon has declared, the values will
		 * be accepted or an exception will be thrown.
		 */
		if(ion != null) {
			setIon(ion.getIon());
			setAbundance(ion.getAbundance());
		} else {
			throw new IonIsNullException("The given ion instance should be not null.");
		}
	}

	public AbstractIon(IIon ion, IIonTransition ionTransition) throws AbundanceLimitExceededException, IonLimitExceededException, IonIsNullException, IonTransitionIsNullException {
		/*
		 * Why is setIon(ion) ... used here instead of this.ion = ion?<br/> The
		 * methods setIon(float ion) and setAbundance(float abundance) are
		 * overridden by AbstractSupplierIon. Why?<br/> We do not
		 * actually know which range of ion and abundance values each supplier
		 * does support. Therefore the methods are overridden in
		 * AbstractSupplierIon.<br/> Depending on the value range each
		 * implementation of ISupplierIon has declared, the values will
		 * be accepted or an exception will be thrown.
		 */
		this(ion);
		if(ionTransition != null) {
			this.ionTransition = ionTransition;
		} else {
			throw new IonTransitionIsNullException("The given ion transition instance should be not null.");
		}
	}

	// -----------------------------static
	/**
	 * Returns the given ion as an integer value.
	 */
	public static int getIon(double ion) {

		/*
		 * Should another rounding range be used? E.g 0.7 - 1.7?
		 */
		return (int)Math.round(ion);
	}

	// TODO JUnit
	/**
	 * Returns the given ion as an value rounded to the given precision.
	 * E.g.:
	 * ion = 28.78749204
	 * precision 1 => 28.8
	 * precision 2 => 28.79
	 * precision 3 => 28.787
	 * precision 4 => 28.7875
	 * precision 5 => 28.78749
	 * precision 6 => 28.787492
	 *
	 * The precision of 6 is the maximum. If the precious is outward of
	 * this bounds it will set to 1.
	 */
	public static double getIon(double ion, int precision) {

		/*
		 * Should another rounding range be used? E.g 0.7 - 1.7?
		 */
		if(precision <= 0 || precision > MAX_PRECISION) {
			precision = 1;
		}
		double factor = Math.pow(10, precision);
		return Math.round(ion * factor) / factor;
	}

	/**
	 * Returns the given abundance as an integer value.
	 */
	public static int getAbundance(float abundance) {

		/*
		 * Should another rounding range be used? E.g 0.7 - 1.7?
		 */
		return Math.round(abundance);
	}

	// -----------------------------static
	@Override
	public float getAbundance() {

		return this.abundance;
	}

	@Override
	public double getIon() {

		return this.ion;
	}

	@Override
	public AbstractIon setAbundance(float abundance) throws AbundanceLimitExceededException {

		if(abundance < 0) {
			throw new AbundanceLimitExceededException("The abundance value can't be negative. It is actual: " + abundance);
		}
		this.abundance = abundance;
		return this;
	}

	@Override
	public AbstractIon setIon(double ion) throws IonLimitExceededException {

		if(ion < 0) {
			throw new IonLimitExceededException("The ion value can't be negative. It is actual: " + ion);
		}
		this.ion = ion;
		return this;
	}

	@Override
	public IIonTransition getIonTransition() {

		return ionTransition;
	}

	// -----------------------------Comparable<IIon>
	/**
	 * Compares the mass/charge ration of two ions. Returns the
	 * following values: a.compareTo(b) 0 a == b : 28 == 28 -1 a < b : 18 < 28
	 * +1 a > b : 28 > 18
	 */
	@Override
	public int compareTo(IIon other) {

		return (int)(this.ion - other.getIon());
	}

	// -----------------------------Comparable<IIon>
	// -----------------------------IAdaptable
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Object getAdapter(Class adapter) {

		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	// -----------------------------IAdaptable
	// -----------------------------equals, hashCode, toString
	@Override
	public boolean equals(Object otherObject) {

		if(this == otherObject) {
			return true;
		}
		if(otherObject == null) {
			return false;
		}
		if(getClass() != otherObject.getClass()) {
			return false;
		}
		AbstractIon other = (AbstractIon)otherObject;
		return ion == other.getIon() && abundance == other.getAbundance() && ionTransition == other.getIonTransition();
	}

	@Override
	public int hashCode() {

		int ionTransitionHashCode = 0;
		if(ionTransition != null) {
			ionTransitionHashCode = ionTransition.hashCode();
		}
		return 7 * new Double(ion).hashCode() + 11 * new Float(abundance).hashCode() + ionTransitionHashCode;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[");
		builder.append("ion=" + ion);
		builder.append(",");
		builder.append("abundance=" + abundance);
		builder.append(",");
		builder.append("ionTransition=" + ionTransition);
		builder.append("]");
		return builder.toString();
	}
	// -----------------------------equals, hashCode, toString
}
