/**
 * $Id: BindUnboundNodeToSiteCommandBundle.java,v 1.5 2004/10/20 10:14:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.util.Iterator;
import java.util.List;

/**
 *  Команда привязывания непривязанного элемента к узлу
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/20 10:14:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * привязываемый элемент
	 */
	MapUnboundNodeElement unbound;
	
	/**
	 * узел
	 */
	MapSiteNodeElement site;

	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundNodeToSiteCommandBundle(
			MapUnboundNodeElement unbound, 
			MapSiteNodeElement site)
	{
		this.unbound = unbound;
		this.site = site;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		// список кабельных путей, включающий привязываемый элемент
		List cablePaths = logicalNetLayer.getMapView().getCablePaths(unbound);
		
		// обновляются концевые узлы кабельных путей
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement )it.next();
			if(cp.getEndNode() == unbound)
				cp.setEndNode(site);
			if(cp.getStartNode() == unbound)
				cp.setStartNode(site);
		}

		//При привязывании меняются концевые узлы линий и фрагментов линий
		for(Iterator it = unbound.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			MapPhysicalLinkElement physicalLink = map
					.getPhysicalLink(nodeLink.getPhysicalLinkId());

			MapElementState pls = nodeLink.getState();
					
			if(nodeLink.getEndNode() == unbound)
				nodeLink.setEndNode(site);
			if(nodeLink.getStartNode() == unbound)
				nodeLink.setStartNode(site);

			super.registerStateChange(nodeLink, pls, nodeLink.getState());
				
			MapElementState pls2 = physicalLink.getState();

			if(physicalLink.getEndNode() == unbound)
				physicalLink.setEndNode(site);
			if(physicalLink.getStartNode() == unbound)
				physicalLink.setStartNode(site);

			super.registerStateChange(physicalLink, pls2, physicalLink.getState());
			
		}//while(e.hasNext())

		super.removeNode(unbound);

		SchemeElement se = unbound.getSchemeElement();
		se.siteId = site.getId();

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
}

