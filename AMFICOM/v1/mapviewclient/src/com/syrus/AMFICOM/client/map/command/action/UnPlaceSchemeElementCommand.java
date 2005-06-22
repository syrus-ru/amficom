/**
 * $Id: UnPlaceSchemeElementCommand.java,v 1.17 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * убрать привязку схемного элемента с карты
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/22 08:43:47 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		this.mapView = this.logicalNetLayer.getMapView();

		try {
			if(this.node instanceof UnboundNode)
				super.removeNode(this.node);
			this.schemeElement.setSiteNode(null);
			this.logicalNetLayer.getMapViewController().scanCables(this.schemeElement.getParentScheme());
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
