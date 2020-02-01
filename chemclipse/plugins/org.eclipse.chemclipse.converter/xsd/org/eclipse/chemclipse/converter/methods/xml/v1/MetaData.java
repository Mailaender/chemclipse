/*******************************************************************************
 * Copyright (c) 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: EPL-1.0
 * 
 * Contributors:
 * generated by xjc compiler
 *******************************************************************************/
package org.eclipse.chemclipse.converter.methods.xml.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * Java class for MetaData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MetaData">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetaData", namespace = "https://github.com/eclipse/chemclipse/processmethods/v1", propOrder = {"value"})
public class MetaData {

	@XmlValue
	protected String value;
	@XmlAttribute(name = "key", required = true)
	protected String key;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return
	 * 		possible object is
	 *         {@link String }
	 * 
	 */
	public String getValue() {

		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 * 
	 */
	public void setValue(String value) {

		this.value = value;
	}

	/**
	 * Gets the value of the key property.
	 * 
	 * @return
	 * 		possible object is
	 *         {@link String }
	 * 
	 */
	public String getKey() {

		return key;
	}

	/**
	 * Sets the value of the key property.
	 * 
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 * 
	 */
	public void setKey(String value) {

		this.key = value;
	}
}