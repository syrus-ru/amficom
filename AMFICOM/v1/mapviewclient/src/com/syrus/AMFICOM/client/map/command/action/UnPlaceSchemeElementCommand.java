/*-
 * $$Id: UnPlaceSchemeElementCommand.java,v 1.34 2006/02/14 10:20:06 stas Exp $$
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
 * ������ �������� �������� �������� � �����
 * 
 * @version $Revision: 1.34 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnPlaceSchemeElementCommand extends MapActionCommandBundle {
	/**
	 * ��������� �������� �����
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
		Log.debugMessage("unplace scheme element " //$NON-NLS-1$
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
			Log.errorMessage(e);
		}
	}
}
