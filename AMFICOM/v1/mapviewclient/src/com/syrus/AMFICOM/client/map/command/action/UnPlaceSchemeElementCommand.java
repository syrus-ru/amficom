/**
 * $Id: UnPlaceSchemeElementCommand.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

/**
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class UnPlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	MapSiteNodeElement node = null;
	SchemeElement se = null;

	MapView mapView;

	public UnPlaceSchemeElementCommand(MapSiteNodeElement node, SchemeElement se)
	{
		super();
		this.node = node;
		this.se = se;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		mapView = logicalNetLayer.getMapView();

		if(node instanceof MapUnboundNodeElement)
			super.removeNode(node);

		se.siteId = "";

		mapView.scanCables((Scheme )Pool.get(Scheme.typ, se.getSchemeId()));

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
//		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
//				cablePath, 
//				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
//		logicalNetLayer.setCurrentMapElement(cablePath);
//		logicalNetLayer.notifySchemeEvent(cablePath);
//		logicalNetLayer.notifyCatalogueEvent(cablePath);
	}
}
