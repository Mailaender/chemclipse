/*******************************************************************************
 * Copyright (c) 2008, 2023 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.identifier.settings;

import org.eclipse.chemclipse.model.identifier.IdentifierAdapterSettings;

/**
 * Default settings class, which sets all identifier settings values
 * to their defaults. Additionally, no JsonAnnotations are declared, so
 * that each identifier settings class, which don't need the underlying
 * settings, can re-use this default class.
 */
public abstract class ChromatogramIdentifierAdapterSettings extends IdentifierAdapterSettings implements IChromatogramIdentifierSettings {
}
