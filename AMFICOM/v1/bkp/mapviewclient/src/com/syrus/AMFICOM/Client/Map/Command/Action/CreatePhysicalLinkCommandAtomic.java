/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.util.HashMap;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class CreatePhysicalLinkCommandAtomic extends MapActionCommand
{
	MapPhysicalLinkElement link;
	
	MapNodeElement startNode;
	MapNodeElement endNode;
	
	public CreatePhysicalLinkCommandAtomic(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapPhysicalLinkElement getLink()
	{
		return link;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		
		link = new MapPhysicalLinkElement( 
				dataSource.GetUId( MapPhysicalLinkElement.typ ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapView().getMap(),
				logicalNetLayer.getPen());
		Pool.put(
				MapPhysicalLinkElement.typ, 
				link.getId(), 
				link);

		// копировать атрибуты отображения из протоэлемента
		link.attributes = (HashMap )ResourceUtil.copyAttributes(
				dataSource, 
				logicalNetLayer.getPen().attributes);

		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
	}
}

