/**
 * $Id: InsertSiteCommandBundle.java,v 1.8 2005/01/31 12:19:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * вставить сетевой узел вместо топологического узла
 * 
 * @version $Revision: 1.8 $, $Date: 2005/01/31 12:19:18 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class InsertSiteCommandBundle extends MapActionCommandBundle
{
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
	
	public InsertSiteCommandBundle(
			TopologicalNode node,
			SiteNodeType proto)
	{
		this.proto = proto;
		this.node = node;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;
		
		map = logicalNetLayer.getMapView().getMap();
		
		link = node.getPhysicalLink();
		
		// создать новый узел
		site = super.createSite(
				node.getLocation(),
				proto);
		site.setName(node.getName());
		
		SiteNodeController snc = (SiteNodeController)getLogicalNetLayer().getMapViewController().getController(site);
		
		snc.updateScaleCoefficient(site);

		// обновить концевые узлы фрагментов
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

			MapElementState nls = mnle.getState();

			if(mnle.getStartNode().equals(node))
				mnle.setStartNode(site);
			else
				mnle.setEndNode(site);

			registerStateChange(mnle, nls, mnle.getState());
		}

		super.removeNode(node);

		MapElementState pls = link.getState();

		// обновить концевые узлы линии
		if(link.getStartNode().equals(node))
			link.setStartNode(site);
		else
		if(link.getEndNode().equals(node))
			link.setEndNode(site);

		// если топологический узел был активен, то разделить линию
		// на две части
		if(node.isActive())
		{
			if(link instanceof UnboundLink)
				newLink = super.createUnboundLink(link.getStartNode(), site);
			else
				newLink = super.createPhysicalLink(link.getStartNode(), site);
			newLink.setType(link.getType());
			Collector collector = map.getCollector(link);
			if(collector != null)
				collector.addPhysicalLink(newLink);

			super.moveNodeLinks(link, newLink, true, site, null);
			link.setStartNode(site);
/*
			// получить все фрагменты первой филической линии
			java.util.List nodelinks = link.getNodeLinks();

			// определить начальный узел и начальный фрагмент физической линии
			MapNodeLinkElement startNodeLink = null;
			MapNodeElement startNode = link.getStartNode();
			for(Iterator it = nodelinks.iterator(); it.hasNext();)
			{
				startNodeLink = (MapNodeLinkElement )it.next();
				if(startNodeLink.getStartNode() == link.getStartNode()
					|| startNodeLink.getEndNode() == link.getStartNode())
				{
					break;
				}
			}
		
			// неявный цикл по фракментам линии - перекидывать фрагменты в новую 
			// физическую линию. движемся по фрагментам от первого пока не наткнемся
			// на фрагмент, соседний с удаленным
			for(;;)
			{
				// перекинуть фрагмент в новую линию
				link.removeNodeLink(startNodeLink);
				link1.addNodeLink(startNodeLink);
				
				MapElementState nls = startNodeLink.getState();
				
				startNodeLink.setPhysicalLinkId(link1.getId());

				registerStateChange(startNodeLink, nls, startNodeLink.getState());
				
				// перейти к следующему узлу
				startNode = startNodeLink.getOtherNode(startNode);

				// если наткнулись на разрыв линии связи, то обновить
				// концевые узлы и закончить
				if(startNode == site)
				{
					link1.setEndNode(site);
					link.setStartNode(site);
					break;
				}
				
				// перейти к следующему фрагменту
				for(Iterator it = startNode.getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
					if(startNodeLink != mnle)
					{
						startNodeLink = mnle;
						break;
					}
				}
			}//for(;;)
*/
			MapView mapView = logicalNetLayer.getMapView();
	
			// проверить все кабельные пути, прохидящие по линии,
			// и добавить новую линию
			for(Iterator it = logicalNetLayer.getMapViewController().getCablePaths(link).iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();

				CableController cableController = (CableController )
					getLogicalNetLayer().getMapViewController().getController(cablePath);
				cablePath.addLink(newLink, cableController.generateCCI(newLink));
				if(newLink instanceof UnboundLink)
					((UnboundLink)newLink).setCablePath(cablePath);
				else
					newLink.getBinding().add(cablePath);
			}
		}

		super.registerStateChange(link, pls, link.getState());
	
		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					site, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);

	}
}
