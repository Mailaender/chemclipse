/*******************************************************************************
 * Copyright (c) 2020, 2022 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.filter.peaks.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class DeleteIntegrationsFilterSettings {

	@JsonProperty(value = "Delete Integrations", defaultValue = "false")
	@JsonPropertyDescription(value = "Confirm to delete the integrations.")
	private boolean deleteIntegrations;

	public boolean isDeleteIntegrations() {

		return deleteIntegrations;
	}

	public void setDeleteIntegrations(boolean deleteIntegrations) {

		this.deleteIntegrations = deleteIntegrations;
	}
}