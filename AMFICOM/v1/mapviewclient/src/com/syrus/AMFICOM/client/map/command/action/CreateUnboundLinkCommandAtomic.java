/*-
 * $$Id: CreateUnboundLinkCommandAtomic.java,v 1.26 2005/10/18 07:21:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * �������� ������������� �����, �������� �� � ��� � �� ����� - ���������
 * ��������
 * 
 * @version $Revision: 1.26 $, $Date: 2005/10/18 07:21:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateUnboundLinkCommandAtomic extends MapActionCommand {
	UnboundLink link;

	AbstractNode startNode;
	AbstractNode endNode;

	Map map;

	public CreateUnboundLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public UnboundLink getLink() {
		return this.link;
	}

	@Override
	public void execute() {
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create UnboundLink with start at node "  //$NON-NLS-1$
				+ this.startNode.getName() + " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		final MapView mapView = this.logicalNetLayer.getMapView();
		this.map = mapView.getMap();

		try {
			this.link = (UnboundLink) UnboundLink.createInstance(
					LoginManager.getUserId(),
					this.startNode,
					this.endNode,
					this.logicalNetLayer.getUnboundLinkType());

			mapView.addUnboundLink(this.link);
			setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(ApplicationException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugException(e, Level.SEVERE);
		}
	}

}
