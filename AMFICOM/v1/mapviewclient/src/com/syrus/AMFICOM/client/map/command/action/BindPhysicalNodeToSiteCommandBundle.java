/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.6 2004/10/19 14:10:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;

/**
 *  Команда привязывания топологического узла, принадлежащего
 *  непривязанному кабелю, к элементу узла. при этом линия, которой 
 *  принадлежит данный узел, делится на 2 части
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/19 14:10:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * Перетаскиваемый узел
	 */
	MapPhysicalNodeElement node;
	
	/** Узел, к которому привязывается топологический узел */
	MapSiteNodeElement site;

	/** 
	 * узел, находящийся "слева" от перетаскиваемого узла на той же линии,
	 * что и перетаскиваемый узел
	 */
	MapNodeElement node1 = null;

	/** 
	 * узел, находящийся "справа" от перетаскиваемого узла на той же линии,
	 * что и перетаскиваемый узел
	 */
	MapNodeElement node2 = null;

	/**
	 * Вид, на котором производится операция
	 */
	MapView mapView;

	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(
			MapPhysicalNodeElement node, 
			MapSiteNodeElement site)
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

		MapPhysicalLinkElement link = map.getPhysicalLink(node.getPhysicalLinkId());

		// находим "ливый" и "правый" узлы, одновременно обновляем
		// концевые узлы фрагментов
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();

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
		MapUnboundLinkElement newLink = super.createUnboundLink(link.getStartNode(), site);
		newLink.setProto(link.getProto());

		// single cpath, as long as link is UnboundLink
		MapCablePathElement cpath = (MapCablePathElement )(mapView.getCablePaths(link).get(0));
		
		// новая линия добавляется в кабельный путь
		cpath.addLink(newLink);
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
/* MapActionBundleCommand

		// определить начальный узел и начальный фрагмент физической линии
		MapNodeLinkElement startNodeLink = link.getStartNodeLink();
		MapNodeElement startNode = link.getStartNode();
	
		// неявный цикл по фракментам линии - перекидывать фрагменты в новую 
		// физическую линию. движемся по фрагментам от первого пока не наткнемся
		// на фрагмент, соседний с удаленным
		for(;;)
		{
			// перекинуть фрагмент в новую линию
			link.removeNodeLink(startNodeLink);
			newLink.addNodeLink(startNodeLink);
			startNodeLink.setPhysicalLinkId(newLink.getId());

			// перейти к следующему узлу
			startNode = startNodeLink.getOtherNode(startNode);

			// если наткнулись на разрыв линии связи, то обновить
			// концевые узлы и закончить
			if(startNode == site)
			{
				newLink.setEndNode(site);
				link.setStartNode(site);
				cpath.sortLinks();
				break;
			}
			
			// перейти к следующему фрагменту
			startNodeLink = startNode.getOtherNodeLink(startNodeLink);
		}//for(;;)
*/
		logicalNetLayer.repaint();
	}
}

