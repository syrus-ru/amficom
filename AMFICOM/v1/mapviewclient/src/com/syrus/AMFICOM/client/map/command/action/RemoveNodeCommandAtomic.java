/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * удаление узла из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveNodeCommandAtomic extends MapActionCommand
{
	AbstractNode node;
	
	public RemoveNodeCommandAtomic(AbstractNode node)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = node;
	}
	
	public AbstractNode getNode()
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
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
}

