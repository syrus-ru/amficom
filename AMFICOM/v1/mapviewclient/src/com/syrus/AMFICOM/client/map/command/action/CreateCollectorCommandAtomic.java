/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * создание коллектора, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateCollectorCommandAtomic extends MapActionCommand
{
	/** коллектор */
	MapPipePathElement collector;

	/** название */
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
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

