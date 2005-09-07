/*-
 * $Id: CableType.java,v 1.2 2005/09/07 12:47:45 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.configuration.xml.XmlCableThreadTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
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
	
	public XmlCableLinkType toXMLObject() {
		XmlCableLinkType xmlLT = XmlCableLinkType.Factory.newInstance();
		
		XmlIdentifier uid = xmlLT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		xmlLT.setName(this.name);
		xmlLT.setCodename(this.name);
		xmlLT.setDescription("");
		xmlLT.setSort(XmlLinkTypeSort.OPTICAL);
		
		if (this.threadTypes.size() == 0) {
			System.err.println("this.threadTypes.size() = 0");
		} else {
		XmlCableThreadTypeSeq xmlCTTs = xmlLT.addNewCableThreadTypes();
		Collection<XmlCableThreadType> ctts = new ArrayList<XmlCableThreadType>(this.threadTypes.size());
		for (ThreadType tType : this.threadTypes) {
			ctts.add(tType.toXMLObject());
		}
		xmlCTTs.setCableThreadTypeArray(ctts.toArray(new XmlCableThreadType[ctts.size()]));
		}
		
		return xmlLT;
	}
}
