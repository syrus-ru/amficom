/**
 * $Id: MapSaveTest.java,v 1.2 2005/08/10 09:25:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.editor.MapEditor;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.OpenSessionCommand;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

public class MapSaveTest extends TestCase {

	public void test1() throws ApplicationException {
		new Test1MapEditor();
		Dispatcher dispatcher = new Dispatcher();
		new OpenSessionCommand(dispatcher).execute();


		while(LoginManager.getDomainId() == null) {
			try {
				Thread.sleep(100);
			} catch(InterruptedException e) {
				//nothing
			}
		}
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		System.out.println(userId);
		System.out.println(domainId);

		Map map = Map.createInstance(
				userId, 
				domainId, 
				"test save", 
				"");

		SiteNodeType nodetype = NodeTypeController.getSiteNodeType(SiteNodeType.DEFAULT_BUILDING);

		DoublePoint location1 = new DoublePoint(20, 20);
		SiteNode site1 = SiteNode.createInstance(
				userId,
				location1,
				nodetype);
		map.addNode(site1);

		DoublePoint location2 = new DoublePoint(30, 30);
		SiteNode site2 = SiteNode.createInstance(
				userId,
				location2,
				nodetype);
		map.addNode(site2);

		PhysicalLinkType linktype = LinkTypeController.getDefaultPhysicalLinkType();
		PhysicalLink link = PhysicalLink.createInstance(
				userId,
				site1.getId(), 
				site2.getId(), 
				linktype );
		map.addPhysicalLink(link);

		NodeLink nodeLink = NodeLink.createInstance(
				userId,
				link, 
				site1, 
				site2);
		map.addNodeLink(nodeLink);

		StorableObjectPool.flush(map, userId, true);
	}
}

class Test1MapEditor extends AbstractApplication {
	public Test1MapEditor() {
        super(MapEditor.APPLICATION_NAME);
    }
}
