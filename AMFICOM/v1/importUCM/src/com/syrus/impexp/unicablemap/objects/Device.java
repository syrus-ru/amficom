/*-
 * $Id: Device.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.SchemeDevice;
import com.syrus.amficom.scheme.xml.SchemePort;
import com.syrus.amficom.scheme.xml.SchemePorts;

public class Device {
	private String id;
	private String name;
	private Collection<Port> ports = new LinkedList<Port>();
	
	public Device(String id) {
		this.id = id;
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
	
	public SchemeDevice toXMLObject() {
		SchemeDevice xmlSD = SchemeDevice.Factory.newInstance();
		UID uid = xmlSD.addNewUid();
		uid.setStringValue(this.id);
		xmlSD.setName(this.name);
		
		if (!this.ports.isEmpty()) {
			SchemePorts xmlSchemePorts = xmlSD.addNewSchemecableports();
			
			Collection<SchemePort> sps = new ArrayList<SchemePort>(this.ports.size());
			for (Object port : this.ports) {
				sps.add(((Port)port).toXMLObject());
			}
			xmlSchemePorts.setSchemeportArray(sps.toArray(new SchemePort[sps.size()]));	
		}
		return xmlSD;
	}
}
