/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.12 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.Marker;

/**
 * удаление узла из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
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
		return this.node;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		if(this.node instanceof Marker)
		{
			this.logicalNetLayer.getMapView().removeMarker((Marker)this.node);
		}
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}
}

