/*-
 * $Id: CableThread.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.SchemeCableThread;

public class CableThread {
	
	private String id;
	private String name;
	private String threadTypeId;
	private Integer sourcePortId; // previous fiber
	private Integer targetPortId; // next fiber

	public CableThread(String id) {
		this.id = id;
	}

	public void setSourcePortId(Integer sourcePortId) {
		this.sourcePortId = sourcePortId;
	}

	public void setTargetPortId(Integer targetPortId) {
		this.targetPortId = targetPortId;
	}

	public void setThreadTypeId(String threadTypeId) {
		this.threadTypeId = threadTypeId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	int counter = 0;
	public SchemeCableThread toXMLObject() {
		SchemeCableThread xmlCT = SchemeCableThread.Factory.newInstance();
		UID uid = xmlCT.addNewUid();
		uid.setStringValue(String.valueOf(this.id));
		xmlCT.setName(this.name);
		xmlCT.setDescription("");
		
		if (this.threadTypeId == null) {
			System.err.println("Thread typeId is null");
			this.threadTypeId = "";
		}
		xmlCT.setThreadtype(this.threadTypeId);
		
		if (this.sourcePortId != null) {
			UID suid = xmlCT.addNewSourceportuid();
			suid.setStringValue(Integer.toString(this.sourcePortId));
		}
		
		if (this.targetPortId != null) {
			UID euid = xmlCT.addNewTargetportuid();
			euid.setStringValue(Integer.toString(this.targetPortId));
		}
		
		return xmlCT;
	}
}
