/*******************************************************************************
 * Copyright (c) 2010, 2018 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.eclipse.chemclipse.converter.exceptions.FileIsEmptyException;
import org.eclipse.chemclipse.converter.exceptions.FileIsNotReadableException;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.internal.filter.BatchProcessJobEventFilter;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.internal.support.IBatchProcessJobTags;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.BatchProcessJob;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IBatchProcessJob;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumInputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumOutputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumProcessEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumReportSupplierEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.MassSpectrumInputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.MassSpectrumOutputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.MassSpectrumProcessEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.MassSpectrumReportSupplierEntry;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Matthias Mailänder
 * 
 */
public class BatchProcessJobReader implements IBatchProcessJobReader {

	@Override
	public IBatchProcessJob read(File file, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotReadableException, FileIsEmptyException, IOException {

		IBatchProcessJob batchProcessJob = new BatchProcessJob();
		try {
			readHeader(file, batchProcessJob);
			readMassSpectrumInputEntries(file, batchProcessJob, monitor);
			readMassSpectrumProcessEntries(file, batchProcessJob, monitor);
			readMassSpectrumOutputEntries(file, batchProcessJob, monitor);
			readMassSpectrumReportEntries(file, batchProcessJob, monitor);
		} catch(XMLStreamException e) {
			throw new IOException(e);
		}
		return batchProcessJob;
	}

	/**
	 * Reads the header information.
	 * 
	 * @throws IOException
	 */
	private void readHeader(File file, IBatchProcessJob batchProcessJob) throws XMLStreamException, IOException {

		/*
		 * Open the streams.
		 */
		XMLEventReader eventReader;
		XMLInputFactory.newInstance().setProperty(XMLInputFactory.IS_COALESCING, true);
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		eventReader = inputFactory.createXMLEventReader(bufferedInputStream, IBatchProcessJobTags.UTF8);
		XMLEvent event;
		String elementName;
		exitloop:
		while(eventReader.hasNext()) {
			event = eventReader.nextEvent();
			if(event.isStartElement()) {
				elementName = event.asStartElement().getName().getLocalPart();
				/*
				 * Read the header elements. There are none to read actually.
				 */
			} else {
				/*
				 * Check if it is the end of the header. If yes, break to not
				 * run through all elements of the xml document.
				 */
				if(event.isEndElement()) {
					elementName = event.asEndElement().getName().getLocalPart();
					if(elementName.equals(IBatchProcessJobTags.HEADER)) {
						break exitloop;
					}
				}
			}
		}
		/*
		 * Close the streams.
		 */
		eventReader.close();
		bufferedInputStream.close();
	}

	/**
	 * Read the mass spectrum input entries.
	 * 
	 * @param file
	 * @param batchProcessJob
	 * @param monitor
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private void readMassSpectrumInputEntries(File file, IBatchProcessJob batchProcessJob, IProgressMonitor monitor) throws XMLStreamException, IOException {

		IMassSpectrumInputEntry inputEntry;
		XMLEvent event;
		/*
		 * Open the streams.
		 */
		XMLEventReader eventReader;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		eventReader = inputFactory.createXMLEventReader(bufferedInputStream, IBatchProcessJobTags.UTF8);
		/*
		 * Use event filters.
		 */
		List<String> acceptedElements = new ArrayList<String>();
		acceptedElements.add(IBatchProcessJobTags.MASSSPECTRUM_INPUT_ENTRY);
		EventFilter eventFilter = new BatchProcessJobEventFilter(acceptedElements);
		XMLEventReader filteredEventReader = inputFactory.createFilteredReader(eventReader, eventFilter);
		/*
		 * Read all entries.
		 */
		while(filteredEventReader.hasNext()) {
			event = filteredEventReader.nextEvent();
			event = eventReader.nextEvent();
			inputEntry = new MassSpectrumInputEntry(event.asCharacters().getData());
			batchProcessJob.getMassSpectrumInputEntries().add(inputEntry);
		}
		/*
		 * Close the streams.
		 */
		eventReader.close();
		bufferedInputStream.close();
	}

	@SuppressWarnings("unchecked")
	private void readMassSpectrumProcessEntries(File file, IBatchProcessJob batchProcessJob, IProgressMonitor monitor) throws XMLStreamException, IOException {

		IMassSpectrumProcessEntry processEntry;
		String processCategory = null;
		String processorId = "";
		XMLEvent event;
		Attribute attribute;
		String attributeName;
		/*
		 * Open the streams.
		 */
		XMLEventReader eventReader;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		eventReader = inputFactory.createXMLEventReader(bufferedInputStream, IBatchProcessJobTags.UTF8);
		/*
		 * Use event filters.
		 */
		List<String> acceptedElements = new ArrayList<String>();
		acceptedElements.add(IBatchProcessJobTags.MASSSPECTRUM_PROCESS_ENTRY);
		EventFilter eventFilter = new BatchProcessJobEventFilter(acceptedElements);
		XMLEventReader filteredEventReader = inputFactory.createFilteredReader(eventReader, eventFilter);
		/*
		 * Read all entries.
		 */
		while(filteredEventReader.hasNext()) {
			event = filteredEventReader.nextEvent();
			Iterator<? extends Attribute> attributes = event.asStartElement().getAttributes();
			while(attributes.hasNext()) {
				attribute = attributes.next();
				attributeName = attribute.getName().getLocalPart();
				// Process type
				if(attributeName.equals(IBatchProcessJobTags.PROCESSOR_TYPE)) {
					processCategory = attribute.getValue();
				}
				// Processor id
				if(attributeName.equals(IBatchProcessJobTags.PROCESSOR_ID)) {
					processorId = attribute.getValue();
				}
			}
			processEntry = new MassSpectrumProcessEntry(processCategory, processorId);
			batchProcessJob.getMassSpectrumProcessEntries().add(processEntry);
		}
		/*
		 * Close the streams.
		 */
		eventReader.close();
		bufferedInputStream.close();
	}

	/**
	 * Reads the mass spectrum output entries.
	 * 
	 * @param file
	 * @param batchProcessJob
	 * @param monitor
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void readMassSpectrumOutputEntries(File file, IBatchProcessJob batchProcessJob, IProgressMonitor monitor) throws XMLStreamException, IOException {

		IMassSpectrumOutputEntry outputEntry;
		String outputFolder;
		String converterId = "";
		XMLEvent event;
		Attribute attribute;
		String attributeName;
		/*
		 * Open the streams.
		 */
		XMLEventReader eventReader;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		eventReader = inputFactory.createXMLEventReader(bufferedInputStream, IBatchProcessJobTags.UTF8);
		/*
		 * Use event filters.
		 */
		List<String> acceptedElements = new ArrayList<String>();
		acceptedElements.add(IBatchProcessJobTags.MASSSPECTRUM_OUTPUT_ENTRY);
		EventFilter eventFilter = new BatchProcessJobEventFilter(acceptedElements);
		XMLEventReader filteredEventReader = inputFactory.createFilteredReader(eventReader, eventFilter);
		/*
		 * Read all entries.
		 */
		while(filteredEventReader.hasNext()) {
			event = filteredEventReader.nextEvent();
			Iterator<? extends Attribute> attributes = event.asStartElement().getAttributes();
			while(attributes.hasNext()) {
				attribute = attributes.next();
				attributeName = attribute.getName().getLocalPart();
				// Get the converter id.
				if(attributeName.equals(IBatchProcessJobTags.MASSSPECTRUM_CONVERTER_ID)) {
					converterId = attribute.getValue();
				}
			}
			// Get the output file.
			event = eventReader.nextEvent();
			outputFolder = event.asCharacters().getData();
			outputEntry = new MassSpectrumOutputEntry(outputFolder, converterId);
			batchProcessJob.getMassSpectrumOutputEntries().add(outputEntry);
		}
		/*
		 * Close the streams.
		 */
		eventReader.close();
		bufferedInputStream.close();
	}

	/**
	 * Reads the mass spectrum report entries.
	 * 
	 * @param file
	 * @param batchProcessJob
	 * @param monitor
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void readMassSpectrumReportEntries(File file, IBatchProcessJob batchProcessJob, IProgressMonitor monitor) throws XMLStreamException, IOException {

		IMassSpectrumReportSupplierEntry reportEntry;
		String reportSupplierId = "";
		XMLEvent event;
		Attribute attribute;
		String attributeName;
		/*
		 * Open the streams.
		 */
		XMLEventReader eventReader;
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		eventReader = inputFactory.createXMLEventReader(bufferedInputStream, IBatchProcessJobTags.UTF8);
		/*
		 * Use event filters.
		 */
		List<String> acceptedElements = new ArrayList<String>();
		acceptedElements.add(IBatchProcessJobTags.MASSSPECTRUM_REPORT_ENTRY);
		EventFilter eventFilter = new BatchProcessJobEventFilter(acceptedElements);
		XMLEventReader filteredEventReader = inputFactory.createFilteredReader(eventReader, eventFilter);
		/*
		 * Read all entries.
		 */
		while(filteredEventReader.hasNext()) {
			event = filteredEventReader.nextEvent();
			Iterator<? extends Attribute> attributes = event.asStartElement().getAttributes();
			while(attributes.hasNext()) {
				attribute = attributes.next();
				attributeName = attribute.getName().getLocalPart();
				// Get the report supplier id.
				if(attributeName.equals(IBatchProcessJobTags.MASSSPECTRUM_REPORT_SUPPLIER_ID)) {
					reportSupplierId = attribute.getValue();
				}
			}
			// Get the report folder.
			event = eventReader.nextEvent();
			String reportFolderOrFile = event.asCharacters().getData();
			reportEntry = new MassSpectrumReportSupplierEntry(reportFolderOrFile, reportSupplierId);
			batchProcessJob.getMassSpectrumReportEntries().add(reportEntry);
		}
		/*
		 * Close the streams.
		 */
		eventReader.close();
		bufferedInputStream.close();
	}
}
