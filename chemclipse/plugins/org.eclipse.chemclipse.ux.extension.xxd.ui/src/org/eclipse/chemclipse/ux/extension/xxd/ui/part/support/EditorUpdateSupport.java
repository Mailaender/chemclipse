/*******************************************************************************
 * Copyright (c) 2017, 2023 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - Fix method for NMR
 * Matthias Mailänder - add support for MS
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.part.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.quantitation.IQuantitationDatabase;
import org.eclipse.chemclipse.model.selection.IChromatogramSelection;
import org.eclipse.chemclipse.msd.model.core.IScanMSD;
import org.eclipse.chemclipse.nmr.model.selection.IDataNMRSelection;
import org.eclipse.chemclipse.ux.extension.msd.ui.editors.IMassSpectrumEditor;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChromatogramEditor;
import org.eclipse.chemclipse.ux.extension.ui.editors.IChromatogramProjectEditor;
import org.eclipse.chemclipse.ux.extension.ui.editors.IQuantitationDatabaseEditor;
import org.eclipse.chemclipse.ux.extension.ui.editors.IScanEditorNMR;
import org.eclipse.chemclipse.ux.extension.ui.editors.IScanEditorXIR;
import org.eclipse.chemclipse.ux.extension.xxd.ui.Activator;
import org.eclipse.chemclipse.xir.model.core.IScanXIR;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityEditor;

@SuppressWarnings("restriction")
public class EditorUpdateSupport {

	private static final Logger logger = Logger.getLogger(EditorUpdateSupport.class);
	private EPartService partService = Activator.getDefault().getPartService();

	@SuppressWarnings("rawtypes")
	public IChromatogramSelection getActiveEditorSelection() {

		if(partService != null) {
			try {
				Collection<MPart> parts = partService.getParts();
				if(parts != null) {
					for(MPart part : parts) {
						if(part.isVisible()) {
							Object object = part.getObject();
							if(object instanceof IChromatogramEditor chromatogramEditor) {
								return chromatogramEditor.getChromatogramSelection();
							}
						}
					}
				}
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List<IChromatogramSelection> getChromatogramSelections() {

		List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();
		if(partService != null) {
			try {
				Collection<MPart> parts = partService.getParts();
				if(parts != null) {
					for(MPart part : parts) {
						chromatogramSelections.addAll(extractChromatogramSelections(part.getObject()));
					}
				}
			} catch(Exception e) {
				/*
				 * Error like "Application does not have an active window" occur if this method
				 * is called by a modal dialog. In such a case, try to resolve the chromatogram
				 * selections via the application.
				 */
				MApplication application = Activator.getDefault().getApplication();
				if(application != null) {
					EModelService service = Activator.getDefault().getModelService();
					if(service != null) {
						List<MPart> parts = service.findElements(application, null, MPart.class, null);
						if(parts != null) {
							for(MPart part : parts) {
								if(!extractChromatogramSelections(part.getObject()).isEmpty()) {
									chromatogramSelections.addAll(extractChromatogramSelections(part.getObject()));
								}
							}
						}
					}
				}
			}
		}
		/*
		 * If the window was null and there was no open editor, the list will
		 * contains 0 elements.
		 */
		return chromatogramSelections;
	}

	public List<IScanMSD> getMassSpectrumSelections() {

		List<IScanMSD> dataSelections = new ArrayList<>();
		if(partService != null) {
			/*
			 * TODO: see message
			 */
			try {
				Collection<MPart> parts = partService.getParts();
				for(MPart part : parts) {
					Object object = part.getObject();
					if(object != null) {
						/*
						 * MALDI
						 */
						IScanMSD selection = null;
						if(object instanceof IMassSpectrumEditor editor) {
							selection = editor.getScanSelection();
						}
						//
						if(selection != null) {
							dataSelections.add(selection);
						}
					}
				}
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		/*
		 * If the window was null and there was no open editor, the list will
		 * contains 0 elements.
		 */
		return dataSelections;
	}

	public List<IScanXIR> getScanSelectionsXIR() {

		List<IScanXIR> dataNMRSelections = new ArrayList<>();
		if(partService != null) {
			/*
			 * TODO: see message
			 */
			try {
				Collection<MPart> parts = partService.getParts();
				for(MPart part : parts) {
					Object object = part.getObject();
					if(object != null) {
						/*
						 * XIR
						 */
						IScanXIR selection = null;
						if(object instanceof IScanEditorXIR editor) {
							selection = editor.getScanSelection();
						}
						//
						if(selection != null) {
							dataNMRSelections.add(selection);
						}
					}
				}
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		/*
		 * If the window was null and there was no open editor, the list will
		 * contains 0 elements.
		 */
		return dataNMRSelections;
	}

	public List<IDataNMRSelection> getDataNMRSelections(EPartService partService) {

		List<IDataNMRSelection> scanSelections = new ArrayList<>();
		Collection<MPart> parts = partService.getParts();
		for(MPart part : parts) {
			Object object = part.getObject();
			if(object instanceof IScanEditorNMR editor) {
				IDataNMRSelection selection = editor.getScanSelection();
				if(selection != null) {
					scanSelections.add(selection);
				}
			}
		}
		return scanSelections;
	}

	public List<IQuantitationDatabase> getQuantitationDatabases() {

		List<IQuantitationDatabase> quantitationDatabases = new ArrayList<>();
		if(partService != null) {
			/*
			 * TODO: see message
			 */
			try {
				Collection<MPart> parts = partService.getParts();
				for(MPart part : parts) {
					Object object = part.getObject();
					if(object instanceof IQuantitationDatabaseEditor editor) {
						quantitationDatabases.add(editor.getQuantitationDatabase());
					}
				}
			} catch(Exception e) {
				logger.warn(e);
			}
		}
		/*
		 * If the window was null and there was no open editor, the list will
		 * contains 0 elements.
		 */
		return quantitationDatabases;
	}

	@SuppressWarnings("rawtypes")
	private void addChromatogramSelection(List<IChromatogramSelection> chromatogramSelections, IChromatogramSelection selection) {

		if(selection != null) {
			chromatogramSelections.add(selection);
		}
	}

	@SuppressWarnings("rawtypes")
	private void addChromatogramSelections(List<IChromatogramSelection> chromatogramSelections, List<IChromatogramSelection> selections) {

		if(selections != null && !selections.isEmpty()) {
			chromatogramSelections.addAll(selections);
		}
	}

	@SuppressWarnings("rawtypes")
	private List<IChromatogramSelection> extractChromatogramSelections(Object object) {

		List<IChromatogramSelection> chromatogramSelections = new ArrayList<>();
		//
		if(object != null) {
			/*
			 * MSD/CSD/WSD or specialized Editor
			 */
			if(object instanceof IChromatogramEditor chromatogramEditor) {
				addChromatogramSelection(chromatogramSelections, chromatogramEditor.getChromatogramSelection());
			} else if(object instanceof IChromatogramProjectEditor chromatogramProjectEditor) {
				addChromatogramSelections(chromatogramSelections, chromatogramProjectEditor.getChromatogramSelections());
			} else if(object instanceof CompatibilityEditor compatibilityEditor) {
				/*
				 * 3.x compatibility editor.
				 */
				IWorkbenchPart workbenchPart = compatibilityEditor.getPart();
				if(workbenchPart instanceof IChromatogramEditor chromatogramEditor) {
					addChromatogramSelection(chromatogramSelections, chromatogramEditor.getChromatogramSelection());
				} else if(workbenchPart instanceof IChromatogramProjectEditor chromatogramProjectEditor) {
					addChromatogramSelections(chromatogramSelections, chromatogramProjectEditor.getChromatogramSelections());
				}
			}
		}
		//
		return chromatogramSelections;
	}
}
