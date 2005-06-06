/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.13 2005/06/06 12:20:30 krupenn Exp $
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
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.Marker;

/**
 * удаление узла из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/06 12:20:30 $
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

