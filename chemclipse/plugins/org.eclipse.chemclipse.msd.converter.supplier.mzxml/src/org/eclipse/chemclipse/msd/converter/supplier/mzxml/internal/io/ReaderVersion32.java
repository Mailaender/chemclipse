/*******************************************************************************
 * Copyright (c) 2015, 2019 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.AbundanceLimitExceededException;
import org.eclipse.chemclipse.msd.converter.io.IChromatogramMSDReader;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v32.model.MsRun;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v32.model.Peaks;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.internal.v32.model.Scan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.IVendorScan;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorChromatogram;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorIon;
import org.eclipse.chemclipse.msd.converter.supplier.mzxml.model.VendorScan;
import org.eclipse.chemclipse.msd.model.core.AbstractIon;
import org.eclipse.chemclipse.msd.model.core.IChromatogramMSD;
import org.eclipse.chemclipse.msd.model.exceptions.IonLimitExceededException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReaderVersion32 extends AbstractReaderVersion implements IChromatogramMSDReader {

	private static final Logger logger = Logger.getLogger(ReaderVersion32.class);
	private static final int ION_PRECISION = 4;
	private String contextPath;

	public ReaderVersion32(String contextPath) {
		this.contextPath = contextPath;
	}

	@Override
	public IChromatogramMSD read(File file, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		IVendorChromatogram chromatogram = null;
		//
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList nodeList = document.getElementsByTagName(IConstants.NODE_MS_RUN);
			//
			JAXBContext jaxbContext = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			MsRun msrun = (MsRun)unmarshaller.unmarshal(nodeList.item(0));
			//
			chromatogram = new VendorChromatogram();
			// IIonTransitionSettings ionTransitionSettings = chromatogram.getIonTransitionSettings();
			boolean isTandemMeasurement = isTandemMeasurement(msrun);
			int cycleNumber = isTandemMeasurement ? 1 : 0;
			//
			for(Scan scan : msrun.getScan()) {
				/*
				 * Get the mass spectra.
				 */
				IVendorScan massSpectrum = new VendorScan();
				int retentionTime = scan.getRetentionTime().multiply(1000).getSeconds(); // milliseconds
				// MS, MS/MS
				short msLevel = scan.getMsLevel().shortValue();
				massSpectrum.setMassSpectrometer(msLevel);
				// float collisionEnergy = 0.0f;
				// double filter1Ion = 0.0d;
				//
				if(msLevel >= 2) {
					// collisionEnergy = scan.getCollisionEnergy();
					// if(scan.getPrecursorMz().size() >= 1) {
					// filter1Ion = AbstractIon.getIon(scan.getPrecursorMz().get(0).getValue(), 2);
					// }
				} else {
					cycleNumber++;
				}
				//
				if(cycleNumber >= 1) {
					massSpectrum.setCycleNumber(cycleNumber);
				}
				massSpectrum.setRetentionTime(retentionTime);
				/*
				 * Get the ions.
				 */
				for(Peaks peaks : scan.getPeaks()) {
					ByteBuffer byteBuffer = ByteBuffer.wrap(peaks.getValue());
					/*
					 * Compression
					 */
					String compressionType = peaks.getCompressionType();
					if(compressionType != null && compressionType.toLowerCase().equals("zlib")) {
						Inflater inflater = new Inflater();
						inflater.setInput(byteBuffer.array());
						byte[] byteArray = new byte[byteBuffer.capacity() * 10];
						byteBuffer = ByteBuffer.wrap(byteArray, 0, inflater.inflate(byteArray));
					}
					/*
					 * Byte Order
					 */
					String byteOrder = peaks.getByteOrder();
					if(byteOrder != null && byteOrder.equals("network")) {
						byteBuffer.order(ByteOrder.BIG_ENDIAN);
					} else {
						byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
					}
					/*
					 * Precision
					 */
					double[] values;
					BigInteger precision = peaks.getPrecision();
					if(precision != null && precision.intValue() == 64) {
						DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
						values = new double[doubleBuffer.capacity()];
						for(int index = 0; index < doubleBuffer.capacity(); index++) {
							values[index] = new Double(doubleBuffer.get(index));
						}
					} else {
						FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
						values = new double[floatBuffer.capacity()];
						for(int index = 0; index < floatBuffer.capacity(); index++) {
							values[index] = new Double(floatBuffer.get(index));
						}
					}
					//
					for(int peakIndex = 0; peakIndex < values.length - 1; peakIndex += 2) {
						/*
						 * Get m/z and intensity (m/z-int)
						 */
						double mz = AbstractIon.getIon(values[peakIndex], ION_PRECISION);
						float intensity = (float)values[peakIndex + 1];
						try {
							if(intensity >= VendorIon.MIN_ABUNDANCE && intensity <= VendorIon.MAX_ABUNDANCE) {
								// if(msLevel >= 2) {
								// try {
								// double filter3Ion = mz; // daughter m/z start
								// double filter1Resolution = 1.0d; // q1 resolution
								// double filter3Resolution = 1.0d; // q3 resolution
								// int transitionGroup = 1; // transition group
								// IIonTransition ionTransition = ionTransitionSettings.getIonTransition(filter1Ion, filter3Ion, collisionEnergy, filter1Resolution, filter3Resolution, transitionGroup);
								// massSpectrum.addIon(new VendorIon(mz, intensity, ionTransition));
								// } catch(IonTransitionIsNullException e) {
								// logger.warn(e);
								// }
								// } else {
								// massSpectrum.addIon(new VendorIon(mz, intensity));
								// }
								massSpectrum.addIon(new VendorIon(mz, intensity));
							}
						} catch(AbundanceLimitExceededException e) {
							logger.warn(e);
						} catch(IonLimitExceededException e) {
							logger.warn(e);
						}
					}
				}
				chromatogram.addScan(massSpectrum);
			}
		} catch(SAXException e) {
			logger.warn(e);
		} catch(JAXBException e) {
			logger.warn(e);
		} catch(ParserConfigurationException e) {
			logger.warn(e);
		} catch(DataFormatException e) {
			logger.warn(e);
		}
		//
		chromatogram.setConverterId("");
		chromatogram.setFile(file);
		return chromatogram;
	}

	private boolean isTandemMeasurement(MsRun msrun) {

		for(Scan scan : msrun.getScan()) {
			if(scan.getMsLevel().shortValue() > 1) {
				return true;
			}
		}
		return false;
	}
}
