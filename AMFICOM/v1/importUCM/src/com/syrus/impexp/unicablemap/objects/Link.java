/*-
 * $Id: Link.java,v 1.3 2006/01/24 07:44:18 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;

public class Link {
	
	private String id;
	private String name;
	private Integer typeId;
	private String startPortId; // порт
	private String endPortId; // порт
	
	public Link(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setEndPortId(String endId) {
		this.endPortId = endId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setStartPortId(String startId) {
		this.startPortId = startId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	
	int counter = 0;
	
	public XmlSchemeLink toXMLObject(XmlIdentifier parentId, boolean isScheme) {
		XmlSchemeLink xmlCL = XmlSchemeLink.Factory.newInstance();
		XmlIdentifier uid = xmlCL.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			System.out.println("link name (" + this.name + ") length greater then 32 symbols. coping to description and cutting...");
			xmlCL.setDescription(this.name);
			this.name = this.name.substring(0, 31);
		}
		
		xmlCL.setName(this.name);
//		xmlCL.setDescription("");
		
		if (this.typeId == null) {
			System.err.println("Link typeIs is null");
			this.typeId = Integer.valueOf(0);
		}
		
		XmlIdentifier linkTypeId = xmlCL.addNewLinkTypeId();
		linkTypeId.setStringValue(String.valueOf(this.typeId));
		xmlCL.setLinkTypeId(linkTypeId);
		
		if (this.startPortId != null) {
			XmlIdentifier suid = xmlCL.addNewSourceSchemePortId();
			suid.setStringValue(this.startPortId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): startId is null for " + this.name);
		}

		if (this.endPortId != null) {
			XmlIdentifier euid = xmlCL.addNewTargetSchemePortId();
			euid.setStringValue(this.endPortId);
		} else {
			System.out.println("Cable (" + (++this.counter) + "): endId is null for " + this.name);
		}
		
		if (isScheme) {
			xmlCL.setParentSchemeId(parentId);
		} else {
			xmlCL.setParentSchemeElementId(parentId);
		}
		
		xmlCL.setPhysicalLength(0);
		xmlCL.setOpticalLength(0);
	
		return xmlCL;
	}
}

