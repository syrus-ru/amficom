/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * создание фрагмента линии связи, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand
{
	/**
	 * создаваемый фрагмент линии
	 */
	NodeLink nodeLink;
	
	AbstractNode startNode;
	AbstractNode endNode;
	
	public CreateNodeLinkCommandAtomic(		
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public NodeLink getNodeLink()
	{
		return nodeLink;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		DataSourceInterface dataSource = aContext.getDataSource();

		try
		{
			nodeLink = NodeLink.createInstance(startNode, endNode);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		logicalNetLayer.getMapView().getMap().addNodeLink(nodeLink);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNodeLink(nodeLink);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(nodeLink);
	}
}

