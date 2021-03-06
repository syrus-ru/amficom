/*-
 * $$Id: CreatePhysicalLinkCommandAtomic.java,v 1.32 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * ???????? ?????????? ?????, ???????? ?? ? ??? ? ?? ????? - 
 * ????????? ????????
 *  
 * @version $Revision: 1.32 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreatePhysicalLinkCommandAtomic extends MapActionCommand {
	/** ??????????? ????? */
	PhysicalLink link;

	/** ????????? ???? */
	AbstractNode startNode;

	/** ???????? ???? */
	AbstractNode endNode;

	public CreatePhysicalLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode) {
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public PhysicalLink getLink() {
		return this.link;
	}

	@Override
	public void execute() {
		Log.debugMessage("create PhysicalLink with start at node "  //$NON-NLS-1$
				+ this.startNode.getName() + " (" + this.startNode.getId()  //$NON-NLS-1$
				+ ") and end at node " + this.endNode.getName()  //$NON-NLS-1$
				+ " (" + this.endNode.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		try {
			this.link = PhysicalLink.createInstance(
					LoginManager.getUserId(),
					this.startNode.getId(),
					this.endNode.getId(),
					this.logicalNetLayer.getCurrentPhysicalLinkType());
			this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
			setResult(Command.RESULT_OK);
		} catch(CreateObjectException e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.errorMessage(e);
		} catch(ApplicationException e) {
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
//			StorableObjectPool.putStorableObject(this.link);
//			this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
		StorableObjectPool.delete(this.link.getId());
	}
}
