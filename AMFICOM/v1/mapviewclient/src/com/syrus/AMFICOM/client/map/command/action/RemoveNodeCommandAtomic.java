/*-
 * $$Id: RemoveNodeCommandAtomic.java,v 1.20 2005/09/30 16:08:37 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.util.Log;

/**
 * удаление узла из карты - атомарное действие
 * 
 * @version $Revision: 1.20 $, $Date: 2005/09/30 16:08:37 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class RemoveNodeCommandAtomic extends MapActionCommand {
	AbstractNode node;

	public RemoveNodeCommandAtomic(AbstractNode node) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.node = node;
	}

	public AbstractNode getNode() {
		return this.node;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "remove node " //$NON-NLS-1$
					+ this.node.getName()
					+ " (" + this.node.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		if(this.node instanceof Marker) {
			this.logicalNetLayer.getMapView().removeMarker((Marker) this.node);
		}
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}
}
