/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.16 2005/08/17 14:14:16 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * создание коллектора, внесение его в пул и на карту - 
 * атомарное действие 
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/08/17 14:14:16 $
 * @module mapviewclient
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
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		
		try
		{
			this.collector = Collector.createInstance(
					LoginManager.getUserId(),
					this.logicalNetLayer.getMapView().getMap(),
					this.name,
					"");
			this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
			setResult(Command.RESULT_OK);
		}
		catch (CreateObjectException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	@Override
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}
	
	@Override
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeCollector(this.collector);
	}
}

