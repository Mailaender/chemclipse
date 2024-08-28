/*******************************************************************************
 * Copyright (c) 2017, 2024 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - refactoring
 *******************************************************************************/
package org.eclipse.chemclipse.xxd.process.supplier.pca.core.preprocessing;

import java.util.List;

import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.ISampleData;
import org.eclipse.chemclipse.model.statistics.ISamples;
import org.eclipse.chemclipse.model.statistics.IVariable;

public class TransformationPower extends AbstractDataModificator implements ITransformation {

	@Override
	public String getDescription() {

		return "Power Transformation";
	}

	@Override
	public String getName() {

		return "Power Transformation";
	}

	@Override
	public <V extends IVariable, S extends ISample> void process(ISamples<V, S> samples) {

		samples.getSamples().stream().filter(s -> s.isSelected() || !isOnlySelected()).forEach(s -> {
			List<? extends ISampleData<?>> sampleData = s.getSampleData();
			for(int i = 0; i < sampleData.size(); i++) {
				if(useVariable(samples, i)) {
					ISampleData<?> data = sampleData.get(i);
					data.setModifiedData(Math.sqrt(Math.abs(getData(data))));
				}
			}
		});
	}
}