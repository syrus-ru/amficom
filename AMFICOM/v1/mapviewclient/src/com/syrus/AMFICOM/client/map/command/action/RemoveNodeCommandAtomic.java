/**
 * $Id: RemoveNodeCommandAtomic.java,v 1.14 2005/06/22 08:43:47 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.util.Log;

/**
 * удаление узла из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/06/22 08:43:47 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

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

