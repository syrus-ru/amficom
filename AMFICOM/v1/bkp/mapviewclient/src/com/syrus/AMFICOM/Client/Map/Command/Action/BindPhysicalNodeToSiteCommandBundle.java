/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.15 2005/02/08 15:11:08 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;

/**
 *  Команда привязывания топологического узла, принадлежащего
 *  непривязанному кабелю, к элементу узла. При этом линия, которой 
 *  принадлежит данный узел, делится на 2 части
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/02/08 15:11:08 $
 * @module mapclient_v1
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * Перетаскиваемый узел
	 */
	TopologicalNode node;
	
	/** Узел, к которому привязывается топологический узел */
	SiteNode site;

	/** 
	 * узел, находящийся "слева" от перетаскиваемого узла на той же линии,
	 * что и перетаскиваемый узел
	 */
	AbstractNode node1 = null;

	/** 
	 * узел, находящийся "справа" от перетаскиваемого узла на той же линии,
	 * что и перетаскиваемый узел
	 */
	AbstractNode node2 = null;

	/**
	 * Вид, на котором производится операция
	 */
	MapView mapView;

	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(
			TopologicalNode node, 
			SiteNode site)
	{
		this.node = node;
		this.site = site;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();

		PhysicalLink link = this.node.getPhysicalLink();

		// находим "ливый" и "правый" узлы, одновременно обновляем
		// концевые узлы фрагментов
		for(Iterator it = this.node.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

			if(this.node1 == null)
				this.node1 = mnle.getOtherNode(this.node);
			else
				this.node2 = mnle.getOtherNode(this.node);

			if(mnle.getStartNode().equals(this.node))
				mnle.setStartNode(this.site);
			else
				mnle.setEndNode(this.site);
		}

		// топологический узел удаляется
		super.removeNode(this.node);

		// обновляются концевые узлы линии
		if(link.getStartNode().equals(this.node))
			link.setStartNode(this.site);
		else
		if(link.getEndNode().equals(this.node))
			link.setEndNode(this.site);

		// создается вторая линия
		UnboundLink newLink = super.createUnboundLink(link.getStartNode(), this.site);
		newLink.setType(link.getType());

		// single cpath, as long as link is UnboundLink
		CablePath cpath = (CablePath)(this.mapView.getCablePaths(link).get(0));
		
		// новая линия добавляется в кабельный путь
		cpath.addLink(newLink, CableController.generateCCI(newLink));
		newLink.setCablePath(cpath);

		// переносим фрагменты в новую линию пока не наткнемся на
		// созданный узел
		super.moveNodeLinks(
				link,
				newLink,
				false,
				this.site,
				null);
		link.setStartNode(this.site);
		cpath.sortLinks();

		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}

