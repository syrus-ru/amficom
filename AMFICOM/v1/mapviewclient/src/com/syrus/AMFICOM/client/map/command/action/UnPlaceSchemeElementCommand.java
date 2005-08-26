/**
 * $Id: UnPlaceSchemeElementCommand.java,v 1.24 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * убрать привязку схемного элемента с карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.24 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class UnPlaceSchemeElementCommand extends MapActionCommandBundle {
	/**
	 * Выбранный фрагмент линии
	 */
	SiteNode node = null;
	SchemeElement schemeElement = null;

	MapView mapView;

	public UnPlaceSchemeElementCommand(
			SiteNode node,
			SchemeElement schemeElement) {
		super();
		this.node = node;
		this.schemeElement = schemeElement;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | "
					+ "unplace scheme element "
					+ this.schemeElement.getName()
					+ " (" + this.schemeElement.getId() + ") from site "
					+ this.node.getName()
					+ " (" + this.node.getId() + ")", 
				Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();

		try {
			if(this.node instanceof UnboundNode)
				super.removeNode(this.node);
			this.logicalNetLayer.getMapViewController().scanCables(
					this.schemeElement.getParentScheme());
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
