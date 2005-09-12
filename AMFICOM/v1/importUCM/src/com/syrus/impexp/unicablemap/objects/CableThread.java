/*-
 * $Id: CableThread.java,v 1.4 2005/09/12 06:11:57 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
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
	private Integer sourcePortId; // previous fiber
	private Integer targetPortId; // next fiber

	public CableThread(String id) {
		this.id = id;
		if (id.length() > 32) {
			System.out.println("thread" + id);
		}
	}

	public void setSourcePortId(Integer sourcePortId) {
		this.sourcePortId = sourcePortId;
	}

	public void setTargetPortId(Integer targetPortId) {
		this.targetPortId = targetPortId;
	}

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
		
		if (this.name.length() > 32) {
			System.err.println("thread");
		}
		
		xmlCT.setName(this.name);
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
