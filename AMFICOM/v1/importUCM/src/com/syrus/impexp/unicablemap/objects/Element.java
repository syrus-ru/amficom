/*-
 * $Id: Element.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.Collection;
import java.util.LinkedList;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.DirectionType;
import com.syrus.amficom.scheme.xml.PortTypeKind;
import com.syrus.amficom.scheme.xml.SchemeDevice;
import com.syrus.amficom.scheme.xml.SchemeDevices;
import com.syrus.amficom.scheme.xml.SchemeElement;
import com.syrus.impexp.unicablemap.TextWriter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Element {
	public static final String TYPE = UniCableMapType.UCM_MUFF;
	
	private int id;
	private String name;
	private Integer eqtId; 
	private String codename;
	private Integer wellId;
	
	private Device device;
	private int counter = 0;
//	private Integer startCableId;
//	private Integer endCableId;
	
	
	public Element(int id) {
		this.id = id;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public Port addOutputPort(Integer endCableId) {
		if (this.device == null) {
			this.device = new Device("device" + this.id);
			this.device.setName("device"+this.name);
		}
		Port port = new Port("out" + endCableId);
		port.setDirectionType(DirectionType.OUT);
		port.setKind(PortTypeKind.CABLE);
		port.setName(Integer.toString(++this.counter) + "o");
		this.device.addPort(port);
		return port;
	}

	public void setEqtId(Integer eqtId) {
		this.eqtId = eqtId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Port addInputPort(Integer startCableId) {
		if (this.device == null) {
			this.device = new Device("device" + this.id);
			this.device.setName("device"+this.name);
		}
		Port port = new Port("in" + startCableId);
		port.setDirectionType(DirectionType.IN);
		port.setKind(PortTypeKind.CABLE);
		port.setName(Integer.toString(++this.counter) + "i");
		this.device.addPort(port);
		return port;
	}

	public void setWellId(Integer wellId) {
		this.wellId = wellId;
	}
	
	public Collection<Port> getPorts(DirectionType.Enum directionType) {
		Collection<Port> ports = new LinkedList<Port>();
		if (this.device != null) {
			for (Port p : this.device.getPorts()) {
				if (directionType.equals(p.getDirectionType())) {
					ports.add(p);
				}
			}
		}
		return ports;
	}
	
	public void write(TextWriter writer) {
		writer.startObject(TYPE);
		writer.put("id", String.valueOf(this.id));
		writer.put("name", this.name);
		writer.put("eqtid", String.valueOf(this.eqtId));
		writer.put("codenameid", this.codename);
		writer.put("wellid", String.valueOf(this.wellId));
//		writer.put("startid", String.valueOf(this.startCableId));
//		writer.put("endid", String.valueOf(this.endCableId));
		writer.endObject();
	}
	
	public SchemeElement toXMLObject() {
		SchemeElement xmlSE = SchemeElement.Factory.newInstance();
		UID uid = xmlSE.addNewUid();
		uid.setStringValue(String.valueOf(this.id));
		xmlSE.setName(this.name);
		xmlSE.setDescription("");
		if (this.codename == null) {
			System.err.println("codename is null for " + this.name);
			this.codename = "";
		}
		xmlSE.setEquipmenttype(this.codename);
		if (this.codename.equals("SCHEMED")) {
			xmlSE.setLabel(this.name);
		} else {
			xmlSE.setLabel("лн");
		}
			
		
		UID siteNodeUid = xmlSE.addNewSitenodeuid();
		if (this.wellId == null) {
			System.err.println("well is null for " + this.name);
			this.wellId = 0;
		}
		siteNodeUid.setStringValue(String.valueOf(this.wellId));
		
		if (this.device != null) {
			SchemeDevices xmlSchemeDevices = xmlSE.addNewSchemedevices();
			xmlSchemeDevices.setSchemedeviceArray(new SchemeDevice[] {this.device.toXMLObject()});
		}
		return xmlSE;
	}
}
