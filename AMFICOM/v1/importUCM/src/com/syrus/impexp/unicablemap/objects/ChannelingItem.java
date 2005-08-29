/*-
 * $Id: ChannelingItem.java,v 1.1 2005/08/29 13:04:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap.objects;

import java.math.BigInteger;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.scheme.xml.CableChannelingItem;

public class ChannelingItem {
	private int id;
	private Integer number;
	private Integer tunnelId;
	private Integer startSiteId;
	private Integer endSiteId;
	private Integer rowX;
	private Integer placeY;
	
	private transient double length = 0;
	
	public ChannelingItem(int id) {
		this.id = id;
	}

	public void setEndSiteId(Integer endSiteId) {
		this.endSiteId = endSiteId;
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

	public void setStartSiteId(Integer startSiteId) {
		this.startSiteId = startSiteId;
	}

	public void setTunnelId(Integer tunnelId) {
		this.tunnelId = tunnelId;
	}

	public double getLength() {
		return this.length;
	}

	public void setLength(double length) {
		this.length = length;
	}
	
	public CableChannelingItem toXMLObject() {
		CableChannelingItem xmlCCI = CableChannelingItem.Factory.newInstance();
		
		xmlCCI.setNumber(BigInteger.valueOf(this.number));
		UID pluid = xmlCCI.addNewPhysicallinkuid();
		if (this.tunnelId == null) {
			System.err.println("tunnelId is null for " + this);
		} else {
			pluid.setStringValue(Integer.toString(this.tunnelId));
		}
		
		UID ssuid = xmlCCI.addNewStartsitenodeuid();
		if (this.startSiteId == null) {
			System.err.println("startSiteId is null for " + this);
		} else {
			ssuid.setStringValue(Integer.toString(this.startSiteId));
		}
		
		UID esuid = xmlCCI.addNewEndsitenodeuid();
		if (this.endSiteId == null) {
			System.err.println("endSiteId is null for " + this);
		} else {
			esuid.setStringValue(Integer.toString(this.endSiteId));
		}
		
		xmlCCI.setRowX(BigInteger.valueOf(this.rowX));
		xmlCCI.setPlaceY(BigInteger.valueOf(this.placeY));
		
		return xmlCCI;
	}
}
