/*-
 * $Id: Equipment.java,v 1.5 2005/10/03 13:43:27 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.impexp.unicablemap.map.Site;


public class Equipment {
	private String id;
	private String typeId;
	private String name;
	private float latitude;
	private float longitude;

	public Equipment(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public XmlEquipment toXMLObject() {
		XmlEquipment xmlEq = XmlEquipment.Factory.newInstance();
		
		if (this.name.length() > 32) {
			System.out.println("equipment name (" + this.name + ") length greater then 32 symbols. cuting... ");
			this.name = this.name.substring(0, 31);
		}
		
		XmlIdentifier uid = xmlEq.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		xmlEq.setId(uid);
		
		if (this.typeId == null) {
			System.out.println("Type id is null for " + this.name);
			this.typeId = "VOID";
		}
		XmlIdentifier eqtid = xmlEq.addNewProtoEquipmentId();
		eqtid.setStringValue(this.typeId);
		xmlEq.setProtoEquipmentId(eqtid);
		
		double[] d = Site.UTMtoGeo(this.longitude, this.latitude);
		
		xmlEq.setName(this.name);
		xmlEq.setLongitude((float)d[0]);
		xmlEq.setLatitude((float)d[1]);
		
		return xmlEq;
	}
}
