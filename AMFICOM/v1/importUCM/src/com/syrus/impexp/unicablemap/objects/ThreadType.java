/*-
 * $Id: ThreadType.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.amficom.config.xml.CableThreadType;
import com.syrus.amficom.general.xml.UID;

public class ThreadType {

	private String id;
	private String codename;
	private Integer linkTypeId;
	private Integer cableTypeId;

	public ThreadType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public void setLinkTypeId(Integer typeId) {
		this.linkTypeId = typeId;
	}
	
	public void setCableTypeId(Integer typeId) {
		this.cableTypeId = typeId;
	}
	
	public CableThreadType toXMLObject() {
		CableThreadType xmlCTT = CableThreadType.Factory.newInstance();
		
		UID uid = xmlCTT.addNewUid();
		uid.setStringValue(String.valueOf(this.id));
		xmlCTT.setName(this.codename);
		xmlCTT.setCodename(this.codename);
		
		UID ltuid = xmlCTT.addNewLinktypeuid();
		ltuid.setStringValue(this.linkTypeId.toString());
		
		UID cltuid = xmlCTT.addNewCablelinktypeuid();
		cltuid.setStringValue(this.cableTypeId.toString());
		
		return xmlCTT;
	}
}
