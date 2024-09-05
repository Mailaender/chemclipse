/*******************************************************************************
 * Copyright (c) 2019, 2024 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.ranges;

public class TimeRangeSupport {

	public static void transferTimeRanges(TimeRanges timeRangesSource, TimeRanges timeRangesSink) {

		if(timeRangesSource != null && timeRangesSink != null) {
			for(TimeRange timeRangeSource : timeRangesSource.values()) {
				String identifier = timeRangeSource.getIdentifier();
				TimeRange timeRangeSink = timeRangesSink.get(identifier);
				if(timeRangeSink == null) {
					timeRangeSink = new TimeRange(identifier, 0, 0);
					timeRangesSink.add(timeRangeSink);
				}
				//
				timeRangeSink.update(timeRangeSource.getStart(), timeRangeSource.getMaximum(), timeRangeSource.getStop());
			}
		}
	}
}