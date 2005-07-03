/**
 * $Id: PlaceSchemeElementCommand.java,v 1.21 2005/06/24 12:50:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * Разместить c[tvysq элемент на карте в соответствии с привязкой
 * или по координатам
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/06/24 12:50:40 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		if ( !this.aContext.getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;
		
		try {
			// если географическая точка не задана, получить ее из экранной точки
			if(this.coordinatePoint == null)
				this.coordinatePoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.point);
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			this.site = mapView.findElement(this.schemeElement);
			if(this.site == null)
			{
				MapElement mapElement = this.logicalNetLayer.getMapElementAtPoint(this.point, this.netMapViewer.getVisibleBounds());
				
				if(mapElement instanceof SiteNode
					&& !(mapElement instanceof UnboundNode))
				{
					this.site = (SiteNode )mapElement;
					this.schemeElement.setSiteNode(this.site);
				}
				else
				{
					this.unbound = super.createUnboundNode(this.coordinatePoint, this.schemeElement);
					this.site = this.unbound;
				}
				
				this.logicalNetLayer.getMapViewController().scanCables(this.schemeElement.getParentScheme());
			}
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(this.site);
			this.logicalNetLayer.notifySchemeEvent(this.site);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}

	}
}
