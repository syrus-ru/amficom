/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.5 2004/10/19 10:07:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  Команда генерации тоннелей в соответствии с прохождением кабеля.
 *  из непроложенных линий генерируются тоннели и кабель привязывается к ним.
 *  Уже существующая привязка сохраняется. По непривязанным элементам 
 *  генерируются сетевые узла и схемные элементы привязываются к ним.
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/19 10:07:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class GenerateCablePathCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapCablePathElement path;
	
	/**
	 * тип узлов для генерации вместо непривязанных элементов
	 */
	MapNodeProtoElement proto;

	/**
	 * Карта, на которой производится операция
	 */
	MapView mapView;

	Map map;

	public GenerateCablePathCablingCommandBundle(
			MapCablePathElement path, 
			MapNodeProtoElement proto)
	{
		this.path = path;
		this.proto = proto;
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
		
		// для последующего цикла необходима последовательность
		// узлов от начального к конечному
		MapSiteNodeElement site = (MapSiteNodeElement )path.getStartNode();
		
		// проверить, что узел является сетевым узлом (если это непривязанный
		// элемент, сгенерировать на его месте сетевой узел)
		site = this.checkSite(site);

		// отдельный список, поскольку используется удаление
		List list  = new LinkedList();
		list.addAll(path.getLinks());

		// цикл по всем линиям, участвующим в кабельном пути
		// по непривязанным линиям генерировать тоннели
		for(Iterator it = list.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

			// перейти к следующему узлу
			if(site == link.getEndNode())
				site = (MapSiteNodeElement )link.getStartNode();
			else
				site = (MapSiteNodeElement )link.getEndNode();

			// проверить, что узел является сетевым узлом (если это непривязанный
			// элемент, сгенерировать на его месте сетевой узел)
			site = this.checkSite(site);

			// если непривязанная линия, генерировать тоннель
			if(link instanceof MapUnboundLinkElement)
			{
				path.removeLink(link);
				MapUnboundLinkElement un = (MapUnboundLinkElement )link;
				super.removePhysicalLink(un);

				link = super.createPhysicalLink(un.getStartNode(), un.getEndNode());
				// фрагменты переносятся в новый сгенерированный тоннель
				for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it2.next();
					mnle.setPhysicalLinkId(link.getId());
					link.addNodeLink(mnle);
				}
				path.addLink(link);
				link.getBinding().add(path);
			}
		}
	}

	/**
	 * проверить, что узел явзяется сетевым узлом.
	 * если он является непривязанным элементом, сгенерировать на его месте
	 * сетевой узел
	 */
	private MapSiteNodeElement checkSite(MapSiteNodeElement site)
	{
		MapSiteNodeElement site2 = site;
		if(site instanceof MapUnboundNodeElement)
		{
			site2 = super.createSite(site.getAnchor(), proto);
			super.removeNode(site);

			// привязать элементо к вновь созданному узлу
			((MapUnboundNodeElement )site).getSchemeElement().siteId = site2.getId();

			// проверить все линии, заканчивающиеся на
			// непривязанном элементе, и обновить концевые узлы
			for(Iterator it = path.getLinks().iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

				// обновить концевые узлы линии
				if(link.getStartNode().equals(site))
					link.setStartNode(site2);
				if(link.getEndNode().equals(site))
					link.setEndNode(site2);

				// обновить концевые узлы фрагментов линий
				for(Iterator it2 = link.getNodeLinksAt(site).iterator(); it2.hasNext();)
				{
					MapNodeLinkElement nl = (MapNodeLinkElement )it2.next();
					if(nl.getStartNode().equals(site))
						nl.setStartNode(site2);
					if(nl.getEndNode().equals(site))
						nl.setEndNode(site2);
				}
			}

			// обновить концевые узлы кабельных путей
			for(Iterator it = mapView.getCablePaths(site).iterator(); it.hasNext();)
			{
				MapCablePathElement cpath = (MapCablePathElement )it.next();
				if(cpath.getStartNode().equals(site))
					cpath.setStartNode(site2);
				else
				if(cpath.getEndNode().equals(site))
					cpath.setEndNode(site2);
			}
		}
		return site2;
	}

}

