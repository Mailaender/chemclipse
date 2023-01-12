/*******************************************************************************
 * Copyright (c) 2013, 2023 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.quantitation;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.numeric.equations.IQuadraticEquation;
import org.eclipse.chemclipse.numeric.equations.LinearEquation;
import org.eclipse.chemclipse.numeric.equations.QuadraticEquation;

/**
 * This must be a list, cause it could contains more than one
 * value for a specific signal.
 */
public interface IResponseSignals extends List<IResponseSignal>, Serializable {

	/**
	 * Returns the linear equation.
	 * 
	 * @param signal
	 * @param isCrossZero
	 * @return {@link LinearEquation}
	 */
	LinearEquation getLinearEquation(double signal, boolean isCrossZero);

	/**
	 * Returns the quadratic equation.
	 * 
	 * @param signal
	 * @param isCrossZero
	 * @return {@link QuadraticEquation}
	 */
	IQuadraticEquation getQuadraticEquation(double signal, boolean isCrossZero);

	/**
	 * Returns the average factor to calculate the unknown concentration
	 * of a known intensity.
	 * 
	 * factor = Concentration Average / Intensity Average
	 * 
	 * Concentration Unknown = factor * Intensity Unknown
	 * 
	 * @param signal
	 * @param isCrossZero
	 * @return double
	 */
	double getAverageFactor(double signal, boolean isCrossZero);

	double getMinResponseValue(double signal);

	double getMaxResponseValue(double signal);

	/**
	 * Returns the min response value of the stored concentration response entries.
	 * Or 0 if none value is stored.
	 * 
	 * @return double
	 */
	double getMinResponseValue();

	/**
	 * Returns the max response value of the stored concentration response entries.
	 * Or 0 if none value is stored.
	 * 
	 * @return double
	 */
	double getMaxResponseValue();

	/**
	 * Returns the set of used signals.
	 * 
	 * @return Set<Double>
	 */
	Set<Double> getSignalSet();

	/**
	 * Returns the list of concentration response entries,
	 * denoted by the given signal.
	 * 
	 * @param signal
	 * @return
	 */
	List<IResponseSignal> getList(double signal);
}