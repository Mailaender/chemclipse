/*******************************************************************************
 * Copyright (c) 2026 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.eclipse.chemclipse.model.core.IChromatogram;
import org.eclipse.chemclipse.model.core.IScan;

/**
 * Labels a scan number axis with the cycle number, e.g. the position of a called base.
 * <p>
 * The axis itself stays a scan number axis, so every tick is drawn where its scan really is. Only the
 * printed label is translated, which keeps the called bases in place. The bases are not evenly spaced,
 * hence the translation interpolates between the called scans instead of assuming a constant number of
 * scans per cycle. Beyond the first and the last called scan the neighbouring segment is extrapolated.
 */
public class ScanNumberToCycleNumberFormat extends DecimalFormat {

	private static final long serialVersionUID = 2295849707518664533L;

	/*
	 * A scan without a cycle number reports 1, hence only the second and the following cycles are reliable anchors.
	 */
	private static final int FIRST_RELIABLE_CYCLE = 2;

	private final int[] scanNumbers;
	private final int[] cycleNumbers;

	public ScanNumberToCycleNumberFormat(IChromatogram chromatogram) {

		super("0", new DecimalFormatSymbols(Locale.ENGLISH));

		List<IScan> scans = chromatogram.getScans();
		int[] scanBuffer = new int[scans.size()];
		int[] cycleBuffer = new int[scans.size()];
		int size = 0;

		for(IScan scan : scans) {
			int cycleNumber = scan.getCycleNumber();
			if(cycleNumber >= FIRST_RELIABLE_CYCLE && (size == 0 || cycleNumber > cycleBuffer[size - 1])) {
				scanBuffer[size] = scan.getScanNumber();
				cycleBuffer[size] = cycleNumber;
				size++;
			}
		}

		scanNumbers = Arrays.copyOf(scanBuffer, size);
		cycleNumbers = Arrays.copyOf(cycleBuffer, size);
	}

	/**
	 * Returns false if the chromatogram carries too few cycle numbers to span a segment, e.g. because
	 * no base calling has been run yet. The axis must not be added in that case.
	 * 
	 * @return boolean
	 */
	public boolean hasCycleNumbers() {

		return scanNumbers.length >= 2;
	}

	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {

		return super.format(interpolate(scanNumbers, cycleNumbers, number), toAppendTo, pos);
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {

		Number result = super.parse(source, parsePosition);
		if(result != null) {
			return interpolate(cycleNumbers, scanNumbers, result.doubleValue());
		}

		return result;
	}

	private double interpolate(int[] keys, int[] values, double key) {

		if(!hasCycleNumbers()) {
			return key;
		}

		int upper = getSegmentIndex(keys, key);
		int lower = upper - 1;
		double fraction = (key - keys[lower]) / (double)(keys[upper] - keys[lower]);

		return values[lower] + fraction * (values[upper] - values[lower]);
	}

	/**
	 * Returns the upper bound of the segment the key falls into. The first respectively the last
	 * segment is returned for a key outside of the anchors, which extrapolates rather than clips.
	 */
	private static int getSegmentIndex(int[] keys, double key) {

		int low = 1;
		int high = keys.length - 1;

		while(low < high) {
			int mid = (low + high) >>> 1;
			if(keys[mid] < key) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}

		return low;
	}
}
