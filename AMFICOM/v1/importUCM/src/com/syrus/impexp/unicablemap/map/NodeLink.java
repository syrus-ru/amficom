/**
 * $Id: NodeLink.java,v 1.2 2005/08/30 12:41:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.impexp.unicablemap.map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.XmlNodeLink;

public class NodeLink {

	private double length = 0.0D;
	private String uid;
	private String physicalLinkId;
	private String endNodeId;
	private String startNodeId;

	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void setPhysicalLinkId(String physicalLinkId) {
		this.physicalLinkId = physicalLinkId;
	}

	public void setStartNodeId(String startNodeId) {
		this.startNodeId = startNodeId;
	}

	public void setId(String uid) {
		this.uid = uid;
	}

	public String getEndNodeId() {
		return this.endNodeId;
	}

	public double getLength() {
		return this.length;
	}

	public String getPhysicalLinkId() {
		return this.physicalLinkId;
	}

	public String getStartNodeId() {
		return this.startNodeId;
	}

	public String getId() {
		return this.uid;
	}

	public XmlNodeLink getXmlNodeLink() {
		XmlNodeLink xmlNodeLink = XmlNodeLink.Factory.newInstance();

		XmlIdentifier xmlId = xmlNodeLink.addNewId();
		xmlId.setStringValue(this.uid);

		xmlNodeLink.setLength(this.length);

		xmlId = xmlNodeLink.addNewPhysicalLinkId();
		xmlId.setStringValue(this.physicalLinkId);

		xmlId = xmlNodeLink.addNewStartNodeId();
		xmlId.setStringValue(this.startNodeId);

		xmlId = xmlNodeLink.addNewEndNodeId();
		xmlId.setStringValue(this.endNodeId);

		return xmlNodeLink;
	}

	public static NodeLink createNodeLink(Link link) {
		NodeLink nodeLink = new NodeLink();
		
		nodeLink.setId(link.getId() + "nodelink");
		nodeLink.setPhysicalLinkId(link.getId());
		nodeLink.setStartNodeId(link.getStartNodeId());
		nodeLink.setEndNodeId(link.getEndNodeId());
		nodeLink.setLength(link.getLength());
		
		link.addNodeLink(nodeLink);
		return nodeLink;
	}

}
