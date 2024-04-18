/*******************************************************************************
 * Copyright (c) 2024 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.comparator;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum.IMassSpectrumComparator;
import org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.distance.internal.PearsonDistance;

public class PearsonComparator extends AbstractDistanceComparator implements IMassSpectrumComparator {

	@Override
	DistanceMeasure getDistanceMeasure() {

		return new PearsonDistance();
	}
}