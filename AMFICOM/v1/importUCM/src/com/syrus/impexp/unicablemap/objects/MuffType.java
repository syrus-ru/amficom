/*-
 * $Id: MuffType.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.amficom.config.xml.EquipmentType;
import com.syrus.amficom.general.xml.UID;
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
	
	public EquipmentType toXMLObject() {
		EquipmentType xmlEQT = EquipmentType.Factory.newInstance();
		
		UID uid = xmlEQT.addNewUid();
		uid.setStringValue(String.valueOf(this.id));
		
		xmlEQT.setName(this.name);
		xmlEQT.setCodename(this.codename);
		
		return xmlEQT;
	}
}
