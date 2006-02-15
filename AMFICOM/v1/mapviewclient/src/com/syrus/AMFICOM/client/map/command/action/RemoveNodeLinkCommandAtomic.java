/*-
 * $$Id: RemoveNodeLinkCommandAtomic.java,v 1.21 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление фрагмента линии связи из карты - атомарное действие
 * 
 * @version $Revision: 1.21 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
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
		Log.debugMessage("remove node link " //$NON-NLS-1$
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
		StorableObjectPool.delete(this.nodeLink.getId());
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
		StorableObjectPool.delete(this.nodeLink.getId());
	}

	@Override
	public void undo() {
		try {
			StorableObjectPool.putStorableObject(this.nodeLink);
			final MapView mapView = this.logicalNetLayer.getMapView();

			if(this.nodeLink.getPhysicalLink() instanceof UnboundLink) {
				mapView.addUnboundNodeLink(this.nodeLink);
			}
			else {
				mapView.getMap().addNodeLink(this.nodeLink);
			}
		} catch(IllegalObjectEntityException e) {
			Log.errorMessage(e);
		}
	}
}
