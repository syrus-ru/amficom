/*-
 * $Id: LinkType.java,v 1.2 2005/09/11 15:25:58 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSort;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class LinkType {
	public static final String TYPE = UniCableMapType.UCM_LINK_TYPE;
	
	private int id;
	private String name;
	
	public LinkType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public XmlLinkType toXMLObject() {
		XmlLinkType xmlLT = XmlLinkType.Factory.newInstance();
		
		XmlIdentifier uid = xmlLT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		xmlLT.setName(this.name);
		xmlLT.setCodename(this.name);

		xmlLT.setSort(XmlLinkTypeSort.OPTICAL);
		
		return xmlLT;
	}
}

