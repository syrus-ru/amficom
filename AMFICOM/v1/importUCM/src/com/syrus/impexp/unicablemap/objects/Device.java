/*-
 * $Id: Device.java,v 1.2 2005/09/07 12:47:46 stas Exp $
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

public class Device {
	private String id;
	private String name;
	private String parentId;
	private Collection<Port> ports = new LinkedList<Port>();
	
	public Device(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	public void addPort(Port port) {
		this.ports.add(port);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<Port> getPorts() {
		return this.ports;
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
			System.err.println("device");
		}
		
		xmlSD.setName(this.name);
		
		XmlIdentifier elementId = xmlSD.addNewParentSchemeElementId();
		elementId.setStringValue(this.parentId);
		xmlSD.setParentSchemeElementId(elementId);
		
		if (!this.ports.isEmpty()) {
			XmlSchemeCablePortSeq xmlSchemePorts = xmlSD.addNewSchemeCablePorts();
			
			Collection<XmlSchemeCablePort> sps = new ArrayList<XmlSchemeCablePort>(this.ports.size());
			for (Object port : this.ports) {
				sps.add(((Port)port).toXMLObject());
			}
			xmlSchemePorts.setSchemeCablePortArray(sps.toArray(new XmlSchemeCablePort[sps.size()]));	
		}
		return xmlSD;
	}
}
