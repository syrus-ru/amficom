/*-
 * $Id: CableType.java,v 1.7 2005/11/05 13:42:44 stas Exp $
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

public class CableType {
//	public static final String TYPE = UniCableMapType.UCM_CABLE_TYPE;
	
	private String id;
	private String name;
	private Collection<ThreadType> threadTypes = new LinkedList<ThreadType>();
	private int threadNum;
	
	public CableType(String id) {
		this.id = id;
	}
		
	public String getName() {
		return this.name;
	}

	public String getId() {
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
	
	public Collection<ThreadType> getThreadTypes() {
		return this.threadTypes;
	}

	public XmlCableLinkType toXMLObject() {
		XmlCableLinkType xmlLT = XmlCableLinkType.Factory.newInstance();
		
		XmlIdentifier uid = xmlLT.addNewId();
		uid.setStringValue(this.id);
		xmlLT.setName(this.name);
		xmlLT.setCodename(this.name);
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
