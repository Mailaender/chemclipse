/*******************************************************************************
 * Copyright (c) 2008, 2021 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Lorenz Gerber - add additional smooth method
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.peak.detector.support;

/**
 * @author Philip Wenig
 */
public interface IDetectorSlopes {

	/**
	 * Adds a {@link DetectorSlope} object.
	 * 
	 * @param detectorSlope
	 */
	void add(IDetectorSlope detectorSlope);

	/**
	 * Returns a DetectorSlope object or null, if no object is available.
	 * 
	 * @param scan
	 * @return IDetectorSlope
	 */
	IDetectorSlope getDetectorSlope(int scan);

	/**
	 * Calculates for each stored slope value a smoothed moving average value.<br/>
	 * The window size declares the width of the moving window.
	 * 
	 * @param int
	 */
	void calculateMovingAverage(int windowSize);

	/**
	 * Calculates for each stored slope value a Savitzky-Golay smooothed value.<br/>
	 * The window size declares the filter width of the Savitzky-Golay filter.
	 * 
	 * @param int
	 */
	void calculateSavitzkyGolaySmooth(int windowSize);

	/**
	 * Returns the size of the slope list.
	 * 
	 * @return int
	 */
	int size();

	/**
	 * Returns the start scan.
	 * 
	 * @return
	 */
	int getStartScan();

	/**
	 * Returns the stop scan.
	 * 
	 * @return
	 */
	int getStopScan();
}
