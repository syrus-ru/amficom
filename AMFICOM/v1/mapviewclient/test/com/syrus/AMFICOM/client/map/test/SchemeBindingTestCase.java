/**
 * $Id: SchemeBindingTestCase.java,v 1.2 2005/07/03 13:56:51 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
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
		super.setUp();
		METS.clearMap();
		METS.clearSchemeBinding();

		SiteNodeType buildingType = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		SiteNodeType wellType = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_WELL);

		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(buildingType, this.building1location);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		this.building1 = site1Command.getSite();

		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(wellType, this.well1location);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		this.well1 = site2Command.getSite();
		
		CreateSiteCommandAtomic site3Command = new CreateSiteCommandAtomic(wellType, this.well2location);
		site3Command.setLogicalNetLayer(METS.logicalNetLayer);
		site3Command.execute();
		this.well2 = site3Command.getSite();
		
		CreateSiteCommandAtomic site4Command = new CreateSiteCommandAtomic(wellType, this.well3location);
		site4Command.setLogicalNetLayer(METS.logicalNetLayer);
		site4Command.execute();
		this.well3 = site4Command.getSite();
		
		CreateSiteCommandAtomic site5Command = new CreateSiteCommandAtomic(buildingType, this.building2location);
		site5Command.setLogicalNetLayer(METS.logicalNetLayer);
		site5Command.execute();
		this.building2 = site5Command.getSite();
		
		CreatePhysicalLinkCommandAtomic link1Command = new CreatePhysicalLinkCommandAtomic(this.building1, this.well1);
		link1Command.setLogicalNetLayer(METS.logicalNetLayer);
		link1Command.execute();
		this.link1 = link1Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(this.link1, this.building1, this.well1);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		this.nodeLink1 = nodeLink1Command.getNodeLink();
		
		CreatePhysicalLinkCommandAtomic link2Command = new CreatePhysicalLinkCommandAtomic(this.well1, this.well2);
		link2Command.setLogicalNetLayer(METS.logicalNetLayer);
		link2Command.execute();
		this.link2 = link2Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(this.link2, this.well1, this.well2);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		this.nodeLink2 = nodeLink2Command.getNodeLink();
		
		CreatePhysicalLinkCommandAtomic link3Command = new CreatePhysicalLinkCommandAtomic(this.well2, this.well3);
		link3Command.setLogicalNetLayer(METS.logicalNetLayer);
		link3Command.execute();
		this.link3 = link3Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink3Command = new CreateNodeLinkCommandAtomic(this.link3, this.well2, this.well3);
		nodeLink3Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink3Command.execute();
		this.nodeLink3 = nodeLink3Command.getNodeLink();
		
		CreatePhysicalLinkCommandAtomic link4Command = new CreatePhysicalLinkCommandAtomic(this.well3, this.building2);
		link4Command.setLogicalNetLayer(METS.logicalNetLayer);
		link4Command.execute();
		this.link4 = link4Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink4Command = new CreateNodeLinkCommandAtomic(this.link4, this.well3, this.building2);
		nodeLink4Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink4Command.execute();
		this.nodeLink4 = nodeLink4Command.getNodeLink();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearSchemeBinding();
		METS.clearMap();
	}

}
