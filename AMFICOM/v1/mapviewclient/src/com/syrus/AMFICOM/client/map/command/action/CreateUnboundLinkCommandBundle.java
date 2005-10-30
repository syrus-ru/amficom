/*-
 * $$Id: CreateUnboundLinkCommandBundle.java,v 1.19 2005/10/30 14:48:56 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * создание непривязанной линии, состоящей из одного фрагмента, внесение ее в
 * пул и на карту
 * 
 * @version $Revision: 1.19 $, $Date: 2005/10/30 14:48:56 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateUnboundLinkCommandBundle extends MapActionCommandBundle {
	UnboundLink unbound;

	AbstractNode startNode;
	AbstractNode endNode;

	public CreateUnboundLinkCommandBundle(
			AbstractNode startNode,
			AbstractNode endNode) {
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public UnboundLink getUnbound() {
		return this.unbound;
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

		try {
			this.unbound = super.createUnboundLinkWithNodeLink(
					this.startNode,
					this.endNode);
			setUndoable(false);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugMessage(e, Level.SEVERE);
		}
	}

}
