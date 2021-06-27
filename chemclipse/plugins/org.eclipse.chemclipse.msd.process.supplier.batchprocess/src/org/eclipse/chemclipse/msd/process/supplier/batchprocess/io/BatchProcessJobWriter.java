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
 *******************************************************************************/
package org.eclipse.chemclipse.msd.process.supplier.batchprocess.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.eclipse.chemclipse.converter.exceptions.FileIsNotWriteableException;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.internal.support.IBatchProcessJobTags;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IBatchProcessJob;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumInputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumOutputEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumProcessEntry;
import org.eclipse.chemclipse.msd.process.supplier.batchprocess.model.IMassSpectrumReportSupplierEntry;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Dr. Philip Wenig
 * 
 */
public class BatchProcessJobWriter implements IBatchProcessJobWriter {

	@Override
	public void writeBatchProcessJob(File file, IBatchProcessJob batchProcessJob, IProgressMonitor monitor) throws FileNotFoundException, FileIsNotWriteableException, IOException, XMLStreamException {

		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
		XMLEventWriter eventWriter = xmlOutputFactory.createXMLEventWriter(bufferedOutputStream, IBatchProcessJobTags.UTF8);
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		/*
		 * Document
		 */
		eventWriter.add(eventFactory.createStartDocument());
		StartElement chromatogramStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.BATCH_PROCESS_JOB);
		eventWriter.add(chromatogramStart);
		/*
		 * Write the header and the list informations.
		 */
		writeBatchProcessJobHeader(eventWriter, eventFactory, batchProcessJob);
		writeComment(eventWriter, eventFactory, "Load the following mass spectrum.");
		writeMassSpectrumInputEntries(eventWriter, eventFactory, batchProcessJob.getMassSpectrumInputEntries());
		writeComment(eventWriter, eventFactory, "Process each chromatogram with the listed methods.");
		writeMassSpectrumProcessEntries(eventWriter, eventFactory, batchProcessJob.getMassSpectrumProcessEntries());
		writeComment(eventWriter, eventFactory, "Write each processed mass spectrum to the given output formats.");
		writeMassSpectrumOutputEntries(eventWriter, eventFactory, batchProcessJob.getMassSpectrumOutputEntries());
		writeComment(eventWriter, eventFactory, "Process each mass spectrum with the listed report suppliers.");
		writeMassSpectrumReportEntries(eventWriter, eventFactory, batchProcessJob.getMassSpectrumReportEntries());
		/*
		 * Close the document
		 */
		EndElement chromatogramEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.BATCH_PROCESS_JOB);
		eventWriter.add(chromatogramEnd);
		eventWriter.add(eventFactory.createEndDocument());
		/*
		 * Close the streams
		 */
		bufferedOutputStream.flush();
		eventWriter.flush();
		bufferedOutputStream.close();
		eventWriter.close();
	}

	/**
	 * Writes the comment.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @throws XMLStreamException
	 */
	private void writeComment(XMLEventWriter eventWriter, XMLEventFactory eventFactory, String comment) throws XMLStreamException {

		/*
		 * Comment
		 */
		Comment batchJobInfo = eventFactory.createComment(comment);
		eventWriter.add(batchJobInfo);
	}

	/**
	 * Writes the header.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param batchProcessJob
	 * @throws XMLStreamException
	 */
	private void writeBatchProcessJobHeader(XMLEventWriter eventWriter, XMLEventFactory eventFactory, IBatchProcessJob batchProcessJob) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement headerStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.HEADER);
		EndElement headerEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.HEADER);
		/*
		 * Write the elements.
		 */
		eventWriter.add(headerStart);
		eventWriter.add(headerEnd);
	}

	/**
	 * Writes the chromatogram input entries.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param inputEntries
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumInputEntries(XMLEventWriter eventWriter, XMLEventFactory eventFactory, List<IMassSpectrumInputEntry> inputEntries) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entriesStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_INPUT_ENTRIES);
		EndElement entriesEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_INPUT_ENTRIES);
		/*
		 * Write the elements.
		 */
		eventWriter.add(entriesStart);
		for(IMassSpectrumInputEntry inputEntry : inputEntries) {
			writeMassSpectrumInputEntry(eventWriter, eventFactory, inputEntry);
		}
		eventWriter.add(entriesEnd);
	}

	/**
	 * Writes the mass spectrum input entry.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param inputEntry
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumInputEntry(XMLEventWriter eventWriter, XMLEventFactory eventFactory, IMassSpectrumInputEntry inputEntry) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entryStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_INPUT_ENTRY);
		EndElement entryEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_INPUT_ENTRY);
		/*
		 * Values.
		 */
		Characters inputFile = eventFactory.createCData(inputEntry.getInputFile());
		/*
		 * Write the elements.
		 */
		eventWriter.add(entryStart);
		eventWriter.add(inputFile);
		eventWriter.add(entryEnd);
	}

	/**
	 * Writes the mass spectrum process entries.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param processEntries
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumProcessEntries(XMLEventWriter eventWriter, XMLEventFactory eventFactory, List<IMassSpectrumProcessEntry> processEntries) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entriesStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_PROCESS_ENTRIES);
		EndElement entriesEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_PROCESS_ENTRIES);
		/*
		 * Write the elements.
		 */
		eventWriter.add(entriesStart);
		for(IMassSpectrumProcessEntry processEntry : processEntries) {
			writeMassSpectrumProcessEntry(eventWriter, eventFactory, processEntry);
		}
		eventWriter.add(entriesEnd);
	}

	/**
	 * Writes the mass spectrum process entry.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param processEntry
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumProcessEntry(XMLEventWriter eventWriter, XMLEventFactory eventFactory, IMassSpectrumProcessEntry processEntry) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entryStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_PROCESS_ENTRY);
		EndElement entryEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_PROCESS_ENTRY);
		/*
		 * Attributes and values.
		 */
		Attribute processorType = eventFactory.createAttribute(IBatchProcessJobTags.PROCESSOR_TYPE, processEntry.getProcessCategory());
		Attribute processorId = eventFactory.createAttribute(IBatchProcessJobTags.PROCESSOR_ID, processEntry.getProcessorId());
		/*
		 * Write the elements.
		 */
		eventWriter.add(entryStart);
		eventWriter.add(processorType);
		eventWriter.add(processorId);
		eventWriter.add(entryEnd);
	}

	/**
	 * Writes the mass spectrum output entries.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param inputEntries
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumOutputEntries(XMLEventWriter eventWriter, XMLEventFactory eventFactory, List<IMassSpectrumOutputEntry> outputEntries) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entriesStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_OUTPUT_ENTRIES);
		EndElement entriesEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_OUTPUT_ENTRIES);
		/*
		 * Write the elements.
		 */
		eventWriter.add(entriesStart);
		for(IMassSpectrumOutputEntry outputEntry : outputEntries) {
			writeMassSpectrumOutputEntry(eventWriter, eventFactory, outputEntry);
		}
		eventWriter.add(entriesEnd);
	}

	/**
	 * Writes the mass spectrum output entry.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param inputEntry
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumOutputEntry(XMLEventWriter eventWriter, XMLEventFactory eventFactory, IMassSpectrumOutputEntry outputEntry) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entryStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_OUTPUT_ENTRY);
		EndElement entryEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_OUTPUT_ENTRY);
		/*
		 * Attributes and Values.
		 */
		Attribute converterId = eventFactory.createAttribute(IBatchProcessJobTags.MASSSPECTRUM_CONVERTER_ID, outputEntry.getConverterId());
		Characters outputFolder = eventFactory.createCData(outputEntry.getOutputFolder());
		/*
		 * Write the elements.
		 */
		eventWriter.add(entryStart);
		eventWriter.add(converterId);
		eventWriter.add(outputFolder);
		eventWriter.add(entryEnd);
	}

	/**
	 * Writes the mass spectrum report entries.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param inputEntries
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumReportEntries(XMLEventWriter eventWriter, XMLEventFactory eventFactory, List<IMassSpectrumReportSupplierEntry> reportEntries) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entriesStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_REPORT_ENTRIES);
		EndElement entriesEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_REPORT_ENTRIES);
		/*
		 * Write the elements.
		 */
		eventWriter.add(entriesStart);
		for(IMassSpectrumReportSupplierEntry reportEntry : reportEntries) {
			writeMassSpectrumReportEntry(eventWriter, eventFactory, reportEntry);
		}
		eventWriter.add(entriesEnd);
	}

	/**
	 * Writes the chromatogram report entry.
	 * 
	 * @param eventWriter
	 * @param eventFactory
	 * @param inputEntry
	 * @throws XMLStreamException
	 */
	private void writeMassSpectrumReportEntry(XMLEventWriter eventWriter, XMLEventFactory eventFactory, IMassSpectrumReportSupplierEntry reportEntry) throws XMLStreamException {

		/*
		 * Element and content definition.
		 */
		StartElement entryStart = eventFactory.createStartElement("", "", IBatchProcessJobTags.MASSSPECTRUM_REPORT_ENTRY);
		EndElement entryEnd = eventFactory.createEndElement("", "", IBatchProcessJobTags.MASSSPECTRUM_REPORT_ENTRY);
		/*
		 * Attributes and Values.
		 */
		Attribute converterId = eventFactory.createAttribute(IBatchProcessJobTags.MASSSPECTRUM_REPORT_SUPPLIER_ID, reportEntry.getReportSupplierId());
		Characters reportFolderOrFile = eventFactory.createCData(reportEntry.getReportFolderOrFile());
		/*
		 * Write the elements.
		 */
		eventWriter.add(entryStart);
		eventWriter.add(converterId);
		eventWriter.add(reportFolderOrFile);
		eventWriter.add(entryEnd);
	}
}
