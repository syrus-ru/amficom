/**
 * $Id: PlaceSchemeElementCommand.java,v 1.12 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

/**
 * Разместить c[tvysq элемент на карте в соответствии с привязкой
 * или по координатам
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
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
	SchemeElement schemeElement = null;
	
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
			SchemeElement schemeElement,
			DoublePoint dpoint)
	{
		this.schemeElement = schemeElement;
		this.coordinatePoint = dpoint;
	}


	public PlaceSchemeElementCommand(
			SchemeElement se,
			Point point)
	{
		this.schemeElement = se;
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
		if(this.coordinatePoint == null)
			this.coordinatePoint = this.logicalNetLayer.convertScreenToMap(this.point);
		
		MapView mapView = this.logicalNetLayer.getMapView();
		this.map = mapView.getMap();

		this.site = mapView.findElement(this.schemeElement);
		if(this.site == null)
		{
			MapElement mapElement = this.logicalNetLayer.getMapElementAtPoint(this.point);
			
			if(mapElement instanceof SiteNode
				&& !(mapElement instanceof UnboundNode))
			{
				this.site = (SiteNode )mapElement;
				this.schemeElement.siteNodeImpl(this.site);
			}
			else
			{
				this.unbound = super.createUnboundNode(this.coordinatePoint, this.schemeElement);
				this.site = this.unbound;
			}
			
			this.logicalNetLayer.getMapViewController().scanCables(this.schemeElement.scheme());
		}

		// операция закончена - оповестить слушателей
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				this.site, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		this.logicalNetLayer.setCurrentMapElement(this.site);
		this.logicalNetLayer.notifySchemeEvent(this.site);

	}
}
