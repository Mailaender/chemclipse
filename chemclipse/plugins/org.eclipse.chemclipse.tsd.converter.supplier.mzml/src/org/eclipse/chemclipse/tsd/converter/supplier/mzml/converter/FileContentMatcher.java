/*******************************************************************************
 * Copyright (c) 2025 Lablicate GmbH.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * Matthias Mailänder - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.tsd.converter.supplier.mzml.converter;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.chemclipse.converter.core.AbstractFileContentMatcher;
import org.eclipse.chemclipse.converter.core.IFileContentMatcher;

public class FileContentMatcher extends AbstractFileContentMatcher implements IFileContentMatcher {

	@Override
	public boolean checkFileFormat(File file) {

		boolean isValidFormat = false;
		try {
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(file));
			boolean hasRootElement = false;
			boolean hasIMS = false;
			while(xmlStreamReader.hasNext()) {
				int eventType = xmlStreamReader.next();
				if(eventType == XMLStreamConstants.START_ELEMENT) {
					String elementName = xmlStreamReader.getLocalName();
					if(elementName.equals("mzML")) {
						hasRootElement = true;
					} else if(elementName.equals("scan")) {
						while(xmlStreamReader.hasNext()) {
							int innerEventType = xmlStreamReader.next();
							if(innerEventType == XMLStreamConstants.START_ELEMENT && xmlStreamReader.getLocalName().equals("cvParam")) {
								String accession = xmlStreamReader.getAttributeValue(null, "accession");
								String paramName = xmlStreamReader.getAttributeValue(null, "name");
								if(accession.equals("MS:1002476") && paramName.equals("ion mobility drift time")) {
									hasIMS = true;
									break;
								}
							}
							if(innerEventType == XMLStreamConstants.END_ELEMENT && xmlStreamReader.getLocalName().equals("scan")) {
								break;
							}
						}
					}
					if(hasRootElement && hasIMS) {
						isValidFormat = true;
						break;
					}
				}
			}
			xmlStreamReader.close();
		} catch(Exception e) {
			e.printStackTrace();
			// fail silently
		}
		return isValidFormat;
	}
}
