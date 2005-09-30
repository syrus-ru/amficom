/*-
 * $$Id: CreateNodeLinkCommandAtomic.java,v 1.20 2005/09/30 16:08:36 krupenn Exp $$
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
import com.syrus.util.Log;

/**
 * создание фрагмента линии связи, внесение ее в пул и на карту - 
 * атомарное действие
 *  
 * @version $Revision: 1.20 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand {
	/**
	 * создаваемый фрагмент линии
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

		try {
			this.nodeLink = NodeLink.createInstance(
					LoginManager.getUserId(),
					this.physicalLink,
					this.startNode,
					this.endNode);

			this.logicalNetLayer.getMapView().getMap().addNodeLink(
					this.nodeLink);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
	}
}

