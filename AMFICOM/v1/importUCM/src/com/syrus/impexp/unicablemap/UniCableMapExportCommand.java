package com.syrus.impexp.unicablemap;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.mapinfo.coordsys.CoordSys;
import com.mapinfo.coordsys.Projection;
import com.syrus.AMFICOM.Client.General.Command.ExportCommand;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 09:42:50 $
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

	void writeSites(Collection objects, String proto, String image) 
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

	void writeLinks(Collection objects, String proto) 
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

	void writeCollectors(Collection objects, String linkProto)
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
						writeLinks(physical_links_to_save, linkProto);
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

	public void execute()
	{
		try {
			super.open(this.fileName);
	
			super.startObject(MAP_TYPE);
				super.put("description", "");
				super.put("name", "UCM");
				super.put("id", "1");
			super.endObject();
	
			Collection wells = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL));
			writeSites(wells, "well", "images/well.gif");

			Collection piquets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_PIQUET));
			writeSites(piquets, "piquet", "images/piquet.gif");
	
			Collection cableinlets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
			writeSites(cableinlets, "cableinlet", "images/cableinlet.gif");
	
			Collection tunnels = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
			writeLinks(tunnels, "tunnel");
	
			Collection collectors = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR));
			writeCollectors(collectors, "collector");
	
			super.close();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
/*
class DP {
	public double longitude;
	public double latitude;
	
	public DP() {
	}
}

class UTM {
	public static double center_Lon = -96D;
	public static double center_Lat = 23D;
	public static double scale_Factor = 0.99960000000000004D;
	public static double false_East = 500000.0D;
	public static double false_North = 10000000.0D;
	public static double r_major = WGS.majorAxis;
	public static double r_minor = WGS.minorAxis;
	
}


class WGS {
	public static String name = "WGS 84";
	public static double majorAxis = 6378137D;
	public static double minorAxis = 6356752.3142449996D;
	public static double flattening = 6378137D - 6356752.3142449996D / 6378137D;
	public static double eccentricity = Math.sqrt(6378137D * 6378137D - 6356752.3142449996D * 6356752.3142449996D) / 6378137D;

	static {
		majorAxis = 6378137D;
		minorAxis = 6356752.3142449996D;

		flattening = majorAxis - minorAxis / majorAxis;
		eccentricity = Math.sqrt(majorAxis * majorAxis - minorAxis * minorAxis) / majorAxis;
	}
}
*/