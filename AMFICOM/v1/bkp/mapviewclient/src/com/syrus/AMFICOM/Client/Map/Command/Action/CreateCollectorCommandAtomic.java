/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.8 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.Collector;

/**
 * создание коллектора, внесение его в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
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
		return this.collector;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		try
		{
			this.collector = Collector.createInstance(
					this.logicalNetLayer.getUserId(),
					this.logicalNetLayer.getMapView().getMap(),
					this.name,
					"");
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeCollector(this.collector);
	}
}

