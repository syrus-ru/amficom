/*-
 * $Id: CableThread.java,v 1.7 2006/01/24 07:44:18 stas Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableThread;

public class CableThread {
	
	private String id;
	private String name;
	private String linkTypeId;
	private String parentId;
//	private Integer sourcePortId; // previous fiber
//	private Integer targetPortId; // next fiber

	public CableThread(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

//	public void setSourcePortId(Integer sourcePortId) {
//		System.out.println("sourcePortId is " + sourcePortId);
//		if (sourcePortId.intValue() < 1) {
//			System.err.println("!");
//		}
//		this.sourcePortId = sourcePortId;
//	}
//
//	public void setTargetPortId(Integer targetPortId) {
//		this.targetPortId = targetPortId;
//	}

	public void setLinkTypeId(String linkTypeId) {
		this.linkTypeId = linkTypeId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	int counter = 0;
	public XmlSchemeCableThread toXMLObject() {
		XmlSchemeCableThread xmlCT = XmlSchemeCableThread.Factory.newInstance();
		XmlIdentifier uid = xmlCT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		xmlCT.setName("???????" + this.name);
//		xmlCT.setDescription("");
		
		if (this.linkTypeId == null) {
			System.err.println("Thread linkTypeId is null");
			this.linkTypeId = "";
		}
		
		XmlIdentifier typeId = xmlCT.addNewLinkTypeId();
		typeId.setStringValue(String.valueOf(this.linkTypeId));
		xmlCT.setLinkTypeId(typeId);
		
		XmlIdentifier cableLinkId = xmlCT.addNewParentSchemeCableLinkId();
		cableLinkId.setStringValue(this.parentId);
		xmlCT.setParentSchemeCableLinkId(cableLinkId);
		
//		if (this.sourcePortId != null) {
//			XmlIdentifier suid = xmlCT.addNewSourceSchemePortId();
//			suid.setStringValue(this.sourcePortId.toString());
//		}
//		
//		if (this.targetPortId != null) {
//			XmlIdentifier euid = xmlCT.addNewTargetSchemePortId();
//			euid.setStringValue(this.targetPortId.toString());
//		}
		
		return xmlCT;
	}
}
