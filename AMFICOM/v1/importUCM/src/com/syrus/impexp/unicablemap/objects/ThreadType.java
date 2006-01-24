/*-
 * $Id: ThreadType.java,v 1.6 2006/01/24 07:44:18 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.configuration.xml.XmlCableThreadType;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;

public class ThreadType {

	private String id;
	private String codename;
	private String linkTypeId;
	private String cableTypeId;

	public ThreadType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public void setLinkTypeId(String typeId) {
		this.linkTypeId = typeId;
	}
	
	public String getLinkTypeId() {
		return this.linkTypeId.toString();
	}
	
	public void setCableTypeId(String typeId) {
		this.cableTypeId = typeId;
	}
	
	public XmlCableThreadType toXMLObject() {
		XmlCableThreadType xmlCTT = XmlCableThreadType.Factory.newInstance();
		
		XmlIdentifier uid = xmlCTT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		xmlCTT.setName("волокно" + this.codename);
		xmlCTT.setCodename(String.valueOf(this.id));
		
		XmlIdentifier ltuid = xmlCTT.addNewLinkTypeId();
		ltuid.setStringValue(this.linkTypeId.toString());
		
		XmlIdentifier cltuid = xmlCTT.addNewCableLinkTypeId();
		cltuid.setStringValue(this.cableTypeId.toString());
		
		xmlCTT.setColor(-1);
		
		return xmlCTT;
	}
}
