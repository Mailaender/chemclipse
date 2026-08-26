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
 * Supplies the display settings of the targets table.
 *
 * The targets table is shared by several bundles. Each of them owns its
 * preferences, hence the settings are supplied by the hosting bundle instead of
 * being read from a preference store directly.
 */
public interface ITargetsTableSettings {

	boolean isShowDeviationRetentionTime();

	boolean isShowDeviationRetentionIndex();

	boolean isResolveDatabaseUUID();

	boolean isUseAbsoluteDeviationRetentionTime();

	boolean isUseAbsoluteDeviationRetentionIndex();

	/**
	 * Deviation up to which the retention time is rated as OK.
	 *
	 * @param absolute
	 * @return double
	 */
	double getRetentionTimeDeviationOK(boolean absolute);

	/**
	 * Deviation up to which the retention time is rated as a warning.
	 *
	 * @param absolute
	 * @return double
	 */
	double getRetentionTimeDeviationWarn(boolean absolute);

	/**
	 * Deviation up to which the retention index is rated as OK.
	 *
	 * @param absolute
	 * @return double
	 */
	double getRetentionIndexDeviationOK(boolean absolute);

	/**
	 * Deviation up to which the retention index is rated as a warning.
	 *
	 * @param absolute
	 * @return double
	 */
	double getRetentionIndexDeviationWarn(boolean absolute);
}
