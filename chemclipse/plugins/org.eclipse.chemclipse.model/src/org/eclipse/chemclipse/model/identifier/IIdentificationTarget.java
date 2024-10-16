/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.chemclipse.model.targets.ITarget;

public interface IIdentificationTarget extends ITarget {

	/**
	 * Returns the best matching identification target or
	 * null if none is available.
	 * 
	 * @param targets
	 * @param comparator
	 * @return {@link IIdentificationTarget}
	 */
	static IIdentificationTarget getBestIdentificationTarget(Set<IIdentificationTarget> targets, Comparator<IIdentificationTarget> comparator) {

		IIdentificationTarget identificationTarget = null;
		if(targets != null && targets.size() > 0) {
			if(targets.size() == 1) {
				identificationTarget = targets.iterator().next();
			} else {
				List<IIdentificationTarget> targetsList = new ArrayList<>(targets);
				if(comparator != null) {
					Collections.sort(targetsList, comparator);
				}
				identificationTarget = targetsList.get(0);
			}
		}
		return identificationTarget;
	}

	/**
	 * Returns the best matching library information or
	 * null if none is available.
	 * 
	 * @param targets
	 * @param comparator
	 * @return {@link ILibraryInformation}
	 */
	static ILibraryInformation getBestLibraryInformation(Set<IIdentificationTarget> targets, Comparator<IIdentificationTarget> comparator) {

		IIdentificationTarget identificationTarget = getBestIdentificationTarget(targets, comparator);
		return (identificationTarget != null) ? identificationTarget.getLibraryInformation() : null;
	}

	/**
	 * Returns the library information instance.
	 * 
	 * @return {@link ILibraryInformation}
	 */
	ILibraryInformation getLibraryInformation();

	/**
	 * Returns the comparison result.
	 * 
	 * @return {@link IComparisonResult}
	 */
	IComparisonResult getComparisonResult();

	/**
	 * Get the identifier (e.g. "INCOS Mass Spectrum Identifier").
	 * 
	 * @return String
	 */
	String getIdentifier();

	/**
	 * Set the identifier.
	 * 
	 * @param identifier
	 */
	void setIdentifier(String identifier);

	/**
	 * Returns if this result has been verified manually.
	 * 
	 * @return boolean
	 */
	boolean isManuallyVerified();

	/**
	 * Set to verify this result manually.
	 * 
	 * @param manuallyVerified
	 */
	void setManuallyVerified(boolean manuallyVerified);
}
