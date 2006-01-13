/*-
 * $Id: ChannelingItem.java,v 1.10 2006/01/13 14:52:15 stas Exp $
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
	private int number;
	private String tunnelId;
	private String startSiteId;
	private String endSiteId;
	private int rowX;
	private int placeY;
	private String parentId;
	private String blockId;
	
	private transient double length = 0;
	
	public ChannelingItem(String id) {
		this.id = id;
	}

	public void setEndSiteId(String endSiteId) {
		assert endSiteId != null;
		this.endSiteId = endSiteId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setPlaceY(int placeY) {
		this.placeY = placeY;
	}

	public void setRowX(int rowX) {
		this.rowX = rowX;
	}

	public void setStartSiteId(String startSiteId) {
		assert startSiteId != null;
		this.startSiteId = startSiteId;
	}

	public void setTunnelId(String tunnelId) {
		assert tunnelId != null;
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
			if (this.blockId == null) {
//				System.err.println("blockId is null for " + this.id + " ( tunnel " + this.tunnelId + ")");
			} else {
				XmlIdentifier bluid = xmlCCI.addNewPipeBlockId();
				bluid.setStringValue(this.blockId);
			}
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

	public String getEndSiteId() {
		return this.endSiteId;
	}

	public String getStartSiteId() {
		return this.startSiteId;
	}

	public String getTunnelId() {
		return this.tunnelId;
	}

	public String getId() {
		return this.id;
	}

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
}
