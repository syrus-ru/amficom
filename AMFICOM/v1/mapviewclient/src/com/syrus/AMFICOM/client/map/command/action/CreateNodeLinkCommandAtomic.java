/*-
 * $$Id: CreateNodeLinkCommandAtomic.java,v 1.21 2005/10/12 13:07:08 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * �������� ��������� ����� �����, �������� �� � ��� � �� ����� - 
 * ��������� ��������
 *  
 * @version $Revision: 1.21 $, $Date: 2005/10/12 13:07:08 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand {
	/**
	 * ����������� �������� �����
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
		Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "create NodeLink for link "  //$NON-NLS-1$
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
			e.printStackTrace();
		}
	}

	@Override
	public void redo() {
		final MapView mapView = this.logicalNetLayer.getMapView();

		if(this.physicalLink instanceof UnboundLink) {
			mapView.addUnboundNodeLink(this.nodeLink);
		}
		else {
			mapView.getMap().addNodeLink(this.nodeLink);
		}
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
	}
}

