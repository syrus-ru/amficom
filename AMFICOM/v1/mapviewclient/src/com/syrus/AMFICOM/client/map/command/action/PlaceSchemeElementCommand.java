/**
 * $Id: PlaceSchemeElementCommand.java,v 1.3 2004/10/19 10:07:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Разместить c[tvysq элемент на карте в соответствии с привязкой
 * или по координатам
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:07:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * Размещеный узел
	 */
	MapSiteNodeElement site = null;
	
	/**
	 * созданный непривязанный элемент
	 */
	MapUnboundNodeElement unbound = null;
	
	/**
	 * размещаемый схемный элемент
	 */
	SchemeElement se = null;
	
	Map map;
	
	/**
	 * экранная точка, в которой размещается элемент
	 */
	Point point = null;

	/**
	 * географическая точка, в которой размещается элемент
	 */
	Point2D.Double coordinatePoint = null;

	public PlaceSchemeElementCommand(
			SchemeElement se,
			Point2D.Double dpoint)
	{
		this.se = se;
		this.coordinatePoint = dpoint;
	}


	public PlaceSchemeElementCommand(
			SchemeElement se,
			Point point)
	{
		this.se = se;
		this.point = point;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled("mapActionCreateEquipment"))
			return;
		
		// если географическая точка не задана, получить ее из экранной точки
		if(coordinatePoint == null)
			coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();

		site = mapView.findElement(se);
		if(site == null)
		{
			MapElement me = logicalNetLayer.getMapElementAtPoint(point);
			
			if(me instanceof MapSiteNodeElement
				&& !(me instanceof MapUnboundNodeElement))
			{
				site = (MapSiteNodeElement )me;
				se.siteId = site.getId();
			}
			else
			{
				unbound = super.createUnboundNode(coordinatePoint, se);
				site = unbound;
			}
			
			mapView.scanCables((Scheme )Pool.get(Scheme.typ, se.getSchemeId()));
		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				site, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);
		logicalNetLayer.notifyCatalogueEvent(site);

	}
}
