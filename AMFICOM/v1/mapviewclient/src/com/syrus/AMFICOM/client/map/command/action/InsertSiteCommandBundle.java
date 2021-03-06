/*-
 * $$Id: InsertSiteCommandBundle.java,v 1.44 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
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
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 * ???????? ??????? ???? ?????? ??????????????? ????
 * 
 * @version $Revision: 1.44 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class InsertSiteCommandBundle extends MapActionCommandBundle {
	/**
	 * ??????????????? ????
	 */
	TopologicalNode node;

	/**
	 * ????? ??????? ????
	 */
	SiteNode site;

	/**
	 * ??? ???????????? ???????? ????
	 */
	SiteNodeType type;

	/**
	 * ?????, ?? ??????? ????????? ?????????????? ????
	 */
	PhysicalLink link;

	/**
	 * ????? ????? (???? ?????? ??????????? ?? 2 ?????)
	 */
	PhysicalLink newLink = null;

	Map map;

	public InsertSiteCommandBundle(TopologicalNode node, SiteNodeType proto) {
		this.type = proto;
		this.node = node;
	}

	@Override
	public void execute() {
		Log.debugMessage("insert site of type " //$NON-NLS-1$
				+ this.type.getName() + " (" + this.type.getId() + ") "  //$NON-NLS-1$ //$NON-NLS-2$
				+ "instead of topological node " + this.node.getId(),  //$NON-NLS-1$
			Level.FINEST);

		if(!this.aContext.getApplicationModel().isEnabled(
				MapApplicationModel.ACTION_EDIT_MAP))
			return;

		MapView mapView = this.logicalNetLayer.getMapView();

		this.map = mapView.getMap();

		try {
			this.link = this.node.getPhysicalLink();
			// ??????? ????? ????
			this.site = super.createSite(this.node.getLocation(), this.type);
			this.site.setName(this.node.getName());
			SiteNodeController snc = (SiteNodeController )this.logicalNetLayer
					.getMapViewController().getController(this.site);
			snc.updateScaleCoefficient(this.site);
			// ???????? ???????? ???? ??????????
			for(Iterator it = mapView.getNodeLinks(this.node).iterator(); it.hasNext();) {
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
			// ???????? ???????? ???? ?????
			if(this.link.getStartNode().equals(this.node))
				this.link.setStartNode(this.site);
			else
				if(this.link.getEndNode().equals(this.node))
					this.link.setEndNode(this.site);
			// ???? ?????????????? ???? ??? ???????, ?? ????????? ?????
			// ?? ??? ?????
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

				// ????????? ??? ????????? ????, ?????????? ?? ?????,
				// ? ???????? ????? ?????
				for(Iterator it = mapView.getCablePaths(this.link).iterator(); it.hasNext();) {
					CablePath cablePath = (CablePath )it.next();
					super.setUndoable(false);

//					CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.link);
					for(CableChannelingItem cableChannelingItem : cablePath.getCachedCCIs()) {
						if(cablePath.getBinding().get(cableChannelingItem) == this.link) {
							if(this.newLink.getStartNode().equals(cableChannelingItem.getStartSiteNodeId())) {
								CableChannelingItem newCableChannelingItem = 
									CableController.generateCCI(
										cablePath, 
										this.newLink,
										this.newLink.getStartNode(),
										this.site);
								newCableChannelingItem.insertSelfBefore(cableChannelingItem);
								cableChannelingItem.setStartSiteNode(this.site);
								// ????? ????? ??????????? ? ????????? ????
								cablePath.addLink(this.newLink, newCableChannelingItem);
							}
							else {
								CableChannelingItem newCableChannelingItem = 
									CableController.generateCCI(
										cablePath, 
										this.newLink,
										this.site,
										this.newLink.getStartNode());
								newCableChannelingItem.insertSelfAfter(cableChannelingItem);
								cableChannelingItem.setEndSiteNode(this.site);
								// ????? ????? ??????????? ? ????????? ????
								cablePath.addLink(this.newLink, newCableChannelingItem);
							}
						}
					}

					if(this.newLink instanceof UnboundLink)
						((UnboundLink )this.newLink).setCablePath(cablePath);
					else
						this.newLink.getBinding().add(cablePath);
				}
			}
			super.registerStateChange(this.link, pls, this.link.getState());

			this.logicalNetLayer.setCurrentMapElement(this.site);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		}

	}
}
