/**
 * $Id: LogicalNetLayerTestCase.java,v 1.3 2005/10/25 08:02:45 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNodeType;

public class LogicalNetLayerTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		METS.clearMap();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		METS.clearMap();
		Command com = METS.aContext.getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
		com.execute();
	}

	public void testGetMapElementAtPoint() throws Exception {
		AbstractNode startnode = null;
		AbstractNode endnode = null;
		PhysicalLink link = null;
		NodeLink nodeLink = null;

		SiteNodeType type = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);
		Point location = new Point(20, 20);

		CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(type, location);
		command.setLogicalNetLayer(METS.logicalNetLayer);
		command.execute();
		startnode = command.getSite();

		Point location2 = new Point(120, 120);
		CreateSiteCommandAtomic command2 = new CreateSiteCommandAtomic(type, location2);
		command2.setLogicalNetLayer(METS.logicalNetLayer);
		command2.execute();
		endnode = command2.getSite();

		CreatePhysicalLinkCommandAtomic command3 = new CreatePhysicalLinkCommandAtomic(startnode, endnode);
		command3.setLogicalNetLayer(METS.logicalNetLayer);
		command3.execute();
		link = command3.getLink();

		CreateNodeLinkCommandAtomic command4 = new CreateNodeLinkCommandAtomic(link, startnode, endnode);
		command4.setLogicalNetLayer(METS.logicalNetLayer);
		command4.execute();
		nodeLink = command4.getNodeLink();

		Command com = METS.aContext.getApplicationModel().getCommand(MapApplicationModel.MODE_LINK);
		com.execute();

		Rectangle2D.Double visibleBounds = METS.netMapViewer.getVisibleBounds();

		assertSame(METS.logicalNetLayer.getVisibleMapElementAtPoint(new Point(20, 20), visibleBounds), startnode);
		assertSame(METS.logicalNetLayer.getVisibleMapElementAtPoint(new Point(121, 122), visibleBounds), endnode);
		assertSame(METS.logicalNetLayer.getVisibleMapElementAtPoint(new Point(50, 50), visibleBounds), link);

		Command com2 = METS.aContext.getApplicationModel().getCommand(MapApplicationModel.MODE_NODE_LINK);
		com2.execute();
		
		assertSame(METS.logicalNetLayer.getVisibleMapElementAtPoint(new Point(50, 50), visibleBounds), nodeLink);
	}

	public void testGetSelectedElements() throws Exception {
	}

}
