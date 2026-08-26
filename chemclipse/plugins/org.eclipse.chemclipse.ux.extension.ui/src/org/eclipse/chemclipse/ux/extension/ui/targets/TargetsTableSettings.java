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
package org.eclipse.chemclipse.ux.extension.ui.targets;

/**
 * Default settings of the targets table, used when the hosting bundle doesn't
 * supply its own. The values match the defaults of the targets preference page.
 */
public class TargetsTableSettings implements ITargetsTableSettings {

	private static final boolean DEF_SHOW_DEVIATION_RETENTION_TIME = false;
	private static final boolean DEF_SHOW_DEVIATION_RETENTION_INDEX = false;
	private static final boolean DEF_RESOLVE_DATABASE_UUID = false;
	private static final boolean DEF_USE_ABSOLUTE_DEVIATION_RETENTION_TIME = false;
	private static final boolean DEF_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX = false;
	private static final double DEF_RETENTION_TIME_DEVIATION_REL_OK = 20.0d;
	private static final double DEF_RETENTION_TIME_DEVIATION_REL_WARN = 40.0d;
	private static final double DEF_RETENTION_TIME_DEVIATION_ABS_OK = 1000.0d;
	private static final double DEF_RETENTION_TIME_DEVIATION_ABS_WARN = 2000.0d;
	private static final double DEF_RETENTION_INDEX_DEVIATION_REL_OK = 20.0d;
	private static final double DEF_RETENTION_INDEX_DEVIATION_REL_WARN = 40.0d;
	private static final double DEF_RETENTION_INDEX_DEVIATION_ABS_OK = 20.0d;
	private static final double DEF_RETENTION_INDEX_DEVIATION_ABS_WARN = 40.0d;

	@Override
	public boolean isShowDeviationRetentionTime() {

		return DEF_SHOW_DEVIATION_RETENTION_TIME;
	}

	@Override
	public boolean isShowDeviationRetentionIndex() {

		return DEF_SHOW_DEVIATION_RETENTION_INDEX;
	}

	@Override
	public boolean isResolveDatabaseUUID() {

		return DEF_RESOLVE_DATABASE_UUID;
	}

	@Override
	public boolean isUseAbsoluteDeviationRetentionTime() {

		return DEF_USE_ABSOLUTE_DEVIATION_RETENTION_TIME;
	}

	@Override
	public boolean isUseAbsoluteDeviationRetentionIndex() {

		return DEF_USE_ABSOLUTE_DEVIATION_RETENTION_INDEX;
	}

	@Override
	public double getRetentionTimeDeviationOK(boolean absolute) {

		return absolute ? DEF_RETENTION_TIME_DEVIATION_ABS_OK : DEF_RETENTION_TIME_DEVIATION_REL_OK;
	}

	@Override
	public double getRetentionTimeDeviationWarn(boolean absolute) {

		return absolute ? DEF_RETENTION_TIME_DEVIATION_ABS_WARN : DEF_RETENTION_TIME_DEVIATION_REL_WARN;
	}

	@Override
	public double getRetentionIndexDeviationOK(boolean absolute) {

		return absolute ? DEF_RETENTION_INDEX_DEVIATION_ABS_OK : DEF_RETENTION_INDEX_DEVIATION_REL_OK;
	}

	@Override
	public double getRetentionIndexDeviationWarn(boolean absolute) {

		return absolute ? DEF_RETENTION_INDEX_DEVIATION_ABS_WARN : DEF_RETENTION_INDEX_DEVIATION_REL_WARN;
	}
}
