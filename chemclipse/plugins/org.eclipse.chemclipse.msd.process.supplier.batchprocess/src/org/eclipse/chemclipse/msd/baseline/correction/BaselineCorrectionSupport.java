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
package org.eclipse.chemclipse.msd.baseline.correction;

import java.util.ArrayList;
import java.util.List;

public class BaselineCorrectionSupport implements IBaselineCorrectionSupport {

	private List<IBaselineCorrectionSupplier> suppliers;

	public BaselineCorrectionSupport() {
		suppliers = new ArrayList<IBaselineCorrectionSupplier>();
	}

	/**
	 * Adds a {@link IBaselineCorrectionSupplier} to the {@link BaselineCorrectionSupport}.
	 * 
	 * @param supplier
	 */
	protected void add(IBaselineCorrectionSupplier supplier) {

		suppliers.add(supplier);
	}

	@Override
	public List<String> getAvailableCorrectionIds() throws NoBaselineCorrectionAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areCorrectionsStored();
		List<String> availableCorrections = new ArrayList<String>();
		for(IBaselineCorrectionSupplier supplier : suppliers) {
			availableCorrections.add(supplier.getId());
		}
		return availableCorrections;
	}

	@Override
	public IBaselineCorrectionSupplier getBaselineCorrectionSupplier(String detectorId) throws NoBaselineCorrectionAvailableException {

		IBaselineCorrectionSupplier detectorSupplier = null;
		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areCorrectionsStored();
		if(detectorId == null || detectorId.equals("")) {
			throw new NoBaselineCorrectionAvailableException("There is no baseline detector available with the following id: " + detectorId + ".");
		}
		endsearch:
		for(IBaselineCorrectionSupplier supplier : suppliers) {
			if(supplier.getId().equals(detectorId)) {
				detectorSupplier = supplier;
				break endsearch;
			}
		}
		if(detectorSupplier == null) {
			throw new NoBaselineCorrectionAvailableException("There is no baseline detector available with the following id: " + detectorId + ".");
		} else {
			return detectorSupplier;
		}
	}

	@Override
	public String getCorrectionId(int index) throws NoBaselineCorrectionAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areCorrectionsStored();
		/*
		 * Test if the index is out of range.
		 */
		if(index < 0 || index > suppliers.size() - 1) {
			throw new NoBaselineCorrectionAvailableException("There is no baseline detector available with the following id: " + index + ".");
		}
		IBaselineCorrectionSupplier supplier = suppliers.get(index);
		return supplier.getId();
	}

	@Override
	public String[] getCorrectionNames() throws NoBaselineCorrectionAvailableException {

		/*
		 * Test if the suppliers ArrayList is empty.
		 */
		areCorrectionsStored();
		/*
		 * If the ArrayList is not empty, return the registered mass spectrum
		 * converter filter names.<br/>
		 */
		ArrayList<String> detectorNames = new ArrayList<String>();
		for(IBaselineCorrectionSupplier supplier : suppliers) {
			detectorNames.add(supplier.getDetectorName());
		}
		return detectorNames.toArray(new String[detectorNames.size()]);
	}

	// -------------------------------------private methods
	private void areCorrectionsStored() throws NoBaselineCorrectionAvailableException {

		if(suppliers.size() < 1) {
			throw new NoBaselineCorrectionAvailableException();
		}
	}
	// -------------------------------------private methods
}
