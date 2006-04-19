/*-
 * $Id: MuffType.java,v 1.7 2006/04/19 13:38:50 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
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
	
	public XmlProtoEquipment toXMLObject() {
		XmlProtoEquipment xmlEQT = XmlProtoEquipment.Factory.newInstance();
		
		if (this.name.length() > 32) {
			System.out.println("equipmenttype name (" + this.name + ") length greater then 32 symbols. coping to description and cutting...");
			xmlEQT.setDescription(this.name);
			this.name = this.name.substring(0, 31);
		}
		
		XmlIdentifier uid = xmlEQT.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		xmlEQT.setName(this.name);
		
		XmlIdentifier eqtId = xmlEQT.addNewEquipmentTypeId();
		eqtId.setStringValue("muff");
		
		return xmlEQT;
	}
}
