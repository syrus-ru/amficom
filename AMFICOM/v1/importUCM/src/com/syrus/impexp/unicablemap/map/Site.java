/**
 * $Id: Site.java,v 1.6 2005/09/29 15:29:58 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.impexp.unicablemap.map;

import java.sql.SQLException;
import java.util.Collection;

import com.mapinfo.coordsys.CoordSys;
import com.mapinfo.coordsys.Projection;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.XmlSiteNode;
import com.syrus.impexp.unicablemap.UniCableMapDatabase;
import com.syrus.impexp.unicablemap.UniCableMapLink;
import com.syrus.impexp.unicablemap.UniCableMapLinkType;
import com.syrus.impexp.unicablemap.UniCableMapObject;
import com.syrus.impexp.unicablemap.UniCableMapParameter;
import com.syrus.impexp.unicablemap.UniCableMapType;

public class Site {

	private String street = "";
	private String city = "";
	private String building = "";
	private String name;
	private String description = "";
	private String uid;
	private String proto;
	private double x;
	private double y;
	private Site buildingPlan = null;
	private String attachmentSiteNodeId = null;

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

	public void setSiteNodeTypeCodename(String proto) {
		this.proto = proto;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getSiteNodeTypeCodename() {
		return this.proto;
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

	public String getProto() {
		return this.proto;
	}

	public String getStreet() {
		return this.street;
	}

	public String getId() {
		return this.uid;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setAttachmentSiteNodeId(String attachmentSiteNodeId) {
		this.attachmentSiteNodeId = attachmentSiteNodeId;
	}

	public String getAttachmentSiteNodeId() {
		return this.attachmentSiteNodeId;
	}

	private void setBuildingPlan(Site buildingPlan) {
		this.buildingPlan = buildingPlan;
	}

	public Site getBuildingPlan() {
		return this.buildingPlan;
	}

	public XmlSiteNode getXmlSiteNode() {
		XmlSiteNode xmlSiteNode = XmlSiteNode.Factory.newInstance();

		XmlIdentifier xmlId = xmlSiteNode.addNewId();
		xmlId.setStringValue(String.valueOf(this.uid));
		xmlSiteNode.setName(this.name);
		xmlSiteNode.setDescription(this.description);
		xmlSiteNode.setSiteNodeTypeCodename(this.proto);
		xmlSiteNode.setX(this.x);
		xmlSiteNode.setY(this.y);
		xmlSiteNode.setCity(this.city);
		xmlSiteNode.setStreet(this.street);
		xmlSiteNode.setBuilding(this.building);
		
		if(this.attachmentSiteNodeId != null) {
			xmlId = xmlSiteNode.addNewAttachmentSiteNodeId();
			xmlId.setStringValue(this.attachmentSiteNodeId);
		}

		if(this.name == null) {
			xmlSiteNode.setName(this.description);
		}
		
		return xmlSiteNode;
	}

	public static Site parseSite(
			UniCableMapDatabase ucmDatabase,
			UniCableMapObject ucmObject,
			String proto) throws SQLException {
		Site site = new Site();

		site.setDescription(ucmObject.text);
		site.setBuilding("");
		for(UniCableMapLink ucmLink : ucmDatabase.getParents(ucmObject)) {

			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
				if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_STREET)) {
					site.setStreet(ucmLink.parent.text);
					Collection<UniCableMapLink> streetParents = ucmDatabase.getParents(ucmLink.parent);
					for(UniCableMapLink ucmLink1 : streetParents) {
						if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_POSESSES_BELONGS))
							if(ucmLink1.parent.typ.text.equals(UniCableMapType.UCM_CITY))
								site.setCity(ucmLink1.parent.text);
					}
				}
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE))
				if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_BUILDING_PLAN)) {
					site.setAttachmentSiteNodeId("site" + String.valueOf(ucmLink.parent.un));
//					site.setBuildingPlan(Site.parseSite(ucmDatabase, ucmLink.parent, "building"));
				}
		}

		for(UniCableMapLink ucmLink : ucmDatabase.getChildren(ucmObject)) {
			// nothing
		}

		for(UniCableMapParameter param : ucmObject.buf.params) {
			if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LABEL))
				site.setName(param.value);
			else if(param.realParameter.text.equals(UniCableMapParameter.UCM_ADDRESS))
				site.setBuilding(param.value);
		}

		double[] dp = UTMtoGeo(
				ucmObject.x0,
				ucmObject.y0);
		site.setX(dp[0]);
		site.setY(dp[1]);

		String id = String.valueOf(ucmObject.un);
		if(ucmObject.typ.text.equals(UniCableMapType.UCM_BUILDING_PLAN)) {
			id = "site" + id;
		}
		site.setId(id);
		site.setSiteNodeTypeCodename(proto);
		if(ucmObject.typ.text.equals(UniCableMapType.UCM_WELL)) {
			for(UniCableMapLink ucmLink : ucmDatabase.getParents(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND))
					if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_WELL_TYPE)) {
						site.setSiteNodeTypeCodename(String.valueOf(ucmLink.parent.un));
					}
			}
		}

		return site;
	}

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
	
	public static double[] UTMtoGeo(double x, double y) {
		xy[0] = x;
		xy[1] = y;
		
		projection.inverse(xy, 0, longlat, 0, 1);

		return longlat;
	}

}
