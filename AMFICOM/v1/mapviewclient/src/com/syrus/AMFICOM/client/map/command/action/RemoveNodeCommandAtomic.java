/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.4 2004/11/01 15:40:10 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление узла из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/11/01 15:40:10 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveNodeCommandAtomic extends MapActionCommand
{
	MapNodeElement node;
	
	public RemoveNodeCommandAtomic(MapNodeElement node)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = node;
	}
	
	public MapNodeElement getNode()
	{
		return node;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().getMap().removeNode(node);
		if(node instanceof MapMarker)
		{
			logicalNetLayer.getMapView().removeMarker((MapMarker )node);
		}
		Pool.remove(node.getTyp(), node.getId());
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
		Pool.remove(node.getTyp(), node.getId());
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
		Pool.put(node.getTyp(), node.getId(), node);
	}
}

