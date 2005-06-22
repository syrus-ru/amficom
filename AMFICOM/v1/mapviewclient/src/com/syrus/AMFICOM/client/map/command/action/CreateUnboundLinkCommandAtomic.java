/**
 * $Id: CreateUnboundLinkCommandAtomic.java,v 1.17 2005/06/22 08:43:47 krupenn Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * создание непривязанной линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
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
		return this.link;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		this.map = this.logicalNetLayer.getMapView().getMap();
		
		try
		{
			this.link = (UnboundLink )UnboundLink.createInstance(
					LoginManager.getUserId(),
					this.startNode, 
					this.endNode, 
					this.logicalNetLayer.getUnboundLinkType());
	
			this.map.addPhysicalLink(this.link);
			setResult(Command.RESULT_OK);
		}
		catch (ApplicationException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

