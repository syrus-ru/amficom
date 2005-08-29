/*-
 * $Id: Port.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.DirectionType;
import com.syrus.amficom.scheme.xml.PortTypeKind;
import com.syrus.amficom.scheme.xml.SchemePort;

public class Port {
	public static final String DEFAULT_PORT_TYPE = "default_port_type";
	
	private String id;
	private String name;
	private PortTypeKind.Enum kind;
	private DirectionType.Enum directionType;

	private boolean connected = false;
	
	public Port(String id) {
		this.id = id;
	}
	
	public void setDirectionType(DirectionType.Enum directionType) {
		this.directionType = directionType;
	}
	
	public DirectionType.Enum getDirectionType() {
		return this.directionType;
	}

	public void setKind(PortTypeKind.Enum kind) {
		this.kind = kind;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public SchemePort toXMLObject() {
		SchemePort xmlPort = SchemePort.Factory.newInstance();
		UID uid = xmlPort.addNewUid();
		uid.setStringValue(this.id);
		xmlPort.setName(this.name);
		xmlPort.setDescription("");
		
		if (this.kind == null) {
			System.err.println("kind is null for " + this.name);
			this.kind = PortTypeKind.SIMPLE;
		}
		
		xmlPort.setKind(this.kind);
		if (this.directionType == null) {
			System.err.println("directionType is null for " + this.name);
			this.directionType = DirectionType.IN;
		}
		xmlPort.setDirectiontype(this.directionType);
		
		xmlPort.setPorttype(DEFAULT_PORT_TYPE);
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
