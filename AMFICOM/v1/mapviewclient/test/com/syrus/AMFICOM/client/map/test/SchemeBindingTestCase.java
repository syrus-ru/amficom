/**
 * $Id: SchemeBindingTestCase.java,v 1.3 2005/08/10 09:26:23 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

public abstract class SchemeBindingTestCase extends TestCase {

	protected SiteNode building1;
	protected Point building1location = new Point(20, 20);
	protected SiteNode well1;
	protected Point well1location = new Point(120, 20);
	protected SiteNode well2;
	protected Point well2location = new Point(220, 20);
	protected SiteNode well3;
	protected Point well3location = new Point(220, 120);
	protected SiteNode building2;
	protected Point building2location = new Point(220, 220);

	protected PhysicalLink link1;
	protected NodeLink nodeLink1;
	protected PhysicalLink link2;
	protected NodeLink nodeLink2;
	protected PhysicalLink link3;
	protected NodeLink nodeLink3;
	protected PhysicalLink link4;
	protected NodeLink nodeLink4;

	/**
	 * building1 ----- well1 ----- well2 
	 *                               |
	 *                               | 
	 *                             well3
	 *                               |
	 *                               |
	 *                           building2 
	 */
	protected void setUp() throws Exception {
		long t1 = System.currentTimeMillis();
		super.setUp();
		long t2 = System.currentTimeMillis();
		METS.clearMapView();
		long t3 = System.currentTimeMillis();
		METS.clearSchemeBinding();
		long t4 = System.currentTimeMillis();
		METS.clearMap();
		long t5 = System.currentTimeMillis();

		SiteNodeType buildingType = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		SiteNodeType wellType = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_WELL);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(buildingType, this.building1location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		this.building1 = site1Command.getSite();
		this.building1.setName("building1");

		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(wellType, this.well1location);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		this.well1 = site2Command.getSite();
		this.well1.setName("well1");
		
		CreateSiteCommandAtomic site3Command = new CreateSiteCommandAtomic(wellType, this.well2location);
		site3Command.setLogicalNetLayer(METS.logicalNetLayer);
		site3Command.execute();
		this.well2 = site3Command.getSite();
		this.well2.setName("well2");
		
		CreateSiteCommandAtomic site4Command = new CreateSiteCommandAtomic(wellType, this.well3location);
		site4Command.setLogicalNetLayer(METS.logicalNetLayer);
		site4Command.execute();
		this.well3 = site4Command.getSite();
		this.well3.setName("well3");
		
		CreateSiteCommandAtomic site5Command = new CreateSiteCommandAtomic(buildingType, this.building2location);
		site5Command.setLogicalNetLayer(METS.logicalNetLayer);
		site5Command.execute();
		this.building2 = site5Command.getSite();
		this.building2.setName("building2");
		
		CreatePhysicalLinkCommandAtomic link1Command = new CreatePhysicalLinkCommandAtomic(this.building1, this.well1);
		link1Command.setLogicalNetLayer(METS.logicalNetLayer);
		link1Command.execute();
		this.link1 = link1Command.getLink();
		this.link1.setName("link1");
		
		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(this.link1, this.building1, this.well1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		this.nodeLink1 = nodeLink1Command.getNodeLink();
		this.nodeLink1.setName("nodelink1");
		
		CreatePhysicalLinkCommandAtomic link2Command = new CreatePhysicalLinkCommandAtomic(this.well1, this.well2);
		link2Command.setLogicalNetLayer(METS.logicalNetLayer);
		link2Command.execute();
		this.link2 = link2Command.getLink();
		this.link2.setName("link2");
		
		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(this.link2, this.well1, this.well2);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		this.nodeLink2 = nodeLink2Command.getNodeLink();
		this.nodeLink2.setName("nodelink2");
		
		CreatePhysicalLinkCommandAtomic link3Command = new CreatePhysicalLinkCommandAtomic(this.well2, this.well3);
		link3Command.setLogicalNetLayer(METS.logicalNetLayer);
		link3Command.execute();
		this.link3 = link3Command.getLink();
		this.link3.setName("link3");
		
		CreateNodeLinkCommandAtomic nodeLink3Command = new CreateNodeLinkCommandAtomic(this.link3, this.well2, this.well3);
		nodeLink3Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink3Command.execute();
		this.nodeLink3 = nodeLink3Command.getNodeLink();
		this.nodeLink3.setName("nodelink3");
		
		CreatePhysicalLinkCommandAtomic link4Command = new CreatePhysicalLinkCommandAtomic(this.well3, this.building2);
		link4Command.setLogicalNetLayer(METS.logicalNetLayer);
		link4Command.execute();
		this.link4 = link4Command.getLink();
		this.link4.setName("link4");
		
		CreateNodeLinkCommandAtomic nodeLink4Command = new CreateNodeLinkCommandAtomic(this.link4, this.well3, this.building2);
		nodeLink4Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink4Command.execute();
		this.nodeLink4 = nodeLink4Command.getNodeLink();
		this.nodeLink4.setName("nodelink4");

		long t6 = System.currentTimeMillis();
		System.out.println(this.getName() + "::setUp()\n"
				+ "			" + (t2 - t1) + " ms - super.setUp()\n"
				+ "			" + (t3 - t2) + " ms - METS.clearMapView()\n"
				+ "			" + (t4 - t3) + " ms - METS.clearSchemeBinding()\n"
				+ "			" + (t5 - t4) + " ms - METS.clearMap()\n"
				+ "			" + (t6 - t5) + " ms - create commands"
				);
	}

	protected void tearDown() throws Exception {
		long t1 = System.currentTimeMillis();
		super.tearDown();
		long t2 = System.currentTimeMillis();
		METS.clearMapView();
		long t3 = System.currentTimeMillis();
		METS.clearSchemeBinding();
		long t4 = System.currentTimeMillis();
		METS.clearMap();
		long t5 = System.currentTimeMillis();
		System.out.println(this.getName() + "::tearDown()\n"
				+ "			" + (t2 - t1) + " ms - super.tearDown()\n"
				+ "			" + (t3 - t2) + " ms - METS.clearMapView()\n"
				+ "			" + (t4 - t3) + " ms - METS.clearSchemeBinding()\n"
				+ "			" + (t5 - t4) + " ms - METS.clearMap()\n"
				);
	}

}
