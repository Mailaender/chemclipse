/*******************************************************************************
 * Copyright (c) 2016 Lablicate UG (haftungsbeschränkt).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.msd.identifier.settings;

public abstract class AbstractDatabaseIdentifierSettings extends AbstractIdentifierSettings implements IDatabaseIdentifierSettings {

	private float retentionIndexWindowForDatabase;
	private float retentionTimeWindowForDatabase;
	private String forceMatchFactorPenaltyCalculationForDatabase;

	@Override
	public String getForceMatchFactorPenaltyCalculationForDatabase() {

		return forceMatchFactorPenaltyCalculationForDatabase;
	}

	@Override
	public void setForceMatchFactorPenaltyCalculationForDatabase(String forceMatchFactorPenaltyCalculationForDatabase) {

		this.forceMatchFactorPenaltyCalculationForDatabase = forceMatchFactorPenaltyCalculationForDatabase;
	}

	@Override
	public float getRetentionIndexWindowForDatabase() {

		return retentionIndexWindowForDatabase;
	}

	@Override
	public void setRetentionIndexWindowForDatabase(float retentionIndexWindowForDatabase) {

		this.retentionIndexWindowForDatabase = retentionIndexWindowForDatabase;
	}

	@Override
	public float getRetentionTimeWindowForDatabase() {

		return retentionTimeWindowForDatabase;
	}

	@Override
	public void setRetentionTimeWindowForDatabase(float retentionTimeWindowForDatabase) {

		this.retentionTimeWindowForDatabase = retentionTimeWindowForDatabase;
	}
}
