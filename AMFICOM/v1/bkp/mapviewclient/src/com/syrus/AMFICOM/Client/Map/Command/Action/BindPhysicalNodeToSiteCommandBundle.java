/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.1 2004/10/06 14:11:56 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.util.Iterator;

/**
 *  Команда удаления элемента наследника класса MapNodeElement. Команда
 * состоит из  последовательности атомарных действий
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/10/06 14:11:56 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * Удаляемый узел
	 */
	MapPhysicalNodeElement node;
	MapSiteNodeElement site;

	String prevSiteId;
	
	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(MapPhysicalNodeElement node, MapSiteNodeElement site)
	{
		this.node = node;
		this.site = site;
	}
	
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		map = logicalNetLayer.getMapView().getMap();
/*
		//При удалении узла удаляются все фрагменты линий, исходящие из него
		java.util.List nodeLinks = unbound.getNodeLinks();
		Iterator e = nodeLinks.iterator();

		for(Iterator it = logicalNetLayer.getMapView().getCablePaths(unbound).iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement )it.next();
			if(cp.getEndNode() == unbound)
				cp.setEndNode(site);
			if(cp.getStartNode() == unbound)
				cp.setStartNode(site);
		}

		// бежим по списку удаляемых фрагментов
		while(e.hasNext())
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			MapPhysicalLinkElement physicalLink = map
					.getPhysicalLink(nodeLink.getPhysicalLinkId());

			MapElementState pls = nodeLink.getState();
					
			if(nodeLink.getEndNode() == unbound)
				nodeLink.setEndNode(site);
			if(nodeLink.getStartNode() == unbound)
				nodeLink.setStartNode(site);

			registerStateChange(nodeLink, pls, nodeLink.getState());
				
			MapElementState pls2 = physicalLink.getState();

			if(physicalLink.getEndNode() == unbound)
				physicalLink.setEndNode(site);
			if(physicalLink.getStartNode() == unbound)
				physicalLink.setStartNode(site);

			registerStateChange(physicalLink, pls2, physicalLink.getState());
			
		}//while(e.hasNext())

		removeNode(unbound);
		
		SchemeElement se = unbound.getSchemeElement();
		prevSiteId = se.siteId;		
		se.siteId = site.getId();
*/
		logicalNetLayer.repaint();
	}
/*
	public void undo()
	{
		super.undo();
		
		unbound.setCanBind(false);
		SchemeElement se = unbound.getSchemeElement();
		se.siteId = prevSiteId;
	}

	public void redo()
	{
		super.redo();
		
		SchemeElement se = unbound.getSchemeElement();
		se.siteId = site.getId();
	}
*/
}

