/**
 * $Id: RemoveCollectorCommandAtomic.java,v 1.4 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * удаление коллектора из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return collector;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().getMap().removeCollector(collector);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addCollector(collector);
	}
}

