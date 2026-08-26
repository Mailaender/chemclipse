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
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.provider;

import org.eclipse.chemclipse.ux.extension.ui.targets.ITargetsTableSettings;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.ux.extension.xxd.ui.preferences.PreferenceSupplier;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Supplies the targets table settings from the preferences of this bundle.
 */
public class TargetsTableSettingsXXD implements ITargetsTableSettings {

	private final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

	@Override
	public boolean isShowDeviationRetentionTime() {

		return preferenceStore.getBoolean(PreferenceSupplier.P_TARGETS_TABLE_SHOW_DEVIATION_RT);
	}

	@Override
	public boolean isShowDeviationRetentionIndex() {

		return preferenceStore.getBoolean(PreferenceSupplier.P_TARGETS_TABLE_SHOW_DEVIATION_RI);
	}

	@Override
	public boolean isResolveDatabaseUUID() {

		return PreferenceSupplier.isResolveDatabaseUUID();
	}

	@Override
	public boolean isUseAbsoluteDeviationRetentionTime() {

		return preferenceStore.getBoolean(PreferenceSupplier.P_USE_ABSOLUTE_DEVIATION_RETENTION_TIME);
	}

	@Override
	public boolean isUseAbsoluteDeviationRetentionIndex() {

		return preferenceStore.getBoolean(PreferenceSupplier.P_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX);
	}

	@Override
	public double getRetentionTimeDeviationOK(boolean absolute) {

		if(absolute) {
			return preferenceStore.getInt(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_ABS_OK);
		}

		return preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_REL_OK);
	}

	@Override
	public double getRetentionTimeDeviationWarn(boolean absolute) {

		if(absolute) {
			return preferenceStore.getInt(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_ABS_WARN);
		}

		return preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_TIME_DEVIATION_REL_WARN);
	}

	@Override
	public double getRetentionIndexDeviationOK(boolean absolute) {

		if(absolute) {
			return preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_ABS_OK);
		}

		return preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_REL_OK);
	}

	@Override
	public double getRetentionIndexDeviationWarn(boolean absolute) {

		if(absolute) {
			return preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_ABS_WARN);
		}

		return preferenceStore.getFloat(PreferenceSupplier.P_RETENTION_INDEX_DEVIATION_REL_WARN);
	}
}
