/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.msd.ui.swt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.ux.extension.msd.ui.internal.provider.UpdateMenuEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtchart.IAxis.Position;
import org.eclipse.swtchart.extensions.axisconverter.PercentageConverter;
import org.eclipse.swtchart.extensions.core.IChartSettings;
import org.eclipse.swtchart.extensions.core.IPrimaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.ISeriesData;
import org.eclipse.swtchart.extensions.core.RangeRestriction;
import org.eclipse.swtchart.extensions.core.SecondaryAxisSettings;
import org.eclipse.swtchart.extensions.core.SeriesData;
import org.eclipse.swtchart.extensions.linecharts.ILineSeriesData;
import org.eclipse.swtchart.extensions.linecharts.LineChart;
import org.eclipse.swtchart.extensions.linecharts.LineSeriesData;

public class MassSpectrumChartProfile extends LineChart implements IMassSpectrumChart {

	private static final int MAX_NUMBER_MZ = 25000;
	//
	private IScanMSD massSpectrum = null;

	public MassSpectrumChartProfile() {
		super();
		initialize();
	}

	public MassSpectrumChartProfile(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	@Override
	public void update(IScanMSD massSpectrum) {

		this.massSpectrum = massSpectrum;
		update();
	}

	@Override
	public void update() {

		deleteSeries();
		if(massSpectrum != null) {
			List<ILineSeriesData> barSeriesDataList = new ArrayList<ILineSeriesData>();
			ISeriesData seriesData = getMassSpectrum(massSpectrum);
			ILineSeriesData lineSeriesData = new LineSeriesData(seriesData);
			barSeriesDataList.add(lineSeriesData);
			addSeriesData(barSeriesDataList, MAX_NUMBER_MZ);
		}
	}

	private void initialize() {

		IChartSettings chartSettings = getChartSettings();
		chartSettings.setOrientation(SWT.HORIZONTAL);
		chartSettings.setHorizontalSliderVisible(true);
		chartSettings.setVerticalSliderVisible(true);
		chartSettings.setCreateMenu(true);
		//
		chartSettings.addMenuEntry(new UpdateMenuEntry());
		//
		RangeRestriction rangeRestriction = chartSettings.getRangeRestriction();
		rangeRestriction.setZeroX(false);
		rangeRestriction.setZeroY(false);
		rangeRestriction.setRestrictZoom(true);
		rangeRestriction.setExtendTypeX(RangeRestriction.ExtendType.ABSOLUTE);
		rangeRestriction.setExtendMinX(2.0d);
		rangeRestriction.setExtendMaxX(2.0d);
		rangeRestriction.setExtendTypeY(RangeRestriction.ExtendType.RELATIVE);
		rangeRestriction.setExtendMaxY(0.1d);
		//
		setPrimaryAxisSet(chartSettings);
		addSecondaryAxisSet(chartSettings);
		applySettings(chartSettings);
	}

	private void setPrimaryAxisSet(IChartSettings chartSettings) {

		IPrimaryAxisSettings primaryAxisSettingsX = chartSettings.getPrimaryAxisSettingsX();
		primaryAxisSettingsX.setTitle("m/z");
		primaryAxisSettingsX.setDecimalFormat(new DecimalFormat(("0.0##"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsX.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		//
		IPrimaryAxisSettings primaryAxisSettingsY = chartSettings.getPrimaryAxisSettingsY();
		primaryAxisSettingsY.setTitle("Intensity");
		primaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.0#E0"), new DecimalFormatSymbols(Locale.ENGLISH)));
		primaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
	}

	private void addSecondaryAxisSet(IChartSettings chartSettings) {

		ISecondaryAxisSettings secondaryAxisSettingsY = new SecondaryAxisSettings("Relative Intensity [%]", new PercentageConverter(SWT.VERTICAL, true));
		secondaryAxisSettingsY.setPosition(Position.Secondary);
		secondaryAxisSettingsY.setDecimalFormat(new DecimalFormat(("0.00"), new DecimalFormatSymbols(Locale.ENGLISH)));
		secondaryAxisSettingsY.setColor(getDisplay().getSystemColor(SWT.COLOR_BLACK));
		chartSettings.getSecondaryAxisSettingsListY().add(secondaryAxisSettingsY);
	}

	private ISeriesData getMassSpectrum(IScanMSD massSpectrum) {

		List<IIon> ions = massSpectrum.getIons();
		int size = ions.size();
		double[] xSeries = new double[size];
		double[] ySeries = new double[size];
		//
		for(int i = 0; i < size; i++) {
			IIon ion = ions.get(i);
			xSeries[i] = ion.getIon();
			ySeries[i] = ion.getAbundance();
		}
		//
		return new SeriesData(xSeries, ySeries, "Mass Spectrum");
	}
}