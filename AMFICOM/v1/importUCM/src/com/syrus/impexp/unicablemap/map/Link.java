/**
 * $Id: Link.java,v 1.1 2005/08/30 08:25:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.impexp.unicablemap.map;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLink;
import com.syrus.impexp.unicablemap.UniCableMapDatabase;
import com.syrus.impexp.unicablemap.UniCableMapLink;
import com.syrus.impexp.unicablemap.UniCableMapLinkType;
import com.syrus.impexp.unicablemap.UniCableMapObject;
import com.syrus.impexp.unicablemap.UniCableMapParameter;

public class Link {

	private String street;
	private String city;
	private String building;
	private String name;
	private String description;
	private String uid;
	private String proto;
	private String endNodeId;
	private String startNodeId;
	private double length;
	private Collection<NodeLink> nodeLinks;
	
	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String uid) {
		this.uid = uid;
	}

	private void setPhysicalLinkTypeCodename(String proto) {
		this.proto = proto;
	}

	public String getPhysicalLinkTypeCodename() {
		return this.proto;
	}

	private void setLength(double length) {
		this.length = length;
	}

	private void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}

	private void setStartNodeId(String startNodeId) {
		this.startNodeId = startNodeId;
	}

	public String getId() {
		return this.uid;
	}

	public String getEndNodeId() {
		return this.endNodeId;
	}

	public double getLength() {
		return this.length;
	}

	public String getStartNodeId() {
		return this.startNodeId;
	}

	public String getBuilding() {
		return this.building;
	}

	public String getCity() {
		return this.city;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public String getStreet() {
		return this.street;
	}

	public void addNodeLink(NodeLink nodeLink) {
		this.nodeLinks.add(nodeLink);
	}

	public Collection<NodeLink> getNodeLinks() {
		return this.nodeLinks;
	}

	public XmlPhysicalLink getXmlPhysicalLink() {
		XmlPhysicalLink xmlPhysicalLink = XmlPhysicalLink.Factory.newInstance();

		XmlIdentifier xmlId = xmlPhysicalLink.addNewId();
		xmlId.setStringValue(this.uid);
		xmlPhysicalLink.setName(this.name);
		xmlPhysicalLink.setDescription(this.description);
		xmlPhysicalLink.setPhysicalLinkTypeCodename(this.proto);
		xmlPhysicalLink.setCity(this.city);
		xmlPhysicalLink.setStreet(this.street);
		xmlPhysicalLink.setBuilding(this.building);
		xmlId = xmlPhysicalLink.addNewStartNodeId();
		xmlId.setStringValue(this.startNodeId);
		xmlId = xmlPhysicalLink.addNewEndNodeId();
		xmlId.setStringValue(this.endNodeId);

		if(this.name == null || this.name.length() == 0 || this.startNodeId == null || this.startNodeId.length() == 0 || this.endNodeId == null || this.endNodeId.length() == 0) {
			System.out.println("null at " + xmlPhysicalLink);
			this.name = this.uid;
			xmlPhysicalLink.setName(this.name);
		}
		
		if(this.description == null) {
			this.description = this.uid;
			xmlPhysicalLink.setDescription(this.description);
		}

		return xmlPhysicalLink;
	}

	public static Link parseLink(UniCableMapDatabase ucmDatabase, UniCableMapObject ucmObject, String proto) throws SQLException {
		Link link = new Link();

		String street = "";
		String city = "";
		String building = "";

		for(UniCableMapLink ucmLink : ucmDatabase.getParents(ucmObject)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS))
				link.setStartNodeId(String.valueOf(ucmLink.parent.un));
			else
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS))
				link.setEndNodeId(String.valueOf(ucmLink.parent.un));
			
//			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
//				if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_STREET))
//				{
//					street = ucmLink.parent.text;
//					Collection streetParents = ucmDatabase.getParents(ucmLink.parent);
//					for(Iterator it3 = streetParents.iterator(); it3.hasNext();)
//					{
//						UniCableMapLink ucmLink1 = (UniCableMapLink )it3.next();
//						if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
//							if(ucmLink1.parent.typ.text.equals(UniCableMapType.UCM_CITY))
//								city = ucmLink1.parent.text;
//					}
//				}
		}

		for(UniCableMapLink ucmLink : ucmDatabase.getChildren(ucmObject)) {
			// nothing
		}
		
		for(UniCableMapParameter param : ucmObject.buf.params) {
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_HIDDEN))
				link.setName(param.value);
			else
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
				String dval = param.value.replace(',', '.');
				link.setLength(Double.parseDouble(dval));
			}
		}

		link.nodeLinks = new LinkedList<NodeLink>();
		link.setId(String.valueOf(ucmObject.un));
		link.setDescription("");
		link.setPhysicalLinkTypeCodename(proto);

		link.setCity(city);
		link.setStreet(street);
		link.setBuilding(building);
		
		return link;
	}

}
