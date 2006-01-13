/*-
 * $Id: Element.java,v 1.13 2006/01/13 11:49:48 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDeviceSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElementSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeLinkSeq;
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
	
	private Collection<Link> links = new LinkedList<Link>();
	private Collection<Element> elements = new LinkedList<Element>();
	
	public Element(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setEquipmentTypeId(String equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public void addElement(Element element) {
		this.elements.add(element);
	}
	
	public void addLink(Link link) {
		this.links.add(link);
	}
	
	public void initCounter() {
		this.counter = 0;
	}
	
	public Port addOutputCablePort(String endCableId) {
		if (this.device == null) {
			this.device = new Device("dev" + this.id);
			this.device.setParentId(String.valueOf(this.id));
			this.device.setName("dev"+this.id);
		}
		Port port = new Port("o" + endCableId);
		port.setParentId(this.device.getId());
		port.setDirectionType(DirectionType.OUT);
		port.setName(Integer.toString(++this.counter) + "o");
		this.device.addCablePort(port);
		return port;
	}
	
	public SimplePort addOutputPort(String endCableId, String portTypeId) {
		if (this.device == null) {
			this.device = new Device("dev" + this.id);
			this.device.setParentId(String.valueOf(this.id));
			this.device.setName("dev"+this.id);
		}
		// XXX check id
		SimplePort port = new SimplePort("o" + endCableId, portTypeId);
		port.setParentId(this.device.getId());
		port.setDirectionType(DirectionType.OUT);
		port.setName(Integer.toString(++this.counter));
		this.device.addPort(port);
		return port;
	}

//	public void setEqtId(Integer eqtId) {
//		this.eqtId = eqtId;
//	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setKind(String kind) {
		this.kind = kind;
	}

	public Port addInputCablePort(String startCableId) {
		if (this.device == null) {
			this.device = new Device("device" + this.id);
			this.device.setParentId(String.valueOf(this.id));
			this.device.setName("device"+this.id);
		}
		Port port = new Port("i" + startCableId);
		port.setParentId(this.device.getId());
		port.setDirectionType(DirectionType.IN);
		port.setName(Integer.toString(++this.counter) + "i");
		this.device.addCablePort(port);
		return port;
	}
	
	public SimplePort addInputPort(String startCableId, String portTypeId) {
		if (this.device == null) {
			this.device = new Device("device" + this.id);
			this.device.setParentId(String.valueOf(this.id));
			this.device.setName("device"+this.id);
		}
		SimplePort port = new SimplePort("i" + startCableId, portTypeId);
		port.setParentId(this.device.getId());
		port.setDirectionType(DirectionType.IN);
		port.setName(Integer.toString(++this.counter));
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

	public Collection<Port> getAllPorts() {
		if (this.device != null) {
			return this.device.getPorts();
		}
		return Collections.emptyList();
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
		for (Element child : this.elements) {
			ports.addAll(child.getPorts(directionType));
		}
		
		return ports;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public XmlSchemeElement toXMLObject(XmlIdentifier parentId, boolean isScheme) {
		XmlSchemeElement xmlSE = XmlSchemeElement.Factory.newInstance();
		XmlIdentifier uid = xmlSE.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		if (this.name.length() > 32) {
			System.out.println("element name (" + this.name + ") length greater then 32 symbols. cutting... ");
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
		
		if (this.wellId == null) {
			System.out.println("siteNodeId is not set for " + this.name);
			this.wellId = "";
		} else {
			XmlIdentifier siteNodeUid = xmlSE.addNewSiteNodeId();
			siteNodeUid.setStringValue(String.valueOf(this.wellId));
		}
		
		if (isScheme) {
			xmlSE.setParentSchemeId(parentId);
		} else {
			xmlSE.setParentSchemeElementId(parentId);
		}
				
		if (this.device != null) {
			XmlSchemeDeviceSeq xmlSchemeDevices = xmlSE.addNewSchemeDevices();
			xmlSchemeDevices.setSchemeDeviceArray(new XmlSchemeDevice[] {this.device.toXMLObject()});
		}
		
		if (!this.elements.isEmpty()) {
			XmlSchemeElementSeq xmlSchemeElements = xmlSE.addNewSchemeElements();
			Collection<XmlSchemeElement> ses = new ArrayList<XmlSchemeElement>(this.elements.size());
			for (Object element : this.elements) {
				ses.add(((Element)element).toXMLObject(uid, false));
			}
			xmlSchemeElements.setSchemeElementArray(ses.toArray(new XmlSchemeElement[ses.size()]));
		}
		
		if (!this.links.isEmpty()) {
			XmlSchemeLinkSeq xmlLinks = xmlSE.addNewSchemeLinks();
			
			Collection<XmlSchemeLink> ls = new ArrayList<XmlSchemeLink>(this.links.size());
			for (Object link : this.links) {
				ls.add(((Link)link).toXMLObject(uid, this.kind.equals("SCHEMED")));
			}
			xmlLinks.setSchemeLinkArray(ls.toArray(new XmlSchemeLink[ls.size()]));
		}
		return xmlSE;
	}

	public String getWellId() {
		return this.wellId;
	}
}
