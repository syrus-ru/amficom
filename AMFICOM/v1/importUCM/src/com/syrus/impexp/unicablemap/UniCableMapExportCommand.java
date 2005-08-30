package com.syrus.impexp.unicablemap;
import static com.syrus.impexp.unicablemap.UniCableMapExportCommand.COLLECTOR_TYPE;
import static com.syrus.impexp.unicablemap.UniCableMapExportCommand.LINK_TYPE;
import static com.syrus.impexp.unicablemap.UniCableMapExportCommand.NODELINK_TYPE;
import static com.syrus.impexp.unicablemap.UniCableMapExportCommand.SITE_TYPE;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.MapsDocument;
import com.syrus.AMFICOM.map.xml.XmlCollector;
import com.syrus.AMFICOM.map.xml.XmlCollectorSeq;
import com.syrus.AMFICOM.map.xml.XmlMap;
import com.syrus.AMFICOM.map.xml.XmlMapSeq;
import com.syrus.AMFICOM.map.xml.XmlNodeLink;
import com.syrus.AMFICOM.map.xml.XmlNodeLinkSeq;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLink;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkSeq;
import com.syrus.AMFICOM.map.xml.XmlSiteNode;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeSeq;
import com.syrus.AMFICOM.map.xml.XmlTopologicalNodeSeq;
import com.syrus.impexp.unicablemap.map.Collector;
import com.syrus.impexp.unicablemap.map.Link;
import com.syrus.impexp.unicablemap.map.NodeLink;
import com.syrus.impexp.unicablemap.map.Site;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/08/30 08:25:47 $
 * @module mapviewclient_v1
 */
public class UniCableMapExportCommand extends UCMExportCommand 
{
	public static final String MAP_TYPE = "map";
	public static final String MARK_TYPE = "mapmarkelement";
	public static final String SITE_TYPE = "mapsiteelement";
	public static final String NODE_TYPE = "mapnodeelement";
	public static final String NODELINK_TYPE = "mapnodelinkelement";
	public static final String COLLECTOR_TYPE = "mappipepathelement";
	public static final String LINK_TYPE = "maplinkelement";

	String fileName;

	public UniCableMapExportCommand(UniCableMapDatabase ucmDatabase, String fileName)
	{
		super(ucmDatabase);
		this.fileName = fileName;
	}

	public void execute()
	{
		if(this.fileName == null)
			return;
		File file = new File(this.fileName);

		String ext = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf("."));

		if(ext == null) {
			ext = ".xml";
		}

		try {
			if(ext.equals(".xml")) {
				saveXML(this.fileName);
			}
//			else if(ext.equals(".esf")) {
//				saveESF(this.fileName);
//			}
			setResult(Command.RESULT_OK);
		}
		catch (SQLException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
	}
	
	private void saveXML(String fileName) throws SQLException {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/map/xml", "map");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);

		MapsDocument doc = 
			MapsDocument.Factory.newInstance(xmlOptions);

		XmlMapSeq xmlMaps = doc.addNewMaps();

		XmlMap xmlMap = xmlMaps.addNewMap();

		XmlIdentifier uid = xmlMap.addNewId();
		uid.setStringValue("1");
		xmlMap.setName("UCM");
		xmlMap.setDescription("");

		XmlTopologicalNodeSeq xmlTopologicalNodes = xmlMap.addNewTopologicalNodes();
		XmlSiteNodeSeq xmlSiteNodes = xmlMap.addNewSiteNodes();
		XmlPhysicalLinkSeq xmlPhysicalLinks = xmlMap.addNewPhysicalLinks();
		XmlNodeLinkSeq xmlNodeLinks = xmlMap.addNewNodeLinks();
		XmlCollectorSeq xmlCollectors = xmlMap.addNewCollectors();

		Collection<Site> importSites = new LinkedList<Site>();
		Collection<Link> importLinks = new LinkedList<Link>();
		Collection<NodeLink> importNodeLinks = new LinkedList<NodeLink>();
		Collection<Collector> importCollectors = new LinkedList<Collector>();

		Collection<UniCableMapObject> ucmWells = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL));
		createSites(importSites, ucmWells, "defaultwell");

		Collection<UniCableMapObject> ucmPiquets = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_PIQUET));
		createSites(importSites, ucmPiquets, "defaultpiquet");

		Collection<UniCableMapObject> ucmCableinlets = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
		createSites(importSites, ucmCableinlets, "defaultcableinlet");

		Collection<UniCableMapObject> ucmTunnels = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
		createLinks(importLinks, importNodeLinks, ucmTunnels, "defaulttunnel");

		Collection<UniCableMapObject> ucmCollectors = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR));
		createCollectors(importCollectors, importLinks, importNodeLinks, ucmCollectors, "defaultcollector");

		XmlSiteNode[] xmlSiteNodesArray = new XmlSiteNode[importSites.size()];
		int sitesIndex = 0;
		for(Site site : importSites) {
			xmlSiteNodesArray[sitesIndex++] = site.getXmlSiteNode();
		}

		XmlPhysicalLink[] xmlPhysicalLinksArray = new XmlPhysicalLink[importLinks.size()];
		int linksIndex = 0;
		for(Link link : importLinks) {
			xmlPhysicalLinksArray[linksIndex++] = link.getXmlPhysicalLink();
		}

		XmlNodeLink[] xmlNodeLinksArray = new XmlNodeLink[importNodeLinks.size()];
		int nodeLinksIndex = 0;
		for(NodeLink nodeLink : importNodeLinks) {
			xmlNodeLinksArray[nodeLinksIndex++] = nodeLink.getXmlNodeLink();
		}
		XmlCollector[] xmlCollectorsArray = new XmlCollector[importCollectors.size()];
		int collectorsIndex = 0;
		for(Collector collector : importCollectors) {
			xmlCollectorsArray[collectorsIndex++] = collector.getXmlCollector();
		}

		xmlSiteNodes.setSiteNodeArray(xmlSiteNodesArray);
		xmlPhysicalLinks.setPhysicalLinkArray(xmlPhysicalLinksArray);
		xmlNodeLinks.setNodeLinkArray(xmlNodeLinksArray);
		xmlCollectors.setCollectorArray(xmlCollectorsArray);

		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			File f = new File(fileName);

			try {
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("\nXML Instance Document saved at : " + f.getPath());
		}
	}

	private void createSites(Collection<Site> sites, Collection<UniCableMapObject> objects, String proto) throws SQLException {
		for(UniCableMapObject ucmObject : objects) {
			Site site = Site.parseSite(this.ucmDatabase, ucmObject, proto);
			sites.add(site);
		}
	}

	private void createLinks(Collection<Link> links, Collection<NodeLink> nodeLinks, Collection<UniCableMapObject> objects, String proto) throws SQLException {
		for(UniCableMapObject ucmObject : objects) {
			Link link = Link.parseLink(this.ucmDatabase, ucmObject, proto);
			links.add(link);
			NodeLink nodeLink = NodeLink.createNodeLink(link);
			nodeLinks.add(nodeLink);
		}
	}

	private void createCollectors(Collection<Collector> collectors, Collection<Link> links, Collection<NodeLink> nodeLinks, Collection<UniCableMapObject> objects, String linkProto) throws SQLException {
		for(UniCableMapObject ucmObject : objects) {
			Collector collector = Collector.parseCollector(this.ucmDatabase, ucmObject, linkProto);
			collectors.add(collector);
			links.addAll(collector.getLinks());
			nodeLinks.addAll(collector.getNodeLinks());
		}
	}

	private void saveESF(String fileName) throws SQLException {
			super.open(fileName);
	
			super.startObject(MAP_TYPE);
				super.put("description", "");
				super.put("name", "UCM");
				super.put("id", "1");
			super.endObject();
	
			Collection<UniCableMapObject> wells = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL));
			writeESFSites(wells, "well", "images/well.gif");

			Collection<UniCableMapObject> piquets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_PIQUET));
			writeESFSites(piquets, "piquet", "images/piquet.gif");
	
			Collection<UniCableMapObject> cableinlets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
			writeESFSites(cableinlets, "cableinlet", "images/cableinlet.gif");
	
			Collection<UniCableMapObject> tunnels = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
			writeESFLinks(tunnels, "tunnel");
	
			Collection<UniCableMapObject> collectors = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR));
			writeESFCollectors(collectors, "collector");
	
			super.close();

	}

	void writeESFSites(
			Collection<UniCableMapObject> objects,
			String proto,
			String imageId) throws SQLException {
		for(UniCableMapObject ucmObject : objects) {
			Site site = Site.parseSite(this.ucmDatabase, ucmObject, proto);

			writeESFSite(site);
		}
	}

	void writeESFLinks(Collection<UniCableMapObject> objects, String proto)
			throws SQLException {
		for(UniCableMapObject ucmObject : objects) {
			Link link = Link.parseLink(this.ucmDatabase, ucmObject, proto);
			NodeLink nodeLink = NodeLink.createNodeLink(link);

			writeESFLink(link);
			writeESFNodeLink(nodeLink);
		}
	}

	void writeESFCollectors(
			Collection<UniCableMapObject> objects,
			String linkProto) throws SQLException {
		for(UniCableMapObject ucmObject : objects) {
			Collector collector = Collector.parseCollector(
					this.ucmDatabase,
					ucmObject,
					linkProto);
			for(Link link : collector.getLinks()) {
				writeESFLink(link);
			}
			for(NodeLink nodeLink : collector.getNodeLinks()) {
				writeESFNodeLink(nodeLink);
			}

			writeESFCollector(collector);
		}
	}

	private void writeESFSite(Site site) {
		super.startObject(SITE_TYPE);

		super.put("id", site.getId());
		super.put("name", site.getName());
		super.put("description", site.getDescription());
		super.put("x", site.getX());
		super.put("y", site.getY());
		// super.put("image_id", image);
		super.put("proto_id", site.getSiteNodeTypeCodename());
		super.put("city", site.getCity());
		super.put("street", site.getStreet());
		super.put("building", site.getBuilding());

		super.endObject();
	}

	private void writeESFNodeLink(NodeLink nodeLink) {
		super.startObject(NODELINK_TYPE);

		super.put("id", nodeLink.getId());
		super.put("name", "");
		super.put("start_node_id", nodeLink.getStartNodeId());
		super.put("end_node_id", nodeLink.getEndNodeId());
		super.put("physical_link_id", nodeLink.getPhysicalLinkId());
		super.put("length", nodeLink.getLength());

		super.endObject();
	}

	private void writeESFLink(Link link) {
		List<String> nodeLinkIds = new LinkedList<String>();
		for(NodeLink nodeLink : link.getNodeLinks()) {
			nodeLinkIds.add(nodeLink.getId());
		}

		super.startObject(LINK_TYPE);

		super.put("id", link.getId());
		super.put("name", link.getName());
		super.put("description", link.getDescription());
		super.put("start_node_id", link.getStartNodeId());
		super.put("end_node_id", link.getEndNodeId());
		super.put("node_links", nodeLinkIds);
		super.put("proto_id", link.getPhysicalLinkTypeCodename());
		super.put("city", link.getCity());
		super.put("street", link.getStreet());
		super.put("building", link.getBuilding());

		super.endObject();
	}

	private void writeESFCollector(Collector collector) {
		List<String> physicalLinkIds = new LinkedList<String>();
		for(Link link : collector.getLinks()) {
			physicalLinkIds.add(link.getId());
		}

		super.startObject(COLLECTOR_TYPE);

		super.put("id", collector.getId());
		super.put("name", collector.getName());
		super.put("description", collector.getDescription());
		super.put("links", physicalLinkIds);

		super.endObject();
	}

}
