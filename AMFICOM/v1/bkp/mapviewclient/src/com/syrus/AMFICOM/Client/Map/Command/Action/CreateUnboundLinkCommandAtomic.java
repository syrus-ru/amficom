/**
 * $Id: CreateUnboundLinkCommandAtomic.java,v 1.4 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * создание непривязанной линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundLinkCommandAtomic extends MapActionCommand
{
	MapUnboundLinkElement link;
	
	MapNodeElement startNode;
	MapNodeElement endNode;
	
	Map map;
	
	public CreateUnboundLinkCommandAtomic(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapUnboundLinkElement getLink()
	{
		return link;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		DataSourceInterface dataSource = aContext.getDataSource();
		
		map = logicalNetLayer.getMapView().getMap();
		
		link = new MapUnboundLinkElement(
				dataSource.GetUId( MapPhysicalLinkElement.typ ),
				startNode, 
				endNode, 
				map,
				logicalNetLayer.getUnboundPen());
		Pool.put(
				MapPhysicalLinkElement.typ, 
				link.getId(), 
				link);

		map.addPhysicalLink(link);
	}
	
}

