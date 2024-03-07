/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.swt;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.eclipse.chemclipse.csd.model.core.IScanCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IScan;
import org.eclipse.chemclipse.model.types.DataType;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.support.ui.provider.ListContentProvider;
import org.eclipse.chemclipse.support.ui.swt.ExtendedTableViewer;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.IonListContentProviderLazy;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.ScanLabelProvider;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.ScanSignalEditingSupport;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.ScanSignalListFilter;
import org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider.ScanTableComparator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.chemclipse.vsd.model.core.IScanVSD;
import org.eclipse.chemclipse.vsd.model.core.ISignalVSD;
import org.eclipse.chemclipse.wsd.model.core.IScanSignalWSD;
import org.eclipse.chemclipse.wsd.model.core.IScanWSD;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ScanTableUI extends ExtendedTableViewer {

	private static final Logger logger = Logger.getLogger(ScanTableUI.class);
	//
	private EnumMap<DataType, ITableLabelProvider> labelProviderMap;
	private EnumMap<DataType, ViewerComparator> viewerComparatorMap;
	private EnumMap<DataType, IContentProvider> contentProviderMap;
	//
	private ScanSignalListFilter scanSignalListFilter;
	private IScan scan = null;
	//
	private IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	public ScanTableUI(Composite parent, int style) {

		super(parent, style);
		labelProviderMap = new EnumMap<>(DataType.class);
		viewerComparatorMap = new EnumMap<>(DataType.class);
		contentProviderMap = new EnumMap<>(DataType.class);
		setLabelAndContentProviders(DataType.MSD_NOMINAL);
	}

	public void setSearchText(String searchText, boolean caseSensitive) {

		scanSignalListFilter.setSearchText(searchText, caseSensitive);
		refresh();
	}

	/**
	 * Use this method, e.g. when the scan has been edited and the table
	 * needs to be reloaded.
	 */
	public void updateScan() {

		setInput(scan);
	}

	public void setInput(IScan scan) {

		this.scan = scan;
		if(scan instanceof IScanMSD scanMSD) {
			/*
			 * MSD
			 */
			super.setInput(null); // Can only enable the hash look up before input has been set
			IScanMSD optimizedScanMSD = scanMSD.getOptimizedMassSpectrum();
			//
			List<IIon> ions;
			if(optimizedScanMSD != null) {
				ions = optimizedScanMSD.getIons();
			} else {
				ions = scanMSD.getIons();
			}
			int size = ions.size();
			//
			DataType dataType = DataType.MSD_NOMINAL;
			if(scanMSD.isTandemMS()) {
				dataType = DataType.MSD_TANDEM;
			} else {
				if(scanMSD.isHighResolutionMS()) {
					dataType = DataType.MSD_HIGHRES;
				}
			}
			//
			setLabelAndContentProviders(dataType);
			super.setInput(ions);
			setItemCount(size);
		} else if(scan instanceof IScanCSD scanCSD) {
			/*
			 * CSD
			 */
			super.setInput(null);
			setLabelAndContentProviders(DataType.CSD);
			List<IScanCSD> list = new ArrayList<>();
			list.add(scanCSD);
			super.setInput(list);
		} else if(scan instanceof IScanWSD scanWSD) {
			/*
			 * WSD
			 */
			super.setInput(null);
			setLabelAndContentProviders(DataType.WSD);
			super.setInput(scanWSD.getScanSignals());
		} else if(scan instanceof IScanVSD scanVSD) {
			/*
			 * VSD
			 */
			super.setInput(null);
			setLabelAndContentProviders(DataType.VSD);
			super.setInput(scanVSD.getProcessedSignals());
		} else {
			getTable().removeAll();
			super.setInput(null);
		}
	}

	private void setLabelAndContentProviders(DataType dataType) {

		/*
		 * Reset
		 */
		setComparator(null);
		setContentProvider(new ArrayContentProvider());
		setLabelProvider(getTableLabelProvider(DataType.NONE));
		//
		String[] titles = getTitles(dataType);
		int[] bounds = getBounds(dataType);
		createColumns(titles, bounds);
		/*
		 * Relative Intensity [%] calculation
		 */
		ITableLabelProvider labelProvider = getTableLabelProvider(dataType);
		setLabelProvider(labelProvider);
		if(labelProvider instanceof ScanLabelProvider scanLabelProvider) {
			//
			double minIntensity = 0.0f;
			double maxIntensity = 0.0f;
			//
			if(scan instanceof IScanCSD) {
				float intensity = scan.getTotalSignal();
				if(intensity >= 0) {
					minIntensity = 0.0f;
					maxIntensity = intensity;
				} else {
					minIntensity = intensity;
					maxIntensity = 0.0f;
				}
			} else if(scan instanceof IScanMSD scanMSD) {
				minIntensity = 0.0f;
				maxIntensity = scanMSD.getHighestAbundance().getAbundance();
			} else if(scan instanceof IScanWSD scanWSD) {
				minIntensity = scanWSD.getScanSignals().stream().mapToDouble(IScanSignalWSD::getAbsorbance).min().getAsDouble();
				maxIntensity = scanWSD.getScanSignals().stream().mapToDouble(IScanSignalWSD::getAbsorbance).max().getAsDouble();
			} else if(scan instanceof IScanVSD scanVSD) {
				minIntensity = scanVSD.getProcessedSignals().stream().mapToDouble(ISignalVSD::getIntensity).min().getAsDouble();
				maxIntensity = scanVSD.getProcessedSignals().stream().mapToDouble(ISignalVSD::getIntensity).max().getAsDouble();
			}
			//
			scanLabelProvider.setTotalIntensity(minIntensity, maxIntensity);
		}
		//
		IContentProvider contentProvider = getContentProvider(dataType);
		if(useVirtualTableSettings(scan, dataType)) {
			/*
			 * Lazy (Virtual)
			 */
			logger.info("Lazy (Virtual) Modus");
			setContentProvider(contentProvider);
			setUseHashlookup(true);
			setComparator(null);
		} else {
			/*
			 * Normal
			 */
			logger.info("Normal Modus");
			setContentProvider(contentProvider);
			setUseHashlookup(false);
			ViewerComparator viewerComparator = getViewerComparator(dataType);
			setComparator(viewerComparator);
		}
		//
		scanSignalListFilter = new ScanSignalListFilter();
		setFilters(scanSignalListFilter);
		setEditingSupport();
	}

	private String[] getTitles(DataType dataType) {

		String[] titles;
		switch(dataType) {
			case MSD_NOMINAL:
				titles = ScanLabelProvider.TITLES_MSD_NOMINAL;
				break;
			case MSD_TANDEM:
				titles = ScanLabelProvider.TITLES_MSD_TANDEM;
				break;
			case MSD_HIGHRES:
				titles = ScanLabelProvider.TITLES_MSD_HIGHRES;
				break;
			case CSD:
				titles = ScanLabelProvider.TITLES_CSD;
				break;
			case WSD:
				titles = ScanLabelProvider.TITLES_WSD;
				break;
			case VSD:
				titles = ScanLabelProvider.TITLES_VSD;
				break;
			default:
				titles = ScanLabelProvider.TITLES_EMPTY;
		}
		return titles;
	}

	private int[] getBounds(DataType dataType) {

		int[] bounds;
		switch(dataType) {
			case MSD_NOMINAL:
				bounds = ScanLabelProvider.BOUNDS_MSD_NOMINAL;
				break;
			case MSD_TANDEM:
				bounds = ScanLabelProvider.BOUNDS_MSD_TANDEM;
				break;
			case MSD_HIGHRES:
				bounds = ScanLabelProvider.BOUNDS_MSD_HIGHRES;
				break;
			case CSD:
				bounds = ScanLabelProvider.BOUNDS_CSD;
				break;
			case WSD:
				bounds = ScanLabelProvider.BOUNDS_WSD;
				break;
			case VSD:
				bounds = ScanLabelProvider.BOUNDS_VSD;
				break;
			default:
				bounds = ScanLabelProvider.BOUNDS_EMPTY;
		}
		return bounds;
	}

	private ITableLabelProvider getTableLabelProvider(DataType dataType) {

		ITableLabelProvider tableLableProvider = labelProviderMap.get(dataType);
		if(tableLableProvider == null) {
			tableLableProvider = new ScanLabelProvider(dataType);
			labelProviderMap.put(dataType, tableLableProvider);
		}
		return tableLableProvider;
	}

	private ViewerComparator getViewerComparator(DataType dataType) {

		ViewerComparator viewerComparator = viewerComparatorMap.get(dataType);
		if(viewerComparator == null) {
			viewerComparator = new ScanTableComparator(dataType);
			viewerComparatorMap.put(dataType, viewerComparator);
		}
		return viewerComparator;
	}

	private IContentProvider getContentProvider(DataType dataType) {

		IContentProvider contentProvider = contentProviderMap.get(dataType);
		if(contentProvider == null) {
			if(useVirtualTableSettings(scan, dataType)) {
				contentProvider = new IonListContentProviderLazy(this);
			} else {
				contentProvider = new ListContentProvider();
			}
			contentProviderMap.put(dataType, contentProvider);
		}
		return contentProvider;
	}

	private boolean useVirtualTableSettings(IScan scan, DataType dataType) {

		if(isVirtualTable()) {
			if(scan != null) {
				if(scan instanceof IScanMSD scanMSD) {
					if(dataType.equals(DataType.MSD_HIGHRES)) {
						int numberIons = scanMSD.getNumberOfIons();
						if(numberIons > preferenceStore.getInt(PreferenceSupplier.P_TRACES_VIRTUAL_TABLE)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isVirtualTable() {

		return ((getTable().getStyle() & SWT.VIRTUAL) == SWT.VIRTUAL);
	}

	private void setEditingSupport() {

		List<TableViewerColumn> tableViewerColumns = getTableViewerColumns();
		for(int i = 0; i < tableViewerColumns.size(); i++) {
			TableViewerColumn tableViewerColumn = tableViewerColumns.get(i);
			String label = tableViewerColumn.getColumn().getText();
			if(label.equals(ScanLabelProvider.INTENSITY)) {
				tableViewerColumn.setEditingSupport(new ScanSignalEditingSupport(this, label));
			}
		}
	}
}