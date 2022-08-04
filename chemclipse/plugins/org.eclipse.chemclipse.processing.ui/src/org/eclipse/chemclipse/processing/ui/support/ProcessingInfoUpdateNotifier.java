/*******************************************************************************
 * Copyright (c) 2012, 2022 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - use {@link IMessageProvider} interface, make a creatable singleton and store the current value
 *******************************************************************************/
package org.eclipse.chemclipse.processing.ui.support;

import org.eclipse.chemclipse.processing.core.IMessageProvider;
import org.eclipse.chemclipse.processing.ui.Activator;
import org.eclipse.chemclipse.support.events.IChemClipseEvents;
import org.eclipse.e4.core.services.events.IEventBroker;

public class ProcessingInfoUpdateNotifier {

	private IMessageProvider messageProvider;

	public void update(IMessageProvider messageProvider) {

		this.messageProvider = messageProvider;
		IEventBroker eventBroker = Activator.getDefault().getEventBroker();
		if(eventBroker != null) {
			eventBroker.post(IChemClipseEvents.TOPIC_PROCESSING_INFO_UPDATE, messageProvider);
		}
	}

	public IMessageProvider getProcessingInfo() {

		return messageProvider;
	}
}
