/**
 * $Id: SchemeBindingTestCase.java,v 1.1 2005/07/01 16:07:55 krupenn Exp $
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

	protected SiteNode site1;
	protected SiteNode site2;
	protected SiteNode site3;
	protected SiteNode site4;
	protected SiteNode site5;
	protected PhysicalLink link1;
	protected NodeLink nodeLink1;
	protected PhysicalLink link2;
	protected NodeLink nodeLink2;
	protected PhysicalLink link3;
	protected NodeLink nodeLink3;
	protected PhysicalLink link4;
	protected NodeLink nodeLink4;

	/**
	 * building ----- well ----- well ----- well ----- building 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
		METS.clearSchemeBinding();

		SiteNodeType buildingType = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		SiteNodeType wellType = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_WELL);

		Point location1 = new Point(20, 20);
		CreateSiteCommandAtomic site1Command = new CreateSiteCommandAtomic(buildingType, location1);
		site1Command.setLogicalNetLayer(METS.logicalNetLayer);
		site1Command.execute();
		this.site1 = site1Command.getSite();

		Point location2 = new Point(120, 20);
		CreateSiteCommandAtomic site2Command = new CreateSiteCommandAtomic(wellType, location2);
		site2Command.setLogicalNetLayer(METS.logicalNetLayer);
		site2Command.execute();
		this.site2 = site2Command.getSite();
		
		Point location3 = new Point(220, 20);
		CreateSiteCommandAtomic site3Command = new CreateSiteCommandAtomic(wellType, location3);
		site3Command.setLogicalNetLayer(METS.logicalNetLayer);
		site3Command.execute();
		this.site3 = site3Command.getSite();
		
		Point location4 = new Point(320, 20);
		CreateSiteCommandAtomic site4Command = new CreateSiteCommandAtomic(wellType, location4);
		site4Command.setLogicalNetLayer(METS.logicalNetLayer);
		site4Command.execute();
		this.site4 = site4Command.getSite();
		
		Point location5 = new Point(420, 20);
		CreateSiteCommandAtomic site5Command = new CreateSiteCommandAtomic(buildingType, location5);
		site5Command.setLogicalNetLayer(METS.logicalNetLayer);
		site5Command.execute();
		this.site5 = site5Command.getSite();
		
		CreatePhysicalLinkCommandAtomic link1Command = new CreatePhysicalLinkCommandAtomic(this.site1, this.site2);
		link1Command.setLogicalNetLayer(METS.logicalNetLayer);
		link1Command.execute();
		this.link1 = link1Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink1Command = new CreateNodeLinkCommandAtomic(this.link1, this.site1, this.site2);
		nodeLink1Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink1Command.execute();
		this.nodeLink1 = nodeLink1Command.getNodeLink();
		
		CreatePhysicalLinkCommandAtomic link2Command = new CreatePhysicalLinkCommandAtomic(this.site2, this.site3);
		link2Command.setLogicalNetLayer(METS.logicalNetLayer);
		link2Command.execute();
		this.link2 = link2Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink2Command = new CreateNodeLinkCommandAtomic(this.link2, this.site2, this.site3);
		nodeLink2Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink2Command.execute();
		this.nodeLink2 = nodeLink2Command.getNodeLink();
		
		CreatePhysicalLinkCommandAtomic link3Command = new CreatePhysicalLinkCommandAtomic(this.site3, this.site4);
		link3Command.setLogicalNetLayer(METS.logicalNetLayer);
		link3Command.execute();
		this.link3 = link3Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink3Command = new CreateNodeLinkCommandAtomic(this.link3, this.site3, this.site4);
		nodeLink3Command.setLogicalNetLayer(METS.logicalNetLayer);
		nodeLink3Command.execute();
		this.nodeLink3 = nodeLink3Command.getNodeLink();
		
		CreatePhysicalLinkCommandAtomic link4Command = new CreatePhysicalLinkCommandAtomic(this.site4, this.site5);
		link4Command.setLogicalNetLayer(METS.logicalNetLayer);
		link4Command.execute();
		this.link4 = link4Command.getLink();
		
		CreateNodeLinkCommandAtomic nodeLink4Command = new CreateNodeLinkCommandAtomic(this.link4, this.site4, this.site5);
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
