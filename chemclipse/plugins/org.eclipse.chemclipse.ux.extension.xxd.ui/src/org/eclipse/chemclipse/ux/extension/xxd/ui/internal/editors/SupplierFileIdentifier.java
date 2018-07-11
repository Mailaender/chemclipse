/*******************************************************************************
 * Copyright (c) 2018 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.ux.extension.xxd.ui.internal.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.chemclipse.converter.core.ISupplier;
import org.eclipse.chemclipse.converter.sequence.SequenceConverter;
import org.eclipse.chemclipse.csd.converter.chromatogram.ChromatogramConverterCSD;
import org.eclipse.chemclipse.msd.converter.chromatogram.ChromatogramConverterMSD;
import org.eclipse.chemclipse.nmr.converter.core.ScanConverterNMR;
import org.eclipse.chemclipse.pcr.converter.core.PlateConverterPCR;
import org.eclipse.chemclipse.ux.extension.ui.provider.AbstractSupplierFileIdentifier;
import org.eclipse.chemclipse.ux.extension.ui.provider.ISupplierFileIdentifier;
import org.eclipse.chemclipse.ux.extension.xxd.ui.part.support.DataType;
import org.eclipse.chemclipse.wsd.converter.chromatogram.ChromatogramConverterWSD;
import org.eclipse.chemclipse.xir.converter.core.ScanConverterXIR;

public class SupplierFileIdentifier extends AbstractSupplierFileIdentifier implements ISupplierFileIdentifier {

	private String type = "";

	public SupplierFileIdentifier(DataType dataType) {
		super(getSupplier(dataType));
		initialize(dataType);
	}

	private static List<ISupplier> getSupplier(DataType dataType) {

		List<ISupplier> supplier = new ArrayList<ISupplier>();
		switch(dataType) {
			case MSD_NOMINAL:
			case MSD_TANDEM:
			case MSD_HIGHRES:
			case MSD:
				supplier = ChromatogramConverterMSD.getChromatogramConverterSupport().getSupplier();
				break;
			case CSD:
				supplier = ChromatogramConverterCSD.getChromatogramConverterSupport().getSupplier();
				break;
			case WSD:
				supplier = ChromatogramConverterWSD.getChromatogramConverterSupport().getSupplier();
				break;
			case XIR:
				supplier = ScanConverterXIR.getScanConverterSupport().getSupplier();
				break;
			case NMR:
				supplier = ScanConverterNMR.getScanConverterSupport().getSupplier();
				break;
			case PCR:
				supplier = PlateConverterPCR.getScanConverterSupport().getSupplier();
				break;
			case SEQ:
				supplier = SequenceConverter.getSequenceConverterSupport().getSupplier();
				break;
			default:
				// No action
		}
		//
		return supplier;
	}

	private void initialize(DataType dataType) {

		switch(dataType) {
			case MSD_NOMINAL:
			case MSD_TANDEM:
			case MSD_HIGHRES:
			case MSD:
				type = TYPE_MSD;
				break;
			case CSD:
				type = TYPE_CSD;
				break;
			case WSD:
				type = TYPE_WSD;
				break;
			case XIR:
				type = TYPE_XIR;
				break;
			case NMR:
				type = TYPE_NMR;
				break;
			case PCR:
				type = TYPE_PCR;
				break;
			case SEQ:
				type = TYPE_SEQ;
				break;
			default:
				type = "";
		}
	}

	@Override
	public String getType() {

		return type;
	}
}
