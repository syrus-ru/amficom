/**
 * $Id: InsertSiteCommandBundle.java,v 1.20 2005/06/22 08:43:47 krupenn Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * вставить сетевой узел вместо топологического узла
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.20 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
 */
public class InsertSiteCommandBundle extends MapActionCommandBundle {
	/**
	 * топологтический узел
	 */
	TopologicalNode node;

	/**
	 * новый сетевой узел
	 */
	SiteNode site;

	/**
	 * тип создаваемого сетевого узла
	 */
	SiteNodeType proto;

	/**
	 * линия, на которой находится топологический узел
	 */
	PhysicalLink link;

	/**
	 * новая линия (если старая разделяется на 2 части)
	 */
	PhysicalLink newLink = null;

	Map map;

	public InsertSiteCommandBundle(TopologicalNode node, SiteNodeType proto) {
		this.proto = proto;
		this.node = node;
	}

	public void execute() {
		Log.debugMessage(getClass()
				.getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		if(!getLogicalNetLayer().getContext().getApplicationModel().isEnabled(
				MapApplicationModel.ACTION_EDIT_MAP))
			return;

		this.map = this.logicalNetLayer.getMapView().getMap();

		try {
			this.link = this.node.getPhysicalLink();
			// создать новый узел
			this.site = super.createSite(this.node.getLocation(), this.proto);
			this.site.setName(this.node.getName());
			SiteNodeController snc = (SiteNodeController )getLogicalNetLayer()
					.getMapViewController().getController(this.site);
			snc.updateScaleCoefficient(this.site);
			// обновить концевые узлы фрагментов
			for(Iterator it = this.map.getNodeLinks(this.node).iterator(); it.hasNext();) {
				NodeLink mnle = (NodeLink )it.next();

				MapElementState nls = mnle.getState();

				if(mnle.getStartNode().equals(this.node))
					mnle.setStartNode(this.site);
				else
					mnle.setEndNode(this.site);

				registerStateChange(mnle, nls, mnle.getState());
			}
			super.removeNode(this.node);
			MapElementState pls = this.link.getState();
			// обновить концевые узлы линии
			if(this.link.getStartNode().equals(this.node))
				this.link.setStartNode(this.site);
			else
				if(this.link.getEndNode().equals(this.node))
					this.link.setEndNode(this.site);
			// если топологический узел был активен, то разделить линию
			// на две части
			if(this.node.isActive()) {
				if(this.link instanceof UnboundLink)
					this.newLink = super.createUnboundLink(
							this.link.getStartNode(), 
							this.site);
				else
					this.newLink = super.createPhysicalLink(
							this.link.getStartNode(), 
							this.site);
				this.newLink.setType(this.link.getType());
				Collector collector = this.map.getCollector(this.link);
				if(collector != null)
					collector.addPhysicalLink(this.newLink);

				super.moveNodeLinks(
						this.map,
						this.link,
						this.newLink,
						true,
						this.site,
						null);
				this.link.setStartNode(this.site);

				MapView mapView = this.logicalNetLayer.getMapView();

				// проверить все кабельные пути, прохидящие по линии,
				// и добавить новую линию
				for(
					Iterator it = mapView.getCablePaths(this.link).iterator(); 
					it.hasNext();
					) {
					CablePath cablePath = (CablePath )it.next();

					cablePath.addLink(
							this.newLink, 
							CableController.generateCCI(cablePath, this.newLink, LoginManager.getUserId()));
					if(this.newLink instanceof UnboundLink)
						((UnboundLink )this.newLink).setCablePath(cablePath);
					else
						this.newLink.getBinding().add(cablePath);
				}
			}
			super.registerStateChange(this.link, pls, this.link.getState());
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(this.site);
			this.logicalNetLayer.notifySchemeEvent(this.site);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}

	}
}
