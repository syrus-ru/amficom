/*-
 * $$Id: RemoveNodeLinkCommandAtomic.java,v 1.16 2005/10/12 13:07:08 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление фрагмента линии связи из карты - атомарное действие
 * 
 * @version $Revision: 1.16 $, $Date: 2005/10/12 13:07:08 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class RemoveNodeLinkCommandAtomic extends MapActionCommand {
	NodeLink nodeLink;

	public RemoveNodeLinkCommandAtomic(NodeLink nodeLink) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.nodeLink = nodeLink;
	}

	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "remove node link " //$NON-NLS-1$
					+ this.nodeLink.getName()
					+ " (" + this.nodeLink.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		final MapView mapView = this.logicalNetLayer.getMapView();

		if(this.nodeLink.getPhysicalLink() instanceof UnboundLink) {
			mapView.removeUnboundNodeLink(this.nodeLink);
		}
		else {
			mapView.getMap().removeNodeLink(this.nodeLink);
		}
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		final MapView mapView = this.logicalNetLayer.getMapView();

		if(this.nodeLink.getPhysicalLink() instanceof UnboundLink) {
			mapView.removeUnboundNodeLink(this.nodeLink);
		}
		else {
			mapView.getMap().removeNodeLink(this.nodeLink);
		}
	}

	@Override
	public void undo() {
		final MapView mapView = this.logicalNetLayer.getMapView();

		if(this.nodeLink.getPhysicalLink() instanceof UnboundLink) {
			mapView.addUnboundNodeLink(this.nodeLink);
		}
		else {
			mapView.getMap().addNodeLink(this.nodeLink);
		}
	}
}
