/**
 * $Id: BindUnboundNodeToSiteCommandBundle.java,v 1.10 2005/01/30 15:38:17 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.util.Iterator;
import java.util.List;

/**
 *  Команда привязывания непривязанного элемента к узлу
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/01/30 15:38:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * привязываемый элемент
	 */
	UnboundNode unbound;
	
	/**
	 * узел
	 */
	SiteNode site;

	/**
	 * Карта, на которой производится операция
	 */
	Map map;

	public BindUnboundNodeToSiteCommandBundle(
			UnboundNode unbound, 
			SiteNode site)
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
		List cablePaths = logicalNetLayer.getMapViewController().getCablePaths(unbound);
		
		// обновляются концевые узлы кабельных путей
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getEndNode().equals(unbound))
				cp.setEndNode(site);
			if(cp.getStartNode().equals(unbound))
				cp.setStartNode(site);
		}

//		// обновляются концевые узлы путей измерений
//		for(Iterator it = mapView.getMeasurementPaths(unbound).iterator(); it.hasNext();)
//		{
//			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
//			if(mp.getEndNode() == unbound)
//				mp.setEndNode(site);
//			if(mp.getStartNode() == unbound)
//				mp.setStartNode(site);
//		}

		//При привязывании меняются концевые узлы линий и фрагментов линий
		for(Iterator it = unbound.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink)it.next();
			PhysicalLink physicalLink = nodeLink.getPhysicalLink();

			MapElementState pls = nodeLink.getState();
					
			if(nodeLink.getEndNode().equals(unbound))
				nodeLink.setEndNode(site);
			if(nodeLink.getStartNode().equals(unbound))
				nodeLink.setStartNode(site);

			super.registerStateChange(nodeLink, pls, nodeLink.getState());
				
			MapElementState pls2 = physicalLink.getState();

			if(physicalLink.getEndNode().equals(unbound))
				physicalLink.setEndNode(site);
			if(physicalLink.getStartNode().equals(unbound))
				physicalLink.setStartNode(site);

			super.registerStateChange(physicalLink, pls2, physicalLink.getState());
		}//while(e.hasNext())

		super.removeNode(unbound);

		SchemeElement se = unbound.getSchemeElement();
		se.siteNodeImpl(site);

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
}

