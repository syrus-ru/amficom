package com.syrus.impexp.unicablemap;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.MapsDocument;
import com.syrus.AMFICOM.map.xml.XmlCollector;
import com.syrus.AMFICOM.map.xml.XmlCollectorSeq;
import com.syrus.AMFICOM.map.xml.XmlMap;
import com.syrus.AMFICOM.map.xml.XmlMapLibraryEntrySeq;
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
 * @version $Revision: 1.13 $, $Date: 2005/09/30 09:34:37 $
 * @module mapviewclient_v1
 */
public class UniCableMapExportCommand 
{
	UniCableMapDatabase ucmDatabase;

	String fileName;

	public UniCableMapExportCommand(UniCableMapDatabase ucmDatabase, String fileName)
	{
		this.ucmDatabase = ucmDatabase;
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
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void saveXML(String fileName) throws SQLException {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSavePrettyPrintIndent(2);
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/map/xml", "map");
		prefixes.put("http://syrus.com/AMFICOM/general/xml", "general");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		xmlOptions.setSaveAggressiveNamespaces();

		MapsDocument doc = 
			MapsDocument.Factory.newInstance(xmlOptions);

		XmlMapSeq xmlMaps = doc.addNewMaps();

		XmlMap xmlMap = xmlMaps.addNewMap();

		XmlIdentifier uid = xmlMap.addNewId();
		uid.setStringValue("1");
		xmlMap.setName("UCM");
		xmlMap.setDescription("");
		xmlMap.setImportType("ucm");

		XmlMapLibraryEntrySeq xmlMapLibraryEntries = xmlMap.addNewMapLibraryEntries();
		xmlMapLibraryEntries.addMapLibraryEntry("UCM");
		XmlTopologicalNodeSeq xmlTopologicalNodes = xmlMap.addNewTopologicalNodes();
		XmlSiteNodeSeq xmlSiteNodes = xmlMap.addNewSiteNodes();
		XmlPhysicalLinkSeq xmlPhysicalLinks = xmlMap.addNewPhysicalLinks();
		XmlNodeLinkSeq xmlNodeLinks = xmlMap.addNewNodeLinks();
		XmlCollectorSeq xmlCollectors = xmlMap.addNewCollectors();

		Collection<Site> importSites = new LinkedList<Site>();
		Collection<Link> importLinks = new LinkedList<Link>();
		Collection<NodeLink> importNodeLinks = new LinkedList<NodeLink>();
		Collection<Collector> importCollectors = new LinkedList<Collector>();

		System.out.print("Scanning wells... ");
		long t1 = System.currentTimeMillis();
		Collection<UniCableMapObject> ucmWells = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL));
		System.out.print(ucmWells.size() + " objects... ");
		createSites(importSites, ucmWells, "defaultwell");
		long t2 = System.currentTimeMillis();
		System.out.println(" Done in " + (t2 - t1) + " ms!");

		System.out.print("Scanning piquets... ");
		Collection<UniCableMapObject> ucmPiquets = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_PIQUET));
		System.out.print(ucmPiquets.size() + " objects... ");
		createSites(importSites, ucmPiquets, "defaultpiquet");
		long t3 = System.currentTimeMillis();
		System.out.println(" Done in " + (t3 - t2) + " ms!");

		System.out.print("Scanning buildings... ");
		Collection<UniCableMapObject> ucmBuildings = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_BUILDING_PLAN));
		System.out.print(ucmBuildings.size() + " objects... ");
		createSites(importSites, ucmBuildings, "defaultbuilding");
		long t4 = System.currentTimeMillis();
		System.out.println(" Done in " + (t4 - t3) + " ms!");

		System.out.print("Scanning cable inlets... ");
		Collection<UniCableMapObject> ucmCableinlets = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
		System.out.print(ucmCableinlets.size() + " objects... ");
		createCableInlets(importSites, importLinks, importNodeLinks, ucmCableinlets, "defaultcableinlet");
		long t5 = System.currentTimeMillis();
		System.out.println(" Done in " + (t5 - t4) + " ms!");

		System.out.print("Scanning tunnels... ");
		Collection<UniCableMapObject> ucmTunnels = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
		System.out.print(ucmTunnels.size() + " objects... ");
		createLinks(importLinks, importNodeLinks, ucmTunnels, "defaulttunnel");
		long t6 = System.currentTimeMillis();
		System.out.println(" Done in " + (t6 - t5) + " ms!");

		System.out.print("Scanning collectors... ");
		Collection<UniCableMapObject> ucmCollectors = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR));
		System.out.print(ucmCollectors.size() + " objects... ");
		createCollectors(importCollectors, importLinks, importNodeLinks, ucmCollectors, "defaultcollector");
		long t7 = System.currentTimeMillis();
		System.out.println(" Done in " + (t7 - t6) + " ms!");

		System.out.print("Filling XML document... ");
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

		long t8 = System.currentTimeMillis();
		System.out.println(" Done in " + (t8 - t7) + " ms!");
		
		System.out.print("Validating XML document... ");
		boolean isXmlValid = UCMParser.validateXml(doc);
		long t9 = System.currentTimeMillis();
		System.out.println(" Done in " + (t9 - t8) + " ms!");
		if(isXmlValid) {
//		if(true) {
			System.out.print("Writing XML document... ");
			File f = new File(fileName);

			try {
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			long t10 = System.currentTimeMillis();
			System.out.println("\nXML Instance Document saved at : " + f.getPath() + " in " + (t10 - t9) + " ms!");
		}
	}

	private void createCableInlets(Collection<Site> sites, Collection<Link> links, Collection<NodeLink> nodeLinks, Collection<UniCableMapObject> objects, String proto) throws SQLException {
		int i = 0;
		for(UniCableMapObject ucmObject : objects) {
//			if(i++ > 1) 
//				break;
			Site cableInlet = Site.parseSite(this.ucmDatabase, ucmObject, proto);

//			Site building = cableInlet.getBuildingPlan();
			if(cableInlet.getAttachmentSiteNodeId() != null) {
//				building.setBuilding(cableInlet.getBuilding());
//				building.setCity(cableInlet.getCity());
//				building.setId("site" + building.getId());
//				building.setSiteNodeTypeCodename("defaultbuilding");
//				building.setStreet(cableInlet.getStreet());
//				sites.add(building);
//	
//				cableInlet.setAttachmentSiteNodeId(building.getId());
//				
				Link link = new Link();
				link.setBuilding(cableInlet.getBuilding());
				link.setCity(cableInlet.getCity());
				link.setDescription("");
				link.setId(cableInlet.getAttachmentSiteNodeId() + "indoor" + cableInlet.getId());
				link.setStartNodeId(cableInlet.getAttachmentSiteNodeId());
				link.setEndNodeId(cableInlet.getId());
				link.setPhysicalLinkTypeCodename("defaultindoor");
				link.setLength(0.0D);
				link.setName("");
				links.add(link);

				NodeLink nodeLink = NodeLink.createNodeLink(link);
				nodeLinks.add(nodeLink);
			}
			// sates shold be written to xml after attachment building
			// for correct export
			sites.add(cableInlet);
		}
	}

	private void createSites(Collection<Site> sites, Collection<UniCableMapObject> objects, String proto) throws SQLException {
		int i = 0;
		for(UniCableMapObject ucmObject : objects) {
//			if(i++ > 1) 
//				break;
			Site site = Site.parseSite(this.ucmDatabase, ucmObject, proto);
			sites.add(site);
		}
	}

	private void createLinks(Collection<Link> links, Collection<NodeLink> nodeLinks, Collection<UniCableMapObject> objects, String proto) throws SQLException {
		int i = 0;
		for(UniCableMapObject ucmObject : objects) {
//			if(i++ > 1) 
//				break;
			Link link = Link.parseLink(this.ucmDatabase, ucmObject, proto);
			links.add(link);
			NodeLink nodeLink = NodeLink.createNodeLink(link);
			nodeLinks.add(nodeLink);
		}
	}

	private void createCollectors(Collection<Collector> collectors, Collection<Link> links, Collection<NodeLink> nodeLinks, Collection<UniCableMapObject> objects, String linkProto) throws SQLException {
		int i = 0;
		for(UniCableMapObject ucmObject : objects) {
//			if(i++ > 1) 
//				break;
			Collector collector = Collector.parseCollector(this.ucmDatabase, ucmObject, linkProto);
			collectors.add(collector);
			links.addAll(collector.getLinks());
			nodeLinks.addAll(collector.getNodeLinks());
		}
	}

}
