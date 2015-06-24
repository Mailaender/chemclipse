/*******************************************************************************
 * Copyright (c) 2011, 2015 Philip (eselmeister) Wenig.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Philip (eselmeister) Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.ui.views;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.chemclipse.msd.model.core.IChromatogramPeakMSD;
import org.eclipse.chemclipse.msd.model.core.selection.IChromatogramSelectionMSD;
import org.eclipse.chemclipse.chromatogram.xxd.peak.detector.supplier.manual.ui.swt.ManualDetectedPeakUI;
import org.eclipse.chemclipse.ux.extension.msd.ui.views.AbstractChromatogramAndPeakSelectionView;

public class ManualDetectedPeakMSDView extends AbstractChromatogramAndPeakSelectionView {

	@Inject
	private Composite parent;
	private ManualDetectedPeakUI selectedPeakUI;

	@Inject
	public ManualDetectedPeakMSDView(EPartService partService, MPart part, IEventBroker eventBroker) {

		super(part, partService, eventBroker);
	}

	@PostConstruct
	private void createControl() {

		parent.setLayout(new FillLayout());
		selectedPeakUI = new ManualDetectedPeakUI(parent, SWT.NONE);
	}

	@PreDestroy
	private void preDestroy() {

		unsubscribe();
	}

	@Focus
	public void setFocus() {

		selectedPeakUI.setFocus();
		update(getChromatogramSelection(), getChromatogramPeak(), false);
	}

	@Override
	public void update(IChromatogramSelectionMSD chromatogramSelection, IChromatogramPeakMSD chromatogramPeak, boolean forceReload) {

		/*
		 * Update the ui only if the actual view part is visible and the
		 * selection is not null.
		 */
		if(doUpdate(chromatogramSelection, chromatogramPeak)) {
			selectedPeakUI.update(chromatogramSelection, chromatogramPeak);
		}
	}
}
