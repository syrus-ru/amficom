/**
 * $Id: RemoveCollectorCommandAtomic.java,v 1.1 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление узла из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveCollectorCommandAtomic extends MapActionCommand
{
	MapPipePathElement collector;
	
	public RemoveCollectorCommandAtomic(MapPipePathElement collector)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.collector = collector;
	}
	
	public MapPipePathElement getCollector()
	{
		return collector;
	}
	
	public void execute()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
		Pool.remove(MapPipePathElement.typ, collector.getId());
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeCollector(collector);
		Pool.remove(MapPipePathElement.typ, collector.getId());
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addCollector(collector);
		Pool.put(MapPipePathElement.typ, collector.getId(), collector);
	}
}

