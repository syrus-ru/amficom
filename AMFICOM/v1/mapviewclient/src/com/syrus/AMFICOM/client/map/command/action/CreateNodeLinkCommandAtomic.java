/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.4 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * создание фрагмента линии связи, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand
{
	/**
	 * создаваемый фрагмент линии
	 */
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		DataSourceInterface dataSource = aContext.getDataSource();

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
		Pool.put(MapNodeLinkElement.typ, nodeLink.getId(), nodeLink);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(nodeLink);
		Pool.remove(MapNodeLinkElement.typ, nodeLink.getId());
	}
}

