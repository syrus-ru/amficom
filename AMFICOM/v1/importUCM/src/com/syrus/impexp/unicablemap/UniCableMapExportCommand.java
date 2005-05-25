package com.syrus.impexp.unicablemap;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.mapinfo.coordsys.CoordSys;
import com.mapinfo.coordsys.Projection;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExportCommand;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/05/25 16:06:51 $
 * @module mapviewclient_v1
 */
public class UniCableMapExportCommand extends ExportCommand 
{
	public static final String MAP_TYPE = "map";
	public static final String MARK_TYPE = "mapmarkelement";
	public static final String SITE_TYPE = "mapsiteelement";
	public static final String NODE_TYPE = "mapnodeelement";
	public static final String NODELINK_TYPE = "mapnodelinkelement";
	public static final String COLLECTOR_TYPE = "mappipepathelement";
	public static final String LINK_TYPE = "maplinkelement";

	UniCableMapDatabase ucmDatabase;

	String fileName;

	static double xy[] = new double[2];
	static double longlat[] = new double[2];

	public static Projection projection;

	static {
		try 
		{
			CoordSys coordSys = CoordSys.createFromPRJ(
				"\"UTM Zone 37, Northern Hemisphere (WGS 84)\\pEPSG:32637\", 8, 104, 7, 39, 0, 0.9996, 500000, 0");
			projection = coordSys.getProjection();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
	
	public UniCableMapExportCommand(UniCableMapDatabase ucmDatabase, String fileName)
	{
		this.ucmDatabase = ucmDatabase;
		this.fileName = fileName;
	}

	double[] UTMtoGeo(double x, double y) {
		xy[0] = x;
		xy[1] = y;
		
		projection.inverse(xy, 0, longlat, 0, 1);

		return longlat;
	}

	void writeESFSites(Collection objects, String proto, String image) 
			throws SQLException {
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			UniCableMapObject ucmObject = (UniCableMapObject )it.next();

			String street = "";
			String city = "";
			String building = "";
			String name = "";
			String description = ucmObject.text;

			System.out.println(ucmObject.text);

			for(Iterator it2 = this.ucmDatabase.getParents(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
				System.out.println("Parent (" + ucmLink.mod.text + ") " + ucmLink.parent);
				
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
					if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_STREET))
					{
						street = ucmLink.parent.text;
						Collection streetParents = this.ucmDatabase.getParents(ucmLink.parent);
						for(Iterator it3 = streetParents.iterator(); it3.hasNext();)
						{
							UniCableMapLink ucmLink1 = (UniCableMapLink )it3.next();
							if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
								if(ucmLink1.parent.typ.text.equals(UniCableMapType.UCM_CITY))
									city = ucmLink1.parent.text;
						}
//						System.out.print("");
					}
			}

			for(Iterator it2 = this.ucmDatabase.getChildren(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
				System.out.println("Child " + ucmLink.mod.text + ") " + ucmLink.child);
			}
			
			for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();)
			{
				UniCableMapParameter param = (UniCableMapParameter )it2.next();
				System.out.println("Param " + param.realParameter + " = " + param.value);
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LABEL))
					name = param.value;
				else
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_ADDRESS))
					building = param.value;
			}

			double[] dp = UTMtoGeo(ucmObject.x0, ucmObject.y0);

			super.startObject(SITE_TYPE);

			super.put("id", String.valueOf(ucmObject.un));
			super.put("name", name);
			super.put("description", description);
			super.put("x", String.valueOf(dp[0]));
			super.put("y", String.valueOf(dp[1]));
			super.put("image_id", image);
			super.put("proto_id", proto);
			super.put("city", city);
			super.put("street", street);
			super.put("building", building);

			super.endObject();
		}
	}

	void writeESFLinks(Collection objects, String proto) 
			throws SQLException {
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			UniCableMapObject ucmObject = (UniCableMapObject )it.next();

			String street = "";
			String city = "";
			String building = "";
			String name = "";
			String description = ucmObject.text;
			Collection node_links = new LinkedList();
			String start_node_id = "";
			String end_node_id = "";
			double length = 0.0D;
			String node_link_id = String.valueOf(ucmObject.un) + "nodelink";

			System.out.println(ucmObject.text);

			for(Iterator it2 = this.ucmDatabase.getParents(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
				System.out.println("Parent (" + ucmLink.mod.text + ") " + ucmLink.parent);

				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS))
					start_node_id = String.valueOf(ucmLink.parent.un);
				else
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS))
					end_node_id = String.valueOf(ucmLink.parent.un);
				
//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
//					if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_STREET))
//					{
//						street = ucmLink.parent.text;
//						Collection streetParents = ucmDatabase.getParents(ucmLink.parent);
//						for(Iterator it3 = streetParents.iterator(); it3.hasNext();)
//						{
//							UniCableMapLink ucmLink1 = (UniCableMapLink )it3.next();
//							if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
//								if(ucmLink1.parent.typ.text.equals(UniCableMapType.UCM_CITY))
//									city = ucmLink1.parent.text;
//						}
////						System.out.print("");
//					}
			}

			for(Iterator it2 = this.ucmDatabase.getChildren(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
				System.out.println("Child " + ucmLink.mod.text + ") " + ucmLink.child);
			}
			
			for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();)
			{
				UniCableMapParameter param = (UniCableMapParameter )it2.next();
				System.out.println("Param " + param.realParameter + " = " + param.value);
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_HIDDEN))
					name = param.value;
				else
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
					String dval = param.value.replace(',', '.');
					length = Double.parseDouble(dval);
				}
			}

			node_links.add(node_link_id);

			super.startObject(LINK_TYPE);

			super.put("id", String.valueOf(ucmObject.un));
			super.put("name", name);
			super.put("description", description);
			super.put("start_node_id", start_node_id);
			super.put("end_node_id", end_node_id);
			super.put("node_links", node_links);
			super.put("proto_id", proto);
			super.put("city", city);
			super.put("street", street);
			super.put("building", building);

			super.endObject();

			super.startObject(NODELINK_TYPE);

			super.put("id", node_link_id);
			super.put("name", "");
			super.put("start_node_id", start_node_id);
			super.put("end_node_id", end_node_id);

			super.put("physical_link_id", String.valueOf(ucmObject.un));
			super.put("length", String.valueOf(length));

			super.endObject();

		}
	}

	void writeESFCollectors(Collection objects, String linkProto)
			throws SQLException {
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			UniCableMapObject ucmObject = (UniCableMapObject )it.next();

			String name = "";
			String description = ucmObject.text;
			Collection physical_links = new LinkedList();
			Collection physical_links_to_save = new LinkedList();

			System.out.println(ucmObject.text);

			for(Iterator it2 = this.ucmDatabase.getParents(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
				System.out.println("Parent (" + ucmLink.mod.text + ") " + ucmLink.parent);

//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS))
//					start_node_id = String.valueOf(ucmLink.parent.un);
//				else
//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS))
//					end_node_id = String.valueOf(ucmLink.parent.un);
			}

			for(Iterator it2 = this.ucmDatabase.getChildren(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
				System.out.println("Child " + ucmLink.mod.text + ") " + ucmLink.child);

				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
					if(ucmLink.child.typ.text.equals(UniCableMapType.UCM_COLLECTOR_FRAGMENT)) {
						physical_links.add(String.valueOf(ucmLink.child.un));
						physical_links_to_save.clear();
						physical_links_to_save.add(ucmLink.child);
						writeESFLinks(physical_links_to_save, linkProto);
					}
			}
			
			for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();)
			{
				UniCableMapParameter param = (UniCableMapParameter )it2.next();
				System.out.println("Param " + param.realParameter + " = " + param.value);
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_MANDATORY))
					name = param.value;
//				else
//				if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
//					String dval = param.value.replace(',', '.');
//					length = Double.parseDouble(dval);
//				}
			}

			super.startObject(COLLECTOR_TYPE);

			super.put("id", String.valueOf(ucmObject.un));
			super.put("name", name);
			super.put("description", description);
			super.put("links", physical_links);

			super.endObject();
		}
	}

	private void writeXMLSites(Collection xmlSiteNodesArray, Collection objects, String proto) throws SQLException {
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			UniCableMapObject ucmObject = (UniCableMapObject )it.next();

			String street = "";
			String city = "";
			String building = "";
			String name = "";
			String description = ucmObject.text;

//			System.out.println(ucmObject.text);

			for(Iterator it2 = this.ucmDatabase.getParents(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
//				System.out.println("Parent (" + ucmLink.mod.text + ") " + ucmLink.parent);
				
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
					if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_STREET))
					{
						street = ucmLink.parent.text;
						Collection streetParents = this.ucmDatabase.getParents(ucmLink.parent);
						for(Iterator it3 = streetParents.iterator(); it3.hasNext();)
						{
							UniCableMapLink ucmLink1 = (UniCableMapLink )it3.next();
							if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
								if(ucmLink1.parent.typ.text.equals(UniCableMapType.UCM_CITY))
									city = ucmLink1.parent.text;
						}
//						System.out.print("");
					}
			}

			for(Iterator it2 = this.ucmDatabase.getChildren(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
//				System.out.println("Child " + ucmLink.mod.text + ") " + ucmLink.child);
			}
			
			for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();)
			{
				UniCableMapParameter param = (UniCableMapParameter )it2.next();
//				System.out.println("Param " + param.realParameter + " = " + param.value);
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LABEL))
					name = param.value;
				else
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_ADDRESS))
					building = param.value;
			}

			double[] dp = UTMtoGeo(ucmObject.x0, ucmObject.y0);

			com.syrus.amficom.map.xml.SiteNode xmlSiteNode = com.syrus.amficom.map.xml.SiteNode.Factory.newInstance();

			com.syrus.amficom.general.xml.UID uid = xmlSiteNode.addNewUid();
			uid.setStringValue(String.valueOf(ucmObject.un));
			xmlSiteNode.setName(name);
			xmlSiteNode.setDescription(description);
			xmlSiteNode.setSitenodetypeuid(com.syrus.amficom.map.xml.SiteNodeTypeSort.Enum.forString(proto));
			xmlSiteNode.setX(dp[0]);
			xmlSiteNode.setY(dp[1]);
			xmlSiteNode.setCity(city);
			xmlSiteNode.setStreet(street);
			xmlSiteNode.setBuilding(building);
			
			xmlSiteNodesArray.add(xmlSiteNode);
		}
	}

	private void writeXMLLinks(Collection xmlPhysicalLinksArray, Collection xmlNodeLinksArray, Collection objects, String proto) throws SQLException {
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			UniCableMapObject ucmObject = (UniCableMapObject )it.next();

			String street = "";
			String city = "";
			String building = "";
			String name = "";
			String description = ucmObject.text;
			Collection node_links = new LinkedList();
			String start_node_id = "";
			String end_node_id = "";
			double length = 0.0D;
			String node_link_id = String.valueOf(ucmObject.un) + "nodelink";

//			System.out.println(ucmObject.text);

			for(Iterator it2 = this.ucmDatabase.getParents(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
//				System.out.println("Parent (" + ucmLink.mod.text + ") " + ucmLink.parent);

				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS))
					start_node_id = String.valueOf(ucmLink.parent.un);
				else
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS))
					end_node_id = String.valueOf(ucmLink.parent.un);
				
//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
//					if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_STREET))
//					{
//						street = ucmLink.parent.text;
//						Collection streetParents = ucmDatabase.getParents(ucmLink.parent);
//						for(Iterator it3 = streetParents.iterator(); it3.hasNext();)
//						{
//							UniCableMapLink ucmLink1 = (UniCableMapLink )it3.next();
//							if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
//								if(ucmLink1.parent.typ.text.equals(UniCableMapType.UCM_CITY))
//									city = ucmLink1.parent.text;
//						}
////						System.out.print("");
//					}
			}

			for(Iterator it2 = this.ucmDatabase.getChildren(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
//				System.out.println("Child " + ucmLink.mod.text + ") " + ucmLink.child);
			}
			
			for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();)
			{
				UniCableMapParameter param = (UniCableMapParameter )it2.next();
//				System.out.println("Param " + param.realParameter + " = " + param.value);
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_HIDDEN))
					name = param.value;
				else
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
					String dval = param.value.replace(',', '.');
					length = Double.parseDouble(dval);
				}
			}

			node_links.add(node_link_id);

			com.syrus.amficom.map.xml.PhysicalLink xmlPhysicalLink = com.syrus.amficom.map.xml.PhysicalLink.Factory.newInstance();

			com.syrus.amficom.general.xml.UID uid = xmlPhysicalLink.addNewUid();
			uid.setStringValue(String.valueOf(ucmObject.un));
			xmlPhysicalLink.setName(name);
			xmlPhysicalLink.setDescription(description);
			xmlPhysicalLink.setPhysicallinktypeuid(com.syrus.amficom.map.xml.PhysicalLinkTypeSort.Enum.forString(proto));

			uid = xmlPhysicalLink.addNewStartnodeuid();
			uid.setStringValue(start_node_id);

			uid = xmlPhysicalLink.addNewEndnodeuid();
			uid.setStringValue(end_node_id);

			xmlPhysicalLink.setCity(city);
			xmlPhysicalLink.setStreet(street);
			xmlPhysicalLink.setBuilding(building);

			if(name == null || name.length() == 0 || start_node_id == null || start_node_id.length() == 0 || end_node_id == null || end_node_id.length() == 0) {
				System.out.println("null at " + xmlPhysicalLink);
				name = String.valueOf(ucmObject.un);
				xmlPhysicalLink.setName(name);
			}
			
			if(description == null) {
				description = String.valueOf(ucmObject.un);
				xmlPhysicalLink.setDescription(description);
			}
			xmlPhysicalLinksArray.add(xmlPhysicalLink);

			com.syrus.amficom.map.xml.NodeLink xmlNodeLink = com.syrus.amficom.map.xml.NodeLink.Factory.newInstance();

			uid = xmlNodeLink.addNewUid();
			uid.setStringValue(node_link_id);

			xmlNodeLink.setLength(length);

			uid = xmlNodeLink.addNewPhysicallinkuid();
			uid.setStringValue(String.valueOf(ucmObject.un));

			uid = xmlNodeLink.addNewStartnodeuid();
			uid.setStringValue(start_node_id);

			uid = xmlNodeLink.addNewEndnodeuid();
			uid.setStringValue(end_node_id);

			xmlNodeLinksArray.add(xmlNodeLink);
		}
	}

	private void writeXMLCollectors(Collection xmlCollectorsArray, Collection xmlPhysicalLinksArray, Collection xmlNodeLinksArray, Collection objects, String linkProto) throws SQLException {
		for(Iterator it = objects.iterator(); it.hasNext();)
		{
			UniCableMapObject ucmObject = (UniCableMapObject )it.next();

			String name = "";
			String description = ucmObject.text;
			Collection physical_links = new LinkedList();
			Collection physical_links_to_save = new LinkedList();

//			System.out.println(ucmObject.text);

			for(Iterator it2 = this.ucmDatabase.getParents(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
//				System.out.println("Parent (" + ucmLink.mod.text + ") " + ucmLink.parent);

//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS))
//					start_node_id = String.valueOf(ucmLink.parent.un);
//				else
//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS))
//					end_node_id = String.valueOf(ucmLink.parent.un);
			}

			for(Iterator it2 = this.ucmDatabase.getChildren(ucmObject).iterator(); it2.hasNext();)
			{
				UniCableMapLink ucmLink = (UniCableMapLink )it2.next();
//				System.out.println("Child " + ucmLink.mod.text + ") " + ucmLink.child);

				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
					if(ucmLink.child.typ.text.equals(UniCableMapType.UCM_COLLECTOR_FRAGMENT)) {
						physical_links.add(String.valueOf(ucmLink.child.un));
						physical_links_to_save.clear();
						physical_links_to_save.add(ucmLink.child);
						writeXMLLinks(xmlPhysicalLinksArray, xmlNodeLinksArray, physical_links_to_save, linkProto);
					}
			}
			
			for(Iterator it2 = ucmObject.buf.params.iterator(); it2.hasNext();)
			{
				UniCableMapParameter param = (UniCableMapParameter )it2.next();
//				System.out.println("Param " + param.realParameter + " = " + param.value);
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_NAME_MANDATORY))
					name = param.value;
//				else
//				if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
//					String dval = param.value.replace(',', '.');
//					length = Double.parseDouble(dval);
//				}
			}

			com.syrus.amficom.map.xml.Collector xmlCollector = com.syrus.amficom.map.xml.Collector.Factory.newInstance();

			com.syrus.amficom.general.xml.UID uid = xmlCollector.addNewUid();
			uid.setStringValue(String.valueOf(ucmObject.un));
			xmlCollector.setName(name);
			xmlCollector.setDescription(description);

			com.syrus.amficom.map.xml.PhysicalLinkUIds xmlPhysicalLinkUIds = xmlCollector.addNewPhysicallinkuids();

			for (Iterator it2 = physical_links.iterator(); it2.hasNext();) {
				String link_id = (String) it2.next();
				com.syrus.amficom.general.xml.UID xmlPhysicalLinkUId = xmlPhysicalLinkUIds.addNewPhysicallinkuid();
				xmlPhysicalLinkUId.setStringValue(link_id);
			}
			
			xmlCollectorsArray.add(xmlCollector);
		}
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
			else
			if(ext.equals(".esf")) {
				saveESF(this.fileName);
			}
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

		com.syrus.amficom.map.xml.MapsDocument doc = 
			com.syrus.amficom.map.xml.MapsDocument.Factory.newInstance(xmlOptions);

		com.syrus.amficom.map.xml.Maps xmlMaps = doc.addNewMaps();

		com.syrus.amficom.map.xml.Map xmlMap = xmlMaps.addNewMap();

		com.syrus.amficom.general.xml.UID uid = xmlMap.addNewUid();
		uid.setStringValue("1");
		xmlMap.setName("UCM");
		xmlMap.setDescription("");

		com.syrus.amficom.map.xml.TopologicalNodes xmlTopologicalNodes = xmlMap.addNewTopologicalnodes();
		com.syrus.amficom.map.xml.SiteNodes xmlSiteNodes = xmlMap.addNewSitenodes();
		com.syrus.amficom.map.xml.PhysicalLinks xmlPhysicalLinks = xmlMap.addNewPhysicallinks();
		com.syrus.amficom.map.xml.NodeLinks xmlNodeLinks = xmlMap.addNewNodelinks();
		com.syrus.amficom.map.xml.Collectors xmlCollectors = xmlMap.addNewCollectors();

		Collection xmlSiteNodesArray = new LinkedList();
		Collection xmlPhysicalLinksArray = new LinkedList();
		Collection xmlNodeLinksArray = new LinkedList();
		Collection xmlCollectorsArray = new LinkedList();

		Collection wells = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL));
		writeXMLSites(xmlSiteNodesArray, wells, "well");

		Collection piquets = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_PIQUET));
		writeXMLSites(xmlSiteNodesArray, piquets, "piquet");

		Collection cableinlets = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
		writeXMLSites(xmlSiteNodesArray, cableinlets, "cableinlet");

		Collection tunnels = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
		writeXMLLinks(xmlPhysicalLinksArray, xmlNodeLinksArray, tunnels, "tunnel");

		Collection collectors = this.ucmDatabase.getObjects(
			this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR));
		writeXMLCollectors(xmlCollectorsArray, xmlPhysicalLinksArray, xmlNodeLinksArray, collectors, "collector");

		xmlSiteNodes.setSitenodeArray(
				(com.syrus.amficom.map.xml.SiteNode[] )
				xmlSiteNodesArray.toArray(
						new com.syrus.amficom.map.xml.SiteNode[xmlSiteNodesArray.size()]));
		xmlPhysicalLinks.setPhysicallinkArray(
				(com.syrus.amficom.map.xml.PhysicalLink[] )
				xmlPhysicalLinksArray.toArray(
						new com.syrus.amficom.map.xml.PhysicalLink[xmlPhysicalLinksArray.size()]));
		xmlNodeLinks.setNodelinkArray(
				(com.syrus.amficom.map.xml.NodeLink[] )
				xmlNodeLinksArray.toArray(
						new com.syrus.amficom.map.xml.NodeLink[xmlNodeLinksArray.size()]));
		xmlCollectors.setCollectorArray(
				(com.syrus.amficom.map.xml.Collector[] )
				xmlCollectorsArray.toArray(
						new com.syrus.amficom.map.xml.Collector[xmlCollectorsArray.size()]));

		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			File f = new File(fileName);

			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("\nXML Instance Document saved at : "
					+ f.getPath());
		}
	}

	private boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions()
				.setErrorListener(validationMessages));

		if(!isXmlValid) {
			System.out.println("Invalid XML: ");
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				System.out.println("");
				System.out.println(error.getMessage());
				System.out.println(error.getObjectLocation());
				System.out.println("Column " + error.getColumn());
				System.out.println("Line " + error.getLine());
				System.out.println("Offset " + error.getOffset());
				System.out.println("Object at cursor " + error.getCursorLocation().getObject());
				System.out.println("Source name " + error.getSourceName());
			}
		}
		return isXmlValid;
	}

	private void saveESF(String fileName) throws SQLException {
			super.open(fileName);
	
			super.startObject(MAP_TYPE);
				super.put("description", "");
				super.put("name", "UCM");
				super.put("id", "1");
			super.endObject();
	
			Collection wells = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL));
			writeESFSites(wells, "well", "images/well.gif");

			Collection piquets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_PIQUET));
			writeESFSites(piquets, "piquet", "images/piquet.gif");
	
			Collection cableinlets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
			writeESFSites(cableinlets, "cableinlet", "images/cableinlet.gif");
	
			Collection tunnels = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
			writeESFLinks(tunnels, "tunnel");
	
			Collection collectors = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR));
			writeESFCollectors(collectors, "collector");
	
			super.close();

	}
}
