/*-
 * $$Id: CreateNodeLinkCommandAtomic.java,v 1.30 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * ???????? ????????? ????? ?????, ???????? ?? ? ??? ? ?? ????? - 
 * ????????? ????????
 *  
 * @version $Revision: 1.30 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand {
	/**
	 * ??????????? ???????? ?????
	 */
	NodeLink nodeLink;

	AbstractNode startNode;
	AbstractNode endNode;
	PhysicalLink physicalLink;

	public CreateNodeLinkCommandAtomic(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	@Override
	public void execute() {
		Log.debugMessage("create NodeLink for link "  //$NON-NLS-1$
				+ this.physicalLink.getName() 
				+ " (" + this.physicalLink.getId()  //$NON-NLS-1$
				+ ") with start at node " + this.startNode.getName()  //$NON-NLS-1$
				+ " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		final MapView mapView = this.logicalNetLayer.getMapView();
		try {
			this.nodeLink = NodeLink.createInstance(
					LoginManager.getUserId(),
					this.physicalLink,
					this.startNode,
					this.endNode);

			if(this.physicalLink instanceof UnboundLink) {
				mapView.addUnboundNodeLink(this.nodeLink);
			}
			else {
				mapView.getMap().addNodeLink(this.nodeLink);
			}
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		}
	}

	@Override
	public void redo() {
		Log.errorMessage(NOT_IMPLEMENTED);

//		???? ????? ??? ????? ?? ????????
//		try {
//			final MapView mapView = this.logicalNetLayer.getMapView();
//
//			StorableObjectPool.putStorableObject(this.nodeLink);
//			if(this.physicalLink instanceof UnboundLink) {
//				mapView.addUnboundNodeLink(this.nodeLink);
//			}
//			else {
//				mapView.getMap().addNodeLink(this.nodeLink);
//			}
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}

	@Override
	public void undo() {
		final MapView mapView = this.logicalNetLayer.getMapView();

		if(this.physicalLink instanceof UnboundLink) {
			mapView.removeUnboundNodeLink(this.nodeLink);
		}
		else {
			mapView.getMap().removeNodeLink(this.nodeLink);
		}
		StorableObjectPool.delete(this.nodeLink.getId());
	}
}

