/**
 * $Id: BindUnboundNodeToSiteCommandBundle.java,v 1.15 2005/03/16 12:54:57 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 *  Команда привязывания непривязанного элемента к узлу.
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/03/16 12:54:57 $
 * @module mapviewclient_v1
 */
public class BindUnboundNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * привязываемый элемент.
	 */
	UnboundNode unbound;
	
	/**
	 * узел.
	 */
	SiteNode site;

	/**
	 * Карта, на которой производится операция.
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

		try
		{
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			// список кабельных путей, включающий привязываемый элемент
			List cablePaths = mapView.getCablePaths(this.unbound);
			// обновляются концевые узлы кабельных путей
			for(Iterator it = cablePaths.iterator(); it.hasNext();)
			{
				CablePath cp = (CablePath)it.next();
				if(cp.getEndNode().equals(this.unbound))
					cp.setEndNode(this.site);
				if(cp.getStartNode().equals(this.unbound))
					cp.setStartNode(this.site);
			}
			//При привязывании меняются концевые узлы линий и фрагментов линий
			for(Iterator it = this.unbound.getNodeLinks().iterator(); it.hasNext();)
			{
				NodeLink nodeLink = (NodeLink)it.next();
				PhysicalLink physicalLink = nodeLink.getPhysicalLink();

				MapElementState pls = nodeLink.getState();
						
				if(nodeLink.getEndNode().equals(this.unbound))
					nodeLink.setEndNode(this.site);
				if(nodeLink.getStartNode().equals(this.unbound))
					nodeLink.setStartNode(this.site);

				super.registerStateChange(nodeLink, pls, nodeLink.getState());
					
				MapElementState pls2 = physicalLink.getState();

				if(physicalLink.getEndNode().equals(this.unbound))
					physicalLink.setEndNode(this.site);
				if(physicalLink.getStartNode().equals(this.unbound))
					physicalLink.setStartNode(this.site);

				super.registerStateChange(physicalLink, pls2, physicalLink.getState());
			}//while(e.hasNext())
			super.removeNode(this.unbound);
			SchemeElement se = this.unbound.getSchemeElement();
			se.setSiteNode(this.site);
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

