/**
 * $Id: RemoveCollectorCommandAtomic.java,v 1.9 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.util.Log;

/**
 * удаление коллектора из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
 */
public class RemoveCollectorCommandAtomic extends MapActionCommand
{
	Collector collector;
	
	public RemoveCollectorCommandAtomic(Collector collector)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.collector = collector;
	}
	
	public Collector getCollector()
	{
		return this.collector;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		this.logicalNetLayer.getMapView().getMap().removeCollector(this.collector);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removeCollector(this.collector);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}
}

