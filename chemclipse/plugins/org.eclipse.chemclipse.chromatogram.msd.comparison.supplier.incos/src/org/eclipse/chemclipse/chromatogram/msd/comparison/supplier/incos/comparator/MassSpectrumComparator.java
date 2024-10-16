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
package org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.incos.comparator;

import org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum.AbstractMassSpectrumComparator;
import org.eclipse.chemclipse.chromatogram.msd.comparison.massspectrum.IMassSpectrumComparator;
import org.eclipse.chemclipse.chromatogram.msd.comparison.math.GeometricDistanceCalculator;
import org.eclipse.chemclipse.chromatogram.msd.comparison.math.IMatchCalculator;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.AbundanceLimitExceededException;
import org.eclipse.chemclipse.model.identifier.ComparisonResult;
import org.eclipse.chemclipse.model.identifier.IComparisonResult;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.msd.model.exceptions.IonLimitExceededException;
import org.eclipse.chemclipse.msd.model.implementation.Ion;
import org.eclipse.chemclipse.msd.model.implementation.ScanMSD;
import org.eclipse.chemclipse.msd.model.xic.IExtractedIonSignal;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;

/**
 * This class gives back a IMassSpectrumComparisonResult which implements the
 * INCOS mass spectrum comparison algorithm.
 * 
 * @author eselmeister
 */
public class MassSpectrumComparator extends AbstractMassSpectrumComparator implements IMassSpectrumComparator {

	public static final String COMPARATOR_ID = "org.eclipse.chemclipse.chromatogram.msd.comparison.supplier.incos";
	private static final Logger logger = Logger.getLogger(MassSpectrumComparator.class);
	private static final int NORMALIZATION_FACTOR = 100;

	@Override
	public IProcessingInfo compare(IScanMSD unknown, IScanMSD reference) {

		IProcessingInfo processingInfo = super.validate(unknown, reference);
		if(!processingInfo.hasErrorMessages()) {
			IMatchCalculator geometricDistanceCalculator = new GeometricDistanceCalculator();
			IScanMSD unknownAdjusted = adjustMassSpectrum(unknown);
			IScanMSD referenceAdjusted = adjustMassSpectrum(reference);
			/*
			 * Match Factor, Reverse Match Factor
			 * Internally the match is normalized to 1, but a percentage value is used normally.
			 */
			IExtractedIonSignal signalU = unknownAdjusted.getExtractedIonSignal();
			IExtractedIonSignal signalR = referenceAdjusted.getExtractedIonSignal();
			//
			float matchFactor = geometricDistanceCalculator.calculate(unknownAdjusted, referenceAdjusted, signalU.getIonRange()) * 100;
			float reverseMatchFactor = geometricDistanceCalculator.calculate(referenceAdjusted, unknownAdjusted, signalR.getIonRange()) * 100;
			float matchFactorDirect = geometricDistanceCalculator.calculate(unknownAdjusted, referenceAdjusted) * 100;
			float reverseMatchFactorDirect = geometricDistanceCalculator.calculate(referenceAdjusted, unknownAdjusted) * 100;
			/*
			 * Result
			 */
			IComparisonResult massSpectrumComparisonResult = new ComparisonResult(matchFactor, reverseMatchFactor, matchFactorDirect, reverseMatchFactorDirect);
			processingInfo.setProcessingResult(massSpectrumComparisonResult);
		}
		return processingInfo;
	}

	/**
	 * This method will calculate new abundance values in the following manner:<br/>
	 * For each ion the new abundance will be set to:<br/>
	 * <br/>
	 * Inew = sqrt(I * Ion);<br/>
	 * <br/>
	 * Set the highest intensity value to 100 so that no problems will occur
	 * when you calculate the new abundance values. A new mass spectrum will be
	 * returned.
	 */
	private IScanMSD adjustMassSpectrum(IScanMSD massSpectrum) {

		IScanMSD adjustedMassSpectrum = new ScanMSD();
		IIon adjustedIon;
		/*
		 * Normalize the abundance values to a highest value of 100.
		 */
		try {
			/*
			 * Make a deep copy to not destroy the original mass spectrum.
			 */
			IScanMSD massSpectrumNormalized = massSpectrum.makeDeepCopy();
			/*
			 * Normalize the copied mass spectrum.
			 */
			massSpectrumNormalized.normalize(NORMALIZATION_FACTOR);
			IExtractedIonSignal signal;
			signal = massSpectrumNormalized.getExtractedIonSignal();
			/*
			 * Calculate the new abundance value.
			 */
			float abundance;
			int startIon = signal.getStartIon();
			int stopIon = signal.getStopIon();
			for(int ion = startIon; ion <= stopIon; ion++) {
				abundance = signal.getAbundance(ion);
				if(abundance > IIon.ZERO_INTENSITY) {
					/*
					 * Calculate the new abundance and add the ion to the
					 * fresh created mass spectrum.
					 */
					try {
						/*
						 * Inew = sqrt(I * Ion)
						 */
						adjustedIon = new Ion(ion, (float)Math.sqrt(abundance * ion));
						adjustedMassSpectrum.addIon(adjustedIon);
					} catch(AbundanceLimitExceededException e) {
						logger.warn(e);
					} catch(IonLimitExceededException e) {
						logger.warn(e);
					}
				}
			}
		} catch(CloneNotSupportedException e1) {
			logger.warn(e1);
		}
		return adjustedMassSpectrum;
	}
}
