/**
 * $Id: Collector.java,v 1.1 2005/08/30 08:25:47 krupenn Exp $
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
import com.syrus.AMFICOM.general.xml.XmlIdentifierSeq;
import com.syrus.AMFICOM.map.xml.XmlCollector;
import com.syrus.impexp.unicablemap.UniCableMapDatabase;
import com.syrus.impexp.unicablemap.UniCableMapLink;
import com.syrus.impexp.unicablemap.UniCableMapLinkType;
import com.syrus.impexp.unicablemap.UniCableMapObject;
import com.syrus.impexp.unicablemap.UniCableMapParameter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Collector {

	private String uid;
	private String name;
	private String description;
	private Collection<Link> links;
	private Collection<NodeLink> nodeLinks;

	private void setId(String uid) {
		this.uid = uid;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	public Collection<Link> getLinks() {
		return this.links;
	}

	public Collection<NodeLink> getNodeLinks() {
		return this.nodeLinks;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public String getId() {
		return this.uid;
	}

	public XmlCollector getXmlCollector() {
		XmlCollector xmlCollector = XmlCollector.Factory.newInstance();

		XmlIdentifier xmlId = xmlCollector.addNewId();
		xmlId.setStringValue(this.uid);
		xmlCollector.setDescription(this.description);
		xmlCollector.setName(this.name);

		XmlIdentifierSeq xmlPhysicalLinkUIds = xmlCollector.addNewPhysicalLinkIds();

		for (Link link : this.links) {
			XmlIdentifier xmlPhysicalLinkUId = xmlPhysicalLinkUIds.addNewId();
			xmlPhysicalLinkUId.setStringValue(link.getId());
		}
		return xmlCollector;
	}

	public static Collector parseCollector(UniCableMapDatabase ucmDatabase, UniCableMapObject ucmObject, String linkProto) throws SQLException {
		Collector collector = new Collector();
		
		collector.setId(String.valueOf(ucmObject.un));
		collector.setDescription(ucmObject.text);
		collector.links = new LinkedList<Link>();
		collector.nodeLinks = new LinkedList<NodeLink>(); 

		for(UniCableMapLink ucmLink : ucmDatabase.getParents(ucmObject)) {
			// nothing
		}

		for(UniCableMapLink ucmLink : ucmDatabase.getChildren(ucmObject)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
				if(ucmLink.child.typ.text.equals(UniCableMapType.UCM_COLLECTOR_FRAGMENT)) {
					Link link = Link.parseLink(ucmDatabase, ucmLink.child, linkProto);
					collector.links.add(link);
					NodeLink nodeLink = NodeLink.createNodeLink(link);
					collector.nodeLinks.add(nodeLink);
				}
		}
		
		for(UniCableMapParameter param : ucmObject.buf.params) {
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_MANDATORY))
				collector.setName(param.value);
		}

		return collector;
	}

}
