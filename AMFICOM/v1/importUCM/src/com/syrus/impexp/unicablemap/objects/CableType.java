/*-
 * $Id: CableType.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.amficom.config.xml.CableLinkType;
import com.syrus.amficom.config.xml.CableThreadType;
import com.syrus.amficom.config.xml.CableThreadTypes;
import com.syrus.amficom.config.xml.LinkTypeSort;
import com.syrus.amficom.general.xml.UID;
import com.syrus.impexp.unicablemap.TextWriter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class CableType {
	public static final String TYPE = UniCableMapType.UCM_CABLE_TYPE;
	
	private int id;
	private String name;
	private Collection<ThreadType> threadTypes = new LinkedList<ThreadType>();
	private int threadNum;
	
	public CableType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public void addThreadType(ThreadType threadType) {
		if (this.threadTypes.size() < this.threadNum) {
			this.threadTypes.add(threadType);
		}
	}
	
	void write(TextWriter writer) {
		writer.startObject(TYPE);
		writer.put("id", String.valueOf(this.id));
		writer.put("name", this.name);
//		writer.put("threads", String.valueOf(this.threadNum));
		writer.endObject();
	}
	
	public CableLinkType toXMLObject() {
		CableLinkType xmlLT = CableLinkType.Factory.newInstance();
		
		UID uid = xmlLT.addNewUid();
		uid.setStringValue(String.valueOf(this.id));
		xmlLT.setName(this.name);
		xmlLT.setCodename(this.name);
		xmlLT.setDescription("");
		xmlLT.setSort(LinkTypeSort.OPTICAL);
		
		if (this.threadTypes.size() == 0) {
			System.err.println("this.threadTypes.size() = 0");
		} else {
		CableThreadTypes xmlCTTs = xmlLT.addNewCablethreadtypes();
		Collection<CableThreadType> ctts = new ArrayList<CableThreadType>(this.threadTypes.size());
		for (ThreadType tType : this.threadTypes) {
			ctts.add(tType.toXMLObject());
		}
		xmlCTTs.setCablethreadtypeArray(ctts.toArray(new CableThreadType[ctts.size()]));
		}
		
		return xmlLT;
	}
}
