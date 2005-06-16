/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.13 2005/06/16 10:57:19 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * создание фрагмента линии связи, внесение ее в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/16 10:57:19 $
 * @module mapviewclient_v1
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand
{
	/**
	 * создаваемый фрагмент линии
	 */
	NodeLink nodeLink;
	
	AbstractNode startNode;
	AbstractNode endNode;
	PhysicalLink physicalLink;
	
	public CreateNodeLinkCommandAtomic(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public NodeLink getNodeLink()
	{
		return this.nodeLink;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		try
		{
			this.nodeLink = NodeLink.createInstance(
					LoginManager.getUserId(),
					this.physicalLink, 
					this.startNode, 
					this.endNode);

			this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
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
		this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
	}
}

