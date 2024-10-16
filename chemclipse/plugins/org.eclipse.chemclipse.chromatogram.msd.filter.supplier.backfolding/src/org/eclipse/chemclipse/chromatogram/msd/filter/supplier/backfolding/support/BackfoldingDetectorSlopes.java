/*******************************************************************************
 * Copyright (c) 2011, 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.filter.supplier.backfolding.support;

import org.eclipse.chemclipse.model.signals.ITotalScanSignals;
import org.eclipse.chemclipse.chromatogram.peak.detector.support.DetectorSlopes;

/**
 * @author eselmeister
 */
public class BackfoldingDetectorSlopes extends DetectorSlopes implements IBackfoldingDetectorSlopes {

	public BackfoldingDetectorSlopes(ITotalScanSignals totalIonSignals) {
		super(totalIonSignals);
	}
}
