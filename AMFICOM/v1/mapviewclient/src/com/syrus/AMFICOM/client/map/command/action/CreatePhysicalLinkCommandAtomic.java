/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.15 2005/06/22 08:43:47 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
 */
public class CreatePhysicalLinkCommandAtomic extends MapActionCommand
{
	/** создаваемая линия */
	PhysicalLink link;
	
	/** начальный узел */
	AbstractNode startNode;
	
	/** конечный узел */
	AbstractNode endNode;
	
	public CreatePhysicalLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public PhysicalLink getLink()
	{
		return this.link;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		try
		{
			this.link = PhysicalLink.createInstance(
					LoginManager.getUserId(),
					this.startNode, 
					this.endNode, 
					this.logicalNetLayer.getCurrentPhysicalLinkType());
			this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
			setResult(Command.RESULT_OK);
		}
		catch (CreateObjectException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
	}
}

