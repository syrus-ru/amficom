/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;

/**
 * создание фрагмента линии связи, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class CreateNodeLinkCommandAtomic extends MapActionCommand
{
	MapNodeLinkElement nodeLink;
	
	MapNodeElement startNode;
	MapNodeElement endNode;
	
	public CreateNodeLinkCommandAtomic(		
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapNodeLinkElement getNodeLink()
	{
		return nodeLink;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		nodeLink = new MapNodeLinkElement( 
				dataSource.GetUId( MapNodeLinkElement.typ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapView().getMap());
		Pool.put(MapNodeLinkElement.typ, nodeLink.getId(), nodeLink);

		logicalNetLayer.getMapView().getMap().addNodeLink(nodeLink);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNodeLink(nodeLink);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(nodeLink);
	}
}

