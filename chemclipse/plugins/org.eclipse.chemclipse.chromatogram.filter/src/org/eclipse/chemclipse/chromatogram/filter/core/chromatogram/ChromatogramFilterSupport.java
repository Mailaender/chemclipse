/*******************************************************************************
 * Copyright (c) 2016, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.filter.core.chromatogram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.filter.exceptions.NoChromatogramFilterSupplierAvailableException;

public class ChromatogramFilterSupport implements IChromatogramFilterSupport {

	private List<IChromatogramFilterSupplier> suppliers;

	/**
	 * Creates a new suppliers list.
	 */
	public ChromatogramFilterSupport() {

		suppliers = new ArrayList<>();
	}

	/**
	 * Adds a {@link IChromatogramFilterSupplier} to the {@link IChromatogramFilterSupport}.
	 * 
	 * @param supplier
	 */
	protected void add(IChromatogramFilterSupplier supplier) {

		suppliers.add(supplier);
	}

	@Override
	public List<String> getAvailableFilterIds() throws NoChromatogramFilterSupplierAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areChromatogramFiltersStored();
		List<String> availableFilters = new ArrayList<>();
		for(IChromatogramFilterSupplier supplier : suppliers) {
			availableFilters.add(supplier.getId());
		}
		return availableFilters;
	}

	@Override
	public String getFilterId(int index) throws NoChromatogramFilterSupplierAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areChromatogramFiltersStored();
		/*
		 * Test if the index is out of range.
		 */
		if(index < 0 || index > suppliers.size() - 1) {
			throw new NoChromatogramFilterSupplierAvailableException("There is no chromatogram filter available with the following id: " + index + ".");
		}
		IChromatogramFilterSupplier supplier = suppliers.get(index);
		return supplier.getId();
	}

	@Override
	public String[] getFilterNames() throws NoChromatogramFilterSupplierAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areChromatogramFiltersStored();
		/*
		 * If the ArrayList is not empty, return the registered chromatogram
		 * converter filter names.<br/>
		 */
		ArrayList<String> filterNames = new ArrayList<>();
		for(IChromatogramFilterSupplier supplier : suppliers) {
			filterNames.add(supplier.getFilterName());
		}
		return filterNames.toArray(new String[filterNames.size()]);
	}

	@Override
	public IChromatogramFilterSupplier getFilterSupplier(String filterId) throws NoChromatogramFilterSupplierAvailableException {

		IChromatogramFilterSupplier filterSupplier = null;
		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areChromatogramFiltersStored();
		if(filterId == null || filterId.equals("")) {
			throw new NoChromatogramFilterSupplierAvailableException("There is no chromatogram filter supplier available with the following id: " + filterId + ".");
		}
		endsearch:
		for(IChromatogramFilterSupplier supplier : suppliers) {
			if(supplier.getId().equals(filterId)) {
				filterSupplier = supplier;
				break endsearch;
			}
		}
		if(filterSupplier == null) {
			throw new NoChromatogramFilterSupplierAvailableException("There is no chromatogram filter supplier available with the following id: " + filterId + ".");
		} else {
			return filterSupplier;
		}
	}

	private void areChromatogramFiltersStored() throws NoChromatogramFilterSupplierAvailableException {

		if(suppliers.isEmpty()) {
			throw new NoChromatogramFilterSupplierAvailableException();
		}
	}
}
