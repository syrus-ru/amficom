/**
 * $Id: UnPlaceSchemeElementCommand.java,v 1.6 2004/12/24 15:42:12 krupenn Exp $
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
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

/**
 * убрать привязку схемного элемента с карты
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/24 15:42:12 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class UnPlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	SiteNode node = null;
	SchemeElement se = null;

	MapView mapView;

	public UnPlaceSchemeElementCommand(SiteNode node, SchemeElement se)
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

		if(node instanceof UnboundNode)
			super.removeNode(node);

		se.siteNodeImpl(null);

		mapView.scanCables(se.scheme());

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
