/*******************************************************************************
 * Copyright (c) 2019, 2021 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Christoph Läubrich - initial API and implementation
 * Philip Wenig - refactorings
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.model.filter.peaks;

import java.util.Collection;
import java.util.Set;

import org.eclipse.chemclipse.model.comparator.IdentificationTargetComparator;
import org.eclipse.chemclipse.model.core.IPeak;
import org.eclipse.chemclipse.model.core.IPeakModel;
import org.eclipse.chemclipse.model.filter.IPeakFilter;
import org.eclipse.chemclipse.model.identifier.IIdentificationTarget;
import org.eclipse.chemclipse.model.identifier.ILibraryInformation;
import org.eclipse.chemclipse.processing.Processor;
import org.eclipse.chemclipse.processing.core.MessageConsumer;
import org.eclipse.chemclipse.processing.filter.CRUDListener;
import org.eclipse.chemclipse.processing.filter.Filter;
import org.eclipse.chemclipse.xxd.model.settings.peaks.ClassifiedPeaksFilterSettings;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.osgi.service.component.annotations.Component;

@Component(service = {IPeakFilter.class, Filter.class, Processor.class})
public class ClassifiedPeaksFilter implements IPeakFilter<ClassifiedPeaksFilterSettings> {

	@Override
	public String getName() {

		return "Disable Classified Peak(s)";
	}

	@Override
	public String getDescription() {

		return "Disables peak(s) with a given classification";
	}

	@Override
	public Class<ClassifiedPeaksFilterSettings> getConfigClass() {

		return ClassifiedPeaksFilterSettings.class;
	}

	@Override
	public <X extends IPeak> void filterIPeaks(CRUDListener<X, IPeakModel> listener, ClassifiedPeaksFilterSettings configuration, MessageConsumer messageConsumer, IProgressMonitor monitor) throws IllegalArgumentException {

		Collection<X> read = listener.read();
		if(configuration == null) {
			configuration = createConfiguration(read);
		}
		Set<String> classifications = configuration.getClassificationSet();
		SubMonitor subMonitor = SubMonitor.convert(monitor, read.size());
		for(X peak : read) {
			Set<IIdentificationTarget> targets = peak.getTargets();
			float retentionIndex = peak.getPeakModel().getPeakMaximum().getRetentionIndex();
			IdentificationTargetComparator identificationTargetComparator = new IdentificationTargetComparator(retentionIndex);
			IIdentificationTarget target = IIdentificationTarget.getBestIdentificationTarget(targets, identificationTargetComparator);
			if(target != null) {
				ILibraryInformation information = target.getLibraryInformation();
				if(information != null) {
					boolean disable;
					if(classifications.isEmpty()) {
						disable = information.getClassifier().size() > 0;
					} else {
						disable = false;
						for(String classifier : information.getClassifier()) {
							if(classifications.contains(classifier)) {
								disable = true;
								break;
							}
						}
					}
					if(disable) {
						peak.setActiveForAnalysis(false);
						listener.updated(peak);
					}
				}
			}
			subMonitor.worked(1);
		}
	}

	@Override
	public boolean acceptsIPeaks(Collection<? extends IPeak> items) {

		return true;
	}
}
