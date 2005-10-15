/**
 * $Id: Link.java,v 1.8 2005/10/15 13:41:52 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.impexp.unicablemap.map;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLink;
import com.syrus.AMFICOM.map.xml.XmlPipeBlock;
import com.syrus.AMFICOM.map.xml.XmlPipeBlockSeq;
import com.syrus.impexp.unicablemap.UniCableMapDatabase;
import com.syrus.impexp.unicablemap.UniCableMapLink;
import com.syrus.impexp.unicablemap.UniCableMapLinkType;
import com.syrus.impexp.unicablemap.UniCableMapObject;
import com.syrus.impexp.unicablemap.UniCableMapParameter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Link {

	private String street = "";
	private String city = "";
	private String building = "";
	private String name;
	private String description = "";
	private String uid;
	private String proto;
	private String endNodeId;
	private String startNodeId;
	private double length = 0.0D;
	private Collection<NodeLink> nodeLinks = new LinkedList<NodeLink>();

	private Map<String, Block> bindingBlocks = new HashMap<String, Block>();
	
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

	public void setPhysicalLinkTypeCodename(String proto) {
		this.proto = proto;
	}

	public String getPhysicalLinkTypeCodename() {
		return this.proto;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}

	public void setStartNodeId(String startNodeId) {
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
		xmlId = xmlPhysicalLink.addNewStartNodeId();
		xmlId.setStringValue(this.startNodeId);
		xmlId = xmlPhysicalLink.addNewEndNodeId();
		xmlId.setStringValue(this.endNodeId);
		xmlPhysicalLink.setCity(this.city);
		xmlPhysicalLink.setStreet(this.street);
		xmlPhysicalLink.setBuilding(this.building);

		if(this.bindingBlocks.size() > 0) {
			final XmlPipeBlockSeq xmlPipeBlocks = xmlPhysicalLink.addNewPipeBlocks();
			XmlPipeBlock[] pipeBlockArray = new XmlPipeBlock[this.bindingBlocks.size()];
			int i = 0;
			for(Block block : this.bindingBlocks.values()) {
				pipeBlockArray[i++] = block.getXmlPipeBlock();
			}
			xmlPipeBlocks.setPipeBlockArray(pipeBlockArray);
		}
		
		if(this.name == null || this.name.length() == 0 || this.startNodeId == null || this.startNodeId.length() == 0 || this.endNodeId == null || this.endNodeId.length() == 0) {
			System.out.println("link " + this.uid + " has no name!");
			this.name = this.uid;
			xmlPhysicalLink.setName(this.name);
		}
		
		if(this.description == null) {
			this.description = this.uid;
			xmlPhysicalLink.setDescription(this.description);
		}

		if(this.bindingBlocks.size()> 0) {
			// add blocks
		}
		
		return xmlPhysicalLink;
	}

	public static Link parseLink(UniCableMapDatabase ucmDatabase, UniCableMapObject ucmObject, String proto) throws SQLException {
		Link link = new Link();

		String street = "";
		String city = "";
		String building = "";
		String description = "";
//		if(ucmObject.un == 597402L) {
//			// link with 2 blocks
//			int a = 0;
//		}

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

		int blocksscanned = 0;
		for(UniCableMapLink ucmLink : ucmDatabase.getChildren(ucmObject)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)
					&& ucmLink.child.typ.text.equals(UniCableMapType.UCM_TUNNEL_PROFILE)) {
				for(UniCableMapLink ucmLink2 : ucmDatabase.getChildren(ucmLink.child)) {
					if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)
							&& ucmLink2.child.typ.text.equals(UniCableMapType.UCM_BLOCK)) {
						Block block = Block.parseBlock(ucmDatabase, ucmLink2.child, blocksscanned++);
						link.bindingBlocks.put(block.getId(), block);
						description += block.getComment() + "\n";
					}
				}
			}
		}

		if(blocksscanned > 1) {
			System.out.println("Внимание! Тоннель '" + ucmObject.text + "' ("
					+ ucmObject.un + ") содержит блоков труб: "
					+ blocksscanned + ".");
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

		link.setId(String.valueOf(ucmObject.un));
		link.setDescription(description);
		link.setPhysicalLinkTypeCodename(proto);

		link.setCity(city);
		link.setStreet(street);
		link.setBuilding(building);
		
		return link;
	}

	public Map<String, Block> getBindingBlocks() {
		return Collections.unmodifiableMap(this.bindingBlocks);
	}


}
