/**
 * $Id: InsertSiteCommandBundle.java,v 1.13 2005/04/13 11:10:49 krupenn Exp $
 * Syrus Systems ������-����������� ����� ������: ������� ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
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

/**
 * �������� ������� ���� ������ ��������������� ����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/04/13 11:10:49 $
 * @module mapviewclient_v1
 */
public class InsertSiteCommandBundle extends MapActionCommandBundle {
	/**
	 * ��������������� ����
	 */
	TopologicalNode node;

	/**
	 * ����� ������� ����
	 */
	SiteNode site;

	/**
	 * ��� ������������ �������� ����
	 */
	SiteNodeType proto;

	/**
	 * �����, �� ������� ��������� �������������� ����
	 */
	PhysicalLink link;

	/**
	 * ����� ����� (���� ������ ����������� �� 2 �����)
	 */
	PhysicalLink newLink = null;

	Map map;

	public InsertSiteCommandBundle(TopologicalNode node, SiteNodeType proto) {
		this.proto = proto;
		this.node = node;
	}

	public void execute() {
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "execute()");

		if(!getLogicalNetLayer().getContext().getApplicationModel().isEnabled(
				MapApplicationModel.ACTION_EDIT_MAP))
			return;

		this.map = this.logicalNetLayer.getMapView().getMap();

		try {
			this.link = this.node.getPhysicalLink();
			// ������� ����� ����
			this.site = super.createSite(this.node.getLocation(), this.proto);
			this.site.setName(this.node.getName());
			SiteNodeController snc = (SiteNodeController )getLogicalNetLayer()
					.getMapViewController().getController(this.site);
			snc.updateScaleCoefficient(this.site);
			// �������� �������� ���� ����������
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
			// �������� �������� ���� �����
			if(this.link.getStartNode().equals(this.node))
				this.link.setStartNode(this.site);
			else
				if(this.link.getEndNode().equals(this.node))
					this.link.setEndNode(this.site);
			// ���� �������������� ���� ��� �������, �� ��������� �����
			// �� ��� �����
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

				// ��������� ��� ��������� ����, ���������� �� �����,
				// � �������� ����� �����
				for(
					Iterator it = mapView.getCablePaths(this.link).iterator(); 
					it.hasNext();
					) {
					CablePath cablePath = (CablePath )it.next();

					cablePath.addLink(
							this.newLink, 
							CableController.generateCCI(this.newLink));
					if(this.newLink instanceof UnboundLink)
						((UnboundLink )this.newLink).setCablePath(cablePath);
					else
						this.newLink.getBinding().add(cablePath);
				}
			}
			super.registerStateChange(this.link, pls, this.link.getState());
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(new MapEvent(
					this,
					MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					this.site,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			this.logicalNetLayer.setCurrentMapElement(this.site);
			this.logicalNetLayer.notifySchemeEvent(this.site);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}

	}
}
