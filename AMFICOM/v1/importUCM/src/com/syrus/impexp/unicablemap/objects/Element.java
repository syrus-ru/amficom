/*-
 * $Id: Element.java,v 1.9 2005/09/30 08:36:27 stas Exp $
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

public class Element {

	private int id;
	private String name;
//	private Integer eqtId;
	private String equipmentTypeId;
	private Equipment equipment;
	private String wellId;
	
	private Device device;
	private int counter = 0;
	private String label;
	private String kind;
	
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

//	public void setEqtId(Integer eqtId) {
//		this.eqtId = eqtId;
//	}

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

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public void setWellId(String wellId) {
		this.wellId = wellId;
	}
	
	public Device getDevice() {
		return this.device;
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
	
	public XmlSchemeElement toXMLObject(XmlIdentifier parentId) {
		XmlSchemeElement xmlSE = XmlSchemeElement.Factory.newInstance();
		XmlIdentifier uid = xmlSE.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			System.out.println("element name (" + this.name + ") length greater then 32 symbols. cuting... ");
			this.name = this.name.substring(0, 31);
		}
		
		xmlSE.setName(this.name);
//		xmlSE.setDescription("");
		
		if (this.equipment != null) {
			XmlIdentifier eqid = xmlSE.addNewEquipmentId();
			eqid.setStringValue(this.equipment.getId());
			xmlSE.setEquipmentId(eqid);
		}
		else if (this.equipmentTypeId != null) {
			XmlIdentifier eqtid = xmlSE.addNewProtoEquipmentId();
			eqtid.setStringValue(this.equipmentTypeId);
			xmlSE.setProtoEquipmentId(eqtid);
		} else {
			this.equipmentTypeId = "VOID";
			XmlIdentifier eqtid = xmlSE.addNewProtoEquipmentId();
			eqtid.setStringValue(this.equipmentTypeId);
			xmlSE.setProtoEquipmentId(eqtid);
		}
		
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
		} else {
			siteNodeUid.setStringValue(String.valueOf(this.wellId));
		}
		
		xmlSE.setParentSchemeId(parentId);
				
		if (this.device != null) {
			XmlSchemeDeviceSeq xmlSchemeDevices = xmlSE.addNewSchemeDevices();
			xmlSchemeDevices.setSchemeDeviceArray(new XmlSchemeDevice[] {this.device.toXMLObject()});
		}
		return xmlSE;
	}
}
