/*-
 * $Id: Equipment.java,v 1.2 2005/09/11 17:08:11 stas Exp $
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
		
		XmlIdentifier uid = xmlEq.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		xmlEq.setId(uid);
		
		if (this.typeId == null) {
			System.out.println("Type id is null for " + this.name);
			this.typeId = "VOID";
		}
		XmlIdentifier eqtid = xmlEq.addNewEquipmentTypeId();
		eqtid.setStringValue(this.typeId);
		xmlEq.setEquipmentTypeId(eqtid);
		
		double[] d = Site.UTMtoGeo(latitude, longitude);
		
		xmlEq.setName(this.name);
		xmlEq.setLatitude((float)d[0]);
		xmlEq.setLongitude((float)d[1]);
		
		return xmlEq;
	}
}
