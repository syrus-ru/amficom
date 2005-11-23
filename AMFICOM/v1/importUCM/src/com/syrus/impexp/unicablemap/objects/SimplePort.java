/*-
 * $Id: SimplePort.java,v 1.1 2005/11/23 07:03:12 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePort;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemePort.DirectionType;

public class SimplePort {
	public static final String PORT_TYPE = "port_type";
	
	private String id;
	private String name;
	private String description;
	private DirectionType.Enum directionType;
	private String parentId;

	private boolean connected = false;
	
	public SimplePort(String id) {
		this.id = id;
		if (id.length() > 32) {
			System.out.println("port" + id);
		}
	}
	
	public void setDirectionType(DirectionType.Enum directionType) {
		this.directionType = directionType;
	}
	
	public DirectionType.Enum getDirectionType() {
		return this.directionType;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public XmlSchemePort toXMLObject() {
		XmlSchemePort xmlPort = XmlSchemePort.Factory.newInstance();
		XmlIdentifier uid = xmlPort.addNewId();
		uid.setStringValue(this.id);
		xmlPort.setId(uid);
		
		if (this.name.length() > 32) {
			System.out.println("port name (" + this.name + ") length greater then 32 symbols. cuting... ");
			this.name = this.name.substring(0, 31);
		}
		
		xmlPort.setName(this.name);
		if (this.description != null) {
			xmlPort.setDescription(this.description);
		}
		
		XmlIdentifier deviceId = xmlPort.addNewParentSchemeDeviceId();
		deviceId.setStringValue(this.parentId);
		xmlPort.setParentSchemeDeviceId(deviceId);
		
		if (this.directionType == null) {
			System.err.println("directionType is null for " + this.name);
			this.directionType = DirectionType.IN;
		}
		xmlPort.setDirectionType(this.directionType);
		
		XmlIdentifier portTypeId = xmlPort.addNewPortTypeId();
		portTypeId.setStringValue(PORT_TYPE);
		xmlPort.setPortTypeId(portTypeId);
		return xmlPort;
	}
	
	public String getId() {
		return this.id;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
