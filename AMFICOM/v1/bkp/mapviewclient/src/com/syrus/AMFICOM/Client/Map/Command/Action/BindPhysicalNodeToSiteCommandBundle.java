/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.12 2005/01/31 12:19:18 krupenn Exp $
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
 *  непривязанному кабелю, к элементу узла. при этом линия, которой 
 *  принадлежит данный узел, делится на 2 части
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
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

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();

		PhysicalLink link = node.getPhysicalLink();

		// находим "ливый" и "правый" узлы, одновременно обновляем
		// концевые узлы фрагментов
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

			if(node1 == null)
				node1 = mnle.getOtherNode(node);
			else
				node2 = mnle.getOtherNode(node);

			if(mnle.getStartNode().equals(node))
				mnle.setStartNode(site);
			else
				mnle.setEndNode(site);
		}

		// топологический узел удаляется
		super.removeNode(node);

		// обновляются концевые узлы линии
		if(link.getStartNode().equals(node))
			link.setStartNode(site);
		else
		if(link.getEndNode().equals(node))
			link.setEndNode(site);

		// создается вторая линия
		UnboundLink newLink = super.createUnboundLink(link.getStartNode(), site);
		newLink.setType(link.getType());

		// single cpath, as long as link is UnboundLink
		CablePath cpath = (CablePath)(getLogicalNetLayer().getMapViewController().getCablePaths(link).get(0));
		CableController cableController = (CableController )
			getLogicalNetLayer().getMapViewController().getController(cpath);
		
		// новая линия добавляется в кабельный путь
		cpath.addLink(newLink, cableController.generateCCI(newLink));
		newLink.setCablePath(cpath);

		// переносим фрагменты в новую линию пока не наткнемся на
		// созданный узел
		super.moveNodeLinks(
				link,
				newLink,
				false,
				site,
				null);
		link.setStartNode(site);
		cpath.sortLinks();

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}

