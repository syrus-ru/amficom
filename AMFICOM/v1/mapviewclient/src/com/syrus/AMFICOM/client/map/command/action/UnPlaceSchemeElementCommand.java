/*-
 * $$Id: UnPlaceSchemeElementCommand.java,v 1.29 2005/10/18 07:21:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 * @version $Revision: 1.29 $, $Date: 2005/10/18 07:21:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "unplace scheme element " //$NON-NLS-1$
					+ this.schemeElement.getName()
					+ " (" + this.schemeElement.getId() + ") from site " //$NON-NLS-1$ //$NON-NLS-2$
					+ this.node.getName()
					+ " (" + this.node.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();

		try {
			if(this.node instanceof UnboundNode)
				super.removeNode(this.node);
			this.schemeElement.setSiteNode(null);
			this.logicalNetLayer.getMapViewController().scanCables(
					this.schemeElement.getParentScheme());
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			Log.debugException(e, Level.SEVERE);
		}
	}
}
