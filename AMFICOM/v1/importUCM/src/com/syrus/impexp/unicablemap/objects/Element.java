/*-
 * $Id: Element.java,v 1.3 2005/09/08 06:34:32 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDeviceSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemePort.DirectionType;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement.Kind;
import com.syrus.impexp.unicablemap.TextWriter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Element {

	private int id;
	private String name;
	private Integer eqtId;
	private String equipmentTypeId;
	private String wellId;
	
	private Device device;
	private int counter = 0;
	private String label;
	private String kind;
//	private Integer startCableId;
//	private Integer endCableId;
	
	
	public Element(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}



	public void setEquipmentTypeId(String equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public Port addOutputPort(String endCableId) {
		if (this.device == null) {
			this.device = new Device("dev" + this.id);
			this.device.setParentId(String.valueOf(this.id));
			this.device.setName("dev"+this.id);
		}
		Port port = new Port("o" + endCableId);
		port.setParentId(this.device.getId());
		port.setDirectionType(DirectionType.OUT);
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
	
	public void setKind(String kind) {
		this.kind = kind;
	}

	public Port addInputPort(String startCableId) {
		if (this.device == null) {
			this.device = new Device("device" + this.id);
			this.device.setParentId(String.valueOf(this.id));
			this.device.setName("device"+this.id);
		}
		Port port = new Port("i" + startCableId);
		port.setParentId(this.device.getId());
		port.setDirectionType(DirectionType.IN);
		port.setName(Integer.toString(++this.counter) + "i");
		this.device.addPort(port);
		return port;
	}

	public void setWellId(String wellId) {
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
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void write(TextWriter writer) {
		writer.startObject(UniCableMapType.UCM_MUFF);
		writer.put("id", String.valueOf(this.id));
		writer.put("name", this.name);
		writer.put("eqtid", String.valueOf(this.eqtId));
		writer.put("codenameid", this.equipmentTypeId);
		writer.put("wellid", String.valueOf(this.wellId));
//		writer.put("startid", String.valueOf(this.startCableId));
//		writer.put("endid", String.valueOf(this.endCableId));
		writer.endObject();
	}
	
	public XmlSchemeElement toXMLObject(XmlIdentifier parentId) {
		XmlSchemeElement xmlSE = XmlSchemeElement.Factory.newInstance();
		XmlIdentifier uid = xmlSE.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			this.name = this.name.substring(0, 31);
		}
		
		xmlSE.setName(this.name);
//		xmlSE.setDescription("");
		
		if (this.equipmentTypeId == null) {
			System.err.println("equipmentTypeId is null for " + this.name);
			this.equipmentTypeId = "VOID";
		}
		
		XmlIdentifier eqtid = xmlSE.addNewEquipmentTypeId();
		eqtid.setStringValue(this.equipmentTypeId);
		xmlSE.setEquipmentTypeId(eqtid);
		
		xmlSE.setLabel(this.label);
		
		if (this.kind.equals("SCHEMED")) {
			xmlSE.setKind(Kind.SCHEME_CONTAINER);
		} else {
			xmlSE.setKind(Kind.SCHEME_ELEMENT_CONTAINER);
		}
		
		XmlIdentifier siteNodeUid = xmlSE.addNewSiteNodeId();
		if (this.wellId == null) {
			System.err.println("well is null for " + this.name);
			this.wellId = "";
		}
		siteNodeUid.setStringValue(String.valueOf(this.wellId));
		
		xmlSE.setParentSchemeId(parentId);
				
		if (this.device != null) {
			XmlSchemeDeviceSeq xmlSchemeDevices = xmlSE.addNewSchemeDevices();
			xmlSchemeDevices.setSchemeDeviceArray(new XmlSchemeDevice[] {this.device.toXMLObject()});
		}
		return xmlSE;
	}
}
