/**
 * $Id: DeletePhysicalLinkCommandBundle.java,v 1.9 2005/02/01 11:34:56 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;
import java.util.List;

/**
 * В данном классе реализуется алгоритм удаления связи. В зависимости
 * от того, какие конечные точки на концах происходит операция удаления 
 * фрагментов линий, линий, узлов  (и путей). Команда
 * состоит из последовательности атомарных действий
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeletePhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый фрагмент
	 */
	PhysicalLink link;
	
	/**
	 * Карта
	 */
	Map map;

	public DeletePhysicalLinkCommandBundle(PhysicalLink link)
	{
		super();
		this.link = link;
	}


	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		// связь может быть удалена в результате атомарной команды в составе
		// другой команды удаления, в этом случае у неё будет выставлен
		// флаг isRemoved
		if(link.isRemoved())
			return;

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		List cablePathsToScan = mapView.getCablePaths(link);

		link.sortNodes();
		
		/// удаляются все топологические узлы линии
		for(Iterator it = link.getSortedNodes().iterator(); it.hasNext();)
		{
			AbstractNode ne = (AbstractNode)it.next();
			if(ne instanceof TopologicalNode)
			{
				TopologicalNode node = (TopologicalNode)ne;
				super.removeNode(node);
			}
		}
		
		// удаляются все фрагменты линии
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink)it.next();
			super.removeNodeLink(nodeLink);
		}
		
		// удаляется сама линия
		super.removePhysicalLink(link);
		
		// проверяются все кабельные пути, которые проходили по удаленной линии,
		// и прохождение по ней заменяется непривязанной связью
		for(Iterator it = cablePathsToScan.iterator(); it.hasNext();)
		{
			CablePath cablePath = (CablePath)it.next();
			
			cablePath.removeLink(link);
			UnboundLink unbound = super.createUnboundLinkWithNodeLink(
					link.getStartNode(),
					link.getEndNode());
			unbound.setCablePath(cablePath);
			CableController cableController = (CableController )
				getLogicalNetLayer().getMapViewController().getController(cablePath);
			cablePath.addLink(unbound, cableController.generateCCI(unbound));
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
