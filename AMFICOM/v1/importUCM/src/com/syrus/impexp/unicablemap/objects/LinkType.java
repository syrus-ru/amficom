/*-
 * $Id: LinkType.java,v 1.5 2006/01/24 07:44:18 stas Exp $
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
		
		if (this.name.length() > 32) {
			System.out.println("linktype name (" + this.name + ") length greater then 32 symbols. coping to description and cutting...");
			xmlLT.setDescription(this.name);
			this.name = this.name.substring(0, 31);
		}
		
		XmlIdentifier uid = xmlLT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		xmlLT.setName(this.name);
		xmlLT.setCodename(this.name);

		xmlLT.setSort(XmlLinkTypeSort.OPTICAL);
		
		return xmlLT;
	}
}

