/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.2 2004/10/06 09:27:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
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
 * @version $Revision: 1.2 $, $Date: 2004/10/06 09:27:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateCollectorCommandAtomic extends MapActionCommand
{
	MapPipePathElement collector;

	String name;
	
	public CreateCollectorCommandAtomic(String name)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.name = name;
	}
	
	public MapPipePathElement getCollector()
	{
		return collector;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();
		
		collector = new MapPipePathElement(
				dataSource.GetUId( MapPipePathElement.typ ),
				name,
				logicalNetLayer.getMapView().getMap());
		
		Pool.put(MapPipePathElement.typ, collector.getId(), collector);

		logicalNetLayer.getMapView().getMap().addCollector(collector);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addCollector(collector);
		Pool.put(MapPipePathElement.typ, collector.getId(), collector);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
		Pool.remove(MapPipePathElement.typ, collector.getId());
	}
}

