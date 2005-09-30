/*-
 * $Id: Port.java,v 1.5 2005/09/30 08:33:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemePort.DirectionType;

public class Port {
	public static final String CABLE_PORT_TYPE = "cable_port_type";
	
	private String id;
	private String name;
	private DirectionType.Enum directionType;
	private String parentId;

	private boolean connected = false;
	
	public Port(String id) {
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
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public XmlSchemeCablePort toXMLObject() {
		XmlSchemeCablePort xmlPort = XmlSchemeCablePort.Factory.newInstance();
		XmlIdentifier uid = xmlPort.addNewId();
		uid.setStringValue(this.id);
		xmlPort.setId(uid);
		
		if (this.name.length() > 32) {
			System.out.println("port name (" + this.name + ") length greater then 32 symbols. cuting... ");
			this.name = this.name.substring(0, 31);
		}
		
		xmlPort.setName(this.name);
//		xmlPort.setDescription("");
		
		XmlIdentifier deviceId = xmlPort.addNewParentSchemeDeviceId();
		deviceId.setStringValue(this.parentId);
		xmlPort.setParentSchemeDeviceId(deviceId);
		
		if (this.directionType == null) {
			System.err.println("directionType is null for " + this.name);
			this.directionType = DirectionType.IN;
		}
		xmlPort.setDirectionType(this.directionType);
		
		XmlIdentifier portTypeId = xmlPort.addNewCablePortTypeId();
		portTypeId.setStringValue(CABLE_PORT_TYPE);
		xmlPort.setCablePortTypeId(portTypeId);
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
