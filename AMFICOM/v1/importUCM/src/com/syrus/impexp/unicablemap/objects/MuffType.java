/*-
 * $Id: MuffType.java,v 1.2 2005/09/07 12:47:46 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.impexp.unicablemap.TextWriter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class MuffType {
	public static final String TYPE = UniCableMapType.UCM_MUFF_TYPE;
	
	private int id;
	private String name;
	private String codename;
	private int fiberNum;
	private int cablePortsNum;
	
	public MuffType(int id) {
		this.id = id;
		this.codename = "EQT" + Integer.toString(id); 
	}

	public void setCablePortsNum(int cablePortsNum) {
		this.cablePortsNum = cablePortsNum;
	}
	
	public String getId() {
		return String.valueOf(this.id);
	}

	public void setFiberNum(int fiberNum) {
		this.fiberNum = fiberNum;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	void write(TextWriter writer) {
		writer.startObject(TYPE);
		writer.put("id", String.valueOf(this.id));
		writer.put("name", this.name);
		writer.put("fibers", String.valueOf(this.fiberNum));
		writer.put("cableports", String.valueOf(this.cablePortsNum));
		writer.endObject();
	}
	
	public XmlEquipmentType toXMLObject() {
		XmlEquipmentType xmlEQT = XmlEquipmentType.Factory.newInstance();
		
		XmlIdentifier uid = xmlEQT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		xmlEQT.setName(this.name);
		xmlEQT.setCodename(this.codename);
		
		return xmlEQT;
	}
}
