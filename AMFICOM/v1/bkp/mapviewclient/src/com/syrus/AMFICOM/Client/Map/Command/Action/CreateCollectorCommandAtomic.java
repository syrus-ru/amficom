/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.4 2004/12/22 16:38:39 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * создание коллектора, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateCollectorCommandAtomic extends MapActionCommand
{
	/** коллектор */
	Collector collector;

	/** название */
	String name;
	
	public CreateCollectorCommandAtomic(String name)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.name = name;
	}
	
	public Collector getCollector()
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
		
		try
		{
			collector = Collector.createInstance(
					logicalNetLayer.getMapView().getMap(),
					name);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		logicalNetLayer.getMapView().getMap().addCollector(collector);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addCollector(collector);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
	}
}

