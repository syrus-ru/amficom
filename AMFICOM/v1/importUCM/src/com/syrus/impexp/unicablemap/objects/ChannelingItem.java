/*-
 * $Id: ChannelingItem.java,v 1.2 2005/09/07 12:47:46 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.XmlCableChannelingItem;

public class ChannelingItem {
	private String id;
	private Integer number;
	private String tunnelId;
	private String startSiteId;
	private String endSiteId;
	private Integer rowX;
	private Integer placeY;
	private String parentId;
	
	private transient double length = 0;
	
	public ChannelingItem(String id) {
		this.id = id;
	}

	public void setEndSiteId(String endSiteId) {
		this.endSiteId = endSiteId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setPlaceY(Integer placeY) {
		this.placeY = placeY;
	}

	public void setRowX(Integer rowX) {
		this.rowX = rowX;
	}

	public void setStartSiteId(String startSiteId) {
		this.startSiteId = startSiteId;
	}

	public void setTunnelId(String tunnelId) {
		this.tunnelId = tunnelId;
	}

	public double getLength() {
		return this.length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	
	public XmlCableChannelingItem toXMLObject() {
		XmlCableChannelingItem xmlCCI = XmlCableChannelingItem.Factory.newInstance();
		XmlIdentifier uid = xmlCCI.addNewId();
		uid.setStringValue(String.valueOf(this.id));
		
		xmlCCI.setStartSpare(0);
		xmlCCI.setEndSpare(0);
		xmlCCI.setRowX(this.rowX);
		xmlCCI.setPlaceY(this.placeY);
		xmlCCI.setSequentialNumber(this.number);
		
		XmlIdentifier pluid = xmlCCI.addNewPhysicalLinkId();
		if (this.tunnelId == null) {
			System.err.println("tunnelId is null for " + this);
		} else {
			pluid.setStringValue(this.tunnelId);
		}
		
		XmlIdentifier ssuid = xmlCCI.addNewStartSiteNodeId();
		if (this.startSiteId == null) {
			System.err.println("startSiteId is null for " + this);
		} else {
			ssuid.setStringValue(this.startSiteId);
		}
		
		XmlIdentifier esuid = xmlCCI.addNewEndSiteNodeId();
		if (this.endSiteId == null) {
			System.err.println("endSiteId is null for " + this);
		} else {
			esuid.setStringValue(this.endSiteId);
		}

		XmlIdentifier cableLinkId = xmlCCI.addNewParentSchemeCableLinkId();
		cableLinkId.setStringValue(this.parentId);
		xmlCCI.setParentSchemeCableLinkId(cableLinkId);
		
		return xmlCCI;
	}
}
