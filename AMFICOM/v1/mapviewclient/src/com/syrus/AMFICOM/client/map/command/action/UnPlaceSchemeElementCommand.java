/**
 * $Id: UnPlaceSchemeElementCommand.java,v 1.9 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

/**
 * убрать привязку схемного элемента с карты
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class UnPlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	SiteNode node = null;
	SchemeElement schemeElement = null;

	MapView mapView;

	public UnPlaceSchemeElementCommand(SiteNode node, SchemeElement schemeElement)
	{
		super();
		this.node = node;
		this.schemeElement = schemeElement;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.mapView = this.logicalNetLayer.getMapView();

		if(this.node instanceof UnboundNode)
			super.removeNode(this.node);

		this.schemeElement.siteNodeImpl(null);

		this.logicalNetLayer.getMapViewController().scanCables(this.schemeElement.scheme());

		// операция закончена - оповестить слушателей
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
