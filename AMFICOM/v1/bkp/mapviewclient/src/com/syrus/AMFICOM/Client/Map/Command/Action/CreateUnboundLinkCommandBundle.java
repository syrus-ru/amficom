/**
 * $Id: CreateUnboundLinkCommandBundle.java,v 1.1 2004/10/14 15:39:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;

import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;

import java.util.HashMap;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/10/14 15:39:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundLinkCommandBundle extends MapActionCommandBundle
{
	MapUnboundLinkElement unbound;
	
	MapNodeElement startNode;
	MapNodeElement endNode;

	MapNodeLinkElement nodeLink;
	
	Map map;
	
	public CreateUnboundLinkCommandBundle(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapUnboundLinkElement getUnbound()
	{
		return unbound;
	}
	
	public void execute()
	{
		unbound = super.createUnboundLinkWithNodeLink(startNode, endNode);
	}
	
}

