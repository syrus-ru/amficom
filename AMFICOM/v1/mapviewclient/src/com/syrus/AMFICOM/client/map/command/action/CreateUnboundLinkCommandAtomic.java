/**
 * $Id: CreateUnboundLinkCommandAtomic.java,v 1.7 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * создание непривязанной линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundLinkCommandAtomic extends MapActionCommand
{
	UnboundLink link;
	
	AbstractNode startNode;
	AbstractNode endNode;
	
	Map map;
	
	public CreateUnboundLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public UnboundLink getLink()
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
		
		map = logicalNetLayer.getMapView().getMap();
		
		try
		{
			link = com.syrus.AMFICOM.mapview.UnboundLink.createInstance(
					startNode, 
					endNode, 
					map,
					logicalNetLayer.getUnboundPen());
	
			map.addPhysicalLink(link);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}
	}
	
}

