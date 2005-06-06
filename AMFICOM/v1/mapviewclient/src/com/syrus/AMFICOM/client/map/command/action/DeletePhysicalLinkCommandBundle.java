/**
 * $Id: DeletePhysicalLinkCommandBundle.java,v 1.17 2005/06/06 12:57:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.map.controllers.CableController;
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
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/06 12:57:01 $
 * @module mapviewclient_v1
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
		if(this.link.isRemoved())
			return;

		setResult(Command.RESULT_OK);

		try
		{
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			List cablePathsToScan = mapView.getCablePaths(this.link);
			this.link.sortNodes();
			/// удаляются все топологические узлы линии
			for(Iterator it = this.link.getSortedNodes().iterator(); it.hasNext();)
			{
				AbstractNode ne = (AbstractNode)it.next();
				if(ne instanceof TopologicalNode)
				{
					TopologicalNode node = (TopologicalNode)ne;
					super.removeNode(node);
				}
			}
			// удаляются все фрагменты линии
			for(Iterator it = this.link.getNodeLinks().iterator(); it.hasNext();)
			{
				NodeLink nodeLink = (NodeLink)it.next();
				super.removeNodeLink(nodeLink);
			}
			// удаляется сама линия
			super.removePhysicalLink(this.link);
			// проверяются все кабельные пути, которые проходили по удаленной линии,
			// и прохождение по ней заменяется непривязанной связью
			for(Iterator it = cablePathsToScan.iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();
				
				cablePath.removeLink(this.link);
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(
						this.link.getStartNode(),
						this.link.getEndNode());
				unbound.setCablePath(cablePath);
				cablePath.addLink(unbound, CableController.generateCCI(cablePath, unbound, this.logicalNetLayer.getUserId()));
			}
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
