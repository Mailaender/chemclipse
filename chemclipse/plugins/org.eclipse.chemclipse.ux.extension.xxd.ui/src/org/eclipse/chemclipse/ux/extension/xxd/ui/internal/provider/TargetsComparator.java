/*******************************************************************************
 * Copyright (c) 2012, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.support.ui.swt.AbstractRecordTableComparator;
import org.eclipse.chemclipse.support.ui.swt.IRecordTableComparator;
import org.eclipse.jface.viewers.Viewer;

public class TargetsComparator extends AbstractRecordTableComparator implements IRecordTableComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		int sortOrder = 0;
		if(e1 instanceof IIdentificationTarget && e2 instanceof IIdentificationTarget) {
			//
			IIdentificationTarget entry1 = (IIdentificationTarget)e1;
			IIdentificationTarget entry2 = (IIdentificationTarget)e2;
			ILibraryInformation libraryInformation1 = entry1.getLibraryInformation();
			IComparisonResult comparisonResult1 = entry1.getComparisonResult();
			ILibraryInformation libraryInformation2 = entry2.getLibraryInformation();
			IComparisonResult comparisonResult2 = entry2.getComparisonResult();
			//
			switch(getPropertyIndex()) {
				case 0:
					sortOrder = Boolean.compare(entry2.isManuallyVerified(), entry1.isManuallyVerified());
					if(sortOrder == 0) {
						sortOrder = getAdditionalSortOrder(comparisonResult1, comparisonResult2);
					}
					break;
				case 1: // Rating
					sortOrder = Float.compare(comparisonResult2.getRating(), comparisonResult1.getRating());
					if(sortOrder == 0) {
						sortOrder = getAdditionalSortOrder(comparisonResult1, comparisonResult2);
					}
					break;
				case 2: // Name
					sortOrder = libraryInformation2.getName().compareTo(libraryInformation1.getName());
					break;
				case 3: // CAS
					sortOrder = libraryInformation2.getCasNumber().compareTo(libraryInformation1.getCasNumber());
					break;
				case 4: // Match Factor
					sortOrder = Float.compare(comparisonResult2.getMatchFactor(), comparisonResult1.getMatchFactor());
					break;
				case 5: // Reverse Match Factor
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactor(), comparisonResult1.getReverseMatchFactor());
					break;
				case 6: // Match Factor Direct
					sortOrder = Float.compare(comparisonResult2.getMatchFactorDirect(), comparisonResult1.getMatchFactorDirect());
					break;
				case 7: // Reverse Match Factor Direct
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactorDirect(), comparisonResult1.getReverseMatchFactorDirect());
					break;
				case 8: // Probability
					sortOrder = Float.compare(comparisonResult2.getProbability(), comparisonResult1.getProbability());
					break;
				case 9: // Formula
					sortOrder = libraryInformation2.getFormula().compareTo(libraryInformation1.getFormula());
					break;
				case 10: // Smiles
					sortOrder = libraryInformation2.getSmiles().compareTo(libraryInformation1.getSmiles());
					break;
				case 11: // InChI
					sortOrder = libraryInformation2.getInChI().compareTo(libraryInformation1.getInChI());
					break;
				case 12: // Mol Weight
					sortOrder = Double.compare(libraryInformation2.getMolWeight(), libraryInformation1.getMolWeight());
					break;
				case 13: // Advise
					String advise2 = comparisonResult2.getAdvise();
					String advise1 = comparisonResult1.getAdvise();
					if(advise2 != null && advise1 != null) {
						sortOrder = comparisonResult2.getAdvise().compareTo(comparisonResult1.getAdvise());
					}
					break;
				case 14: // Identifier
					sortOrder = entry2.getIdentifier().compareTo(entry1.getIdentifier());
					break;
				case 15: // Miscellaneous
					sortOrder = libraryInformation2.getMiscellaneous().compareTo(libraryInformation1.getMiscellaneous());
					break;
				case 16: // Comments
					sortOrder = libraryInformation2.getComments().compareTo(libraryInformation1.getComments());
					break;
				case 17:
					sortOrder = libraryInformation2.getDatabase().compareTo(libraryInformation1.getDatabase());
					break;
				case 18:
					sortOrder = libraryInformation2.getContributor().compareTo(libraryInformation1.getContributor());
					break;
				case 19:
					sortOrder = libraryInformation2.getReferenceIdentifier().compareTo(libraryInformation1.getReferenceIdentifier());
					break;
				case 20:
					sortOrder = Integer.compare(libraryInformation2.getRetentionTime(), libraryInformation1.getRetentionTime());
					break;
				case 21:
					sortOrder = Float.compare(libraryInformation2.getRetentionIndex(), libraryInformation1.getRetentionIndex());
					break;
				default:
					sortOrder = 0;
			}
		}
		if(getDirection() == ASCENDING) {
			sortOrder = -sortOrder;
		}
		return sortOrder;
	}

	/**
	 * Calculates the additional sort order by MF, RMF, MFD, RMFD and Probability
	 * 
	 * @param comparisonResult1
	 * @param comparisonResult2
	 * @return int
	 */
	private int getAdditionalSortOrder(IComparisonResult comparisonResult1, IComparisonResult comparisonResult2) {

		/*
		 * Match Factor
		 */
		int sortOrder = Float.compare(comparisonResult2.getMatchFactor(), comparisonResult1.getMatchFactor());
		if(sortOrder == 0) {
			/*
			 * Reverse Match Factor
			 */
			sortOrder = Float.compare(comparisonResult2.getReverseMatchFactor(), comparisonResult1.getReverseMatchFactor());
			if(sortOrder == 0) {
				/*
				 * Match Factor Direct
				 */
				sortOrder = Float.compare(comparisonResult2.getMatchFactorDirect(), comparisonResult1.getMatchFactorDirect());
				if(sortOrder == 0) {
					/*
					 * Reverse Match Factor
					 */
					sortOrder = Float.compare(comparisonResult2.getReverseMatchFactorDirect(), comparisonResult1.getReverseMatchFactorDirect());
					if(sortOrder == 0) {
						/*
						 * Probability
						 */
						sortOrder = Float.compare(comparisonResult2.getProbability(), comparisonResult1.getProbability());
					}
				}
			}
		}
		return sortOrder;
	}
}
