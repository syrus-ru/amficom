/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.9 2004/12/07 17:05:54 krupenn Exp $
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
 * @version $Revision: 1.9 $, $Date: 2004/12/07 17:05:54 $
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
		MapSiteNodeElement startsite = (MapSiteNodeElement )path.getStartNode();
		MapSiteNodeElement endsite = null;
		
		// проверить, что узел является сетевым узлом (если это непривязанный
		// элемент, сгенерировать на его месте сетевой узел)
		startsite = this.checkSite(startsite);

		// отдельный список, поскольку используется удаление
		List list  = new LinkedList();
		list.addAll(path.getLinks());

		// цикл по всем линиям, участвующим в кабельном пути
		// по непривязанным линиям генерировать тоннели
		for(Iterator it = list.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

			// перейти к следующему узлу
			if(startsite == link.getEndNode())
				endsite = (MapSiteNodeElement )link.getStartNode();
			else
				endsite = (MapSiteNodeElement )link.getEndNode();

			// проверить, что узел является сетевым узлом (если это непривязанный
			// элемент, сгенерировать на его месте сетевой узел)
			endsite = this.checkSite(endsite);

			// если непривязанная линия, генерировать тоннель
			if(link instanceof MapUnboundLinkElement)
			{
				path.removeLink(link);
				MapUnboundLinkElement un = (MapUnboundLinkElement )link;
				super.removePhysicalLink(un);

				link = super.createPhysicalLink(startsite, endsite);
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

			startsite = endsite;
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
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
			CreateSiteCommandAtomic command = 
					new CreateSiteCommandAtomic(
						proto, 
						site.getLocation());
			command.setLogicalNetLayer(logicalNetLayer);
			command.execute();
			super.add(command);
			
			site2 = command.getSite();
	
			BindUnboundNodeToSiteCommandBundle command2 = 
					new BindUnboundNodeToSiteCommandBundle(
						(MapUnboundNodeElement )site, 
						site2);
			command2.setLogicalNetLayer(logicalNetLayer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

