/*-
 * $$Id: RemoveNodeLinkCommandAtomic.java,v 1.22 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление фрагмента линии связи из карты - атомарное действие
 * 
 * @version $Revision: 1.22 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
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
		Log.errorMessage(NOT_IMPLEMENTED);

//		Этот метод всё равно не работает
//		try {
//			StorableObjectPool.putStorableObject(this.nodeLink);
//			final MapView mapView = this.logicalNetLayer.getMapView();
//
//			if(this.nodeLink.getPhysicalLink() instanceof UnboundLink) {
//				mapView.addUnboundNodeLink(this.nodeLink);
//			}
//			else {
//				mapView.getMap().addNodeLink(this.nodeLink);
//			}
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}
}
