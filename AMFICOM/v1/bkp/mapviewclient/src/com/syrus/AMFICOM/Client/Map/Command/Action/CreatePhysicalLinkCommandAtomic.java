/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return link;
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
			link = PhysicalLink.createInstance(
					startNode, 
					endNode, 
					logicalNetLayer.getPen());
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
	}
}

