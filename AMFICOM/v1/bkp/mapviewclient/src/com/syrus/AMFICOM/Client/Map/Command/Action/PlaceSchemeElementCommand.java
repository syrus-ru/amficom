/**
 * $Id: PlaceSchemeElementCommand.java,v 1.8 2004/12/24 15:42:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Разместить c[tvysq элемент на карте в соответствии с привязкой
 * или по координатам
 * 
 * @version $Revision: 1.8 $, $Date: 2004/12/24 15:42:12 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * Размещеный узел
	 */
	SiteNode site = null;
	
	/**
	 * созданный непривязанный элемент
	 */
	UnboundNode unbound = null;
	
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
	DoublePoint coordinatePoint = null;

	public PlaceSchemeElementCommand(
			SchemeElement se,
			DoublePoint dpoint)
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
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
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
			
			if(me instanceof SiteNode
				&& !(me instanceof UnboundNode))
			{
				site = (SiteNode )me;
				se.siteNodeImpl(site);
			}
			else
			{
				unbound = super.createUnboundNode(coordinatePoint, se);
				site = unbound;
			}
			
			mapView.scanCables(se.scheme());
		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				site, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);

	}
}
