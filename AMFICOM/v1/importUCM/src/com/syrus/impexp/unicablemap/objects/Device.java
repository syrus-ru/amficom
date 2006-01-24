/*-
 * $Id: Device.java,v 1.6 2006/01/24 07:44:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCablePortSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeDevice;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePort;
import com.syrus.AMFICOM.scheme.xml.XmlSchemePortSeq;

public class Device {
	private String id;
	private String name;
	private String parentId;
	private Collection<Port> cablePorts = new LinkedList<Port>();
	private Collection<SimplePort> ports = new LinkedList<SimplePort>();
	
	public Device(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public void addCablePort(Port port) {
		this.cablePorts.add(port);
	}
	
	public void addPort(SimplePort port) {
		this.ports.add(port);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<Port> getPorts() {
		return this.cablePorts;
	}
		
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public XmlSchemeDevice toXMLObject() {
		XmlSchemeDevice xmlSD = XmlSchemeDevice.Factory.newInstance();
		XmlIdentifier uid = xmlSD.addNewId();
		uid.setStringValue(this.id);
		xmlSD.setId(uid);
		
		if (this.name.length() > 32) {
			System.out.println("device name (" + this.name + ") length greater then 32 symbols. coping to description and cutting...");
			xmlSD.setDescription(this.name);
			this.name = this.name.substring(0, 31);
		}
		
		xmlSD.setName(this.name);
		
		XmlIdentifier elementId = xmlSD.addNewParentSchemeElementId();
		elementId.setStringValue(this.parentId);
		xmlSD.setParentSchemeElementId(elementId);
		
		if (!this.cablePorts.isEmpty()) {
			XmlSchemeCablePortSeq xmlSchemePorts = xmlSD.addNewSchemeCablePorts();
			
			Collection<XmlSchemeCablePort> sps = new ArrayList<XmlSchemeCablePort>(this.cablePorts.size());
			for (Object port : this.cablePorts) {
				sps.add(((Port)port).toXMLObject());
			}
			xmlSchemePorts.setSchemeCablePortArray(sps.toArray(new XmlSchemeCablePort[sps.size()]));	
		}
		
		if (!this.ports.isEmpty()) {
			XmlSchemePortSeq xmlSchemePorts = xmlSD.addNewSchemePorts();
			
			Collection<XmlSchemePort> sps = new ArrayList<XmlSchemePort>(this.ports.size());
			for (Object port : this.ports) {
				sps.add(((SimplePort)port).toXMLObject());
			}
			xmlSchemePorts.setSchemePortArray(sps.toArray(new XmlSchemePort[sps.size()]));	
		}
		return xmlSD;
	}
}
