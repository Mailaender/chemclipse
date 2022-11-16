/*******************************************************************************
 * Copyright (c) 2017, 2022 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jan Holy - initial API and implementation
 * Philip Wenig - feature selection
 *******************************************************************************/
package org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.core.filters;

import java.util.Collection;
import java.util.List;

import org.eclipse.chemclipse.chromatogram.xxd.process.supplier.pca.core.preprocessing.IPreprocessing;
import org.eclipse.chemclipse.model.statistics.IRetentionTime;
import org.eclipse.chemclipse.model.statistics.ISample;
import org.eclipse.chemclipse.model.statistics.ISamples;
import org.eclipse.chemclipse.model.statistics.IVariable;

public interface IFilter extends IPreprocessing {

	enum DataTypeProcessing {

		RAW_DATA("Raw Data"), //
		MODIFIED_DATA("Modified Data"), //
		VARIABLES("Variables");

		private String string;

		private DataTypeProcessing(String string) {

			this.string = string;
		}

		@Override
		public String toString() {

			return string;
		}
	}

	void setDataTypeProcessing(DataTypeProcessing processType);

	DataTypeProcessing getDataTypeProcessing();

	static String getErrorMessage(String messagge) {

		return "Error: " + messagge;
	}

	static String getNumberSelectedRow(Collection<Boolean> selection) {

		long countSelectedData = selection.stream().filter(b -> b).count();
		return Long.toString(countSelectedData);
	}

	static <V extends IVariable> boolean isRetentionTimes(List<V> variables) {

		return variables.stream().allMatch(v -> (v instanceof IRetentionTime));
	}

	<V extends IVariable, S extends ISample> List<Boolean> filter(ISamples<V, S> samples);

	String getSelectionResult();

	@Override
	boolean isOnlySelected();

	@Override
	default <V extends IVariable, S extends ISample> void process(ISamples<V, S> samples) {

		List<Boolean> result = filter(samples);
		List<V> variables = samples.getVariables();
		for(int j = 0; j < result.size(); j++) {
			variables.get(j).setSelected(variables.get(j).isSelected() && result.get(j));
		}
	}

	@Override
	void setOnlySelected(boolean onlySelected);
}