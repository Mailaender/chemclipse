/*******************************************************************************
 * Copyright (c) 2018, 2020 Lablicate GmbH.
 * 
 * All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package org.eclipse.chemclipse.model.identifier.template;

public class TargetTemplate {

	private String name = "";
	private String casNumber = "";
	private String comments = "";
	private String contributor = "";
	private String referenceId = "";

	public TargetTemplate() {

		this("", "");
	}

	public TargetTemplate(String name, String casNumber) {

		this.name = name;
		this.casNumber = casNumber;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getCasNumber() {

		return casNumber;
	}

	public void setCasNumber(String casNumber) {

		this.casNumber = casNumber;
	}

	public String getComments() {

		return comments;
	}

	public void setComments(String comments) {

		this.comments = comments;
	}

	public String getContributor() {

		return contributor;
	}

	public void setContributor(String contributor) {

		this.contributor = contributor;
	}

	public String getReferenceId() {

		return referenceId;
	}

	public void setReferenceId(String referenceId) {

		this.referenceId = referenceId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		TargetTemplate other = (TargetTemplate)obj;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return "TargetTemplate [name=" + name + ", casNumber=" + casNumber + ", comments=" + comments + ", contributor=" + contributor + ", referenceId=" + referenceId + "]";
	}
}
