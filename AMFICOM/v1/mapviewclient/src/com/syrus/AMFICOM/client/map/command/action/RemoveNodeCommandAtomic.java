/*-
 * $$Id: RemoveNodeCommandAtomic.java,v 1.27 2006/04/14 12:04:07 arseniy Exp $$
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.util.Log;

/**
 * удаление узла из карты - атомарное действие
 * 
 * @version $Revision: 1.27 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
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
		Log.debugMessage("remove node " //$NON-NLS-1$
					+ this.node.getName()
					+ " (" + this.node.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		if(this.node instanceof UnboundNode) {
			this.logicalNetLayer.getMapView().removeUnboundNode((UnboundNode) this.node);
		}
		else if(this.node instanceof Marker) {
			this.logicalNetLayer.getMapView().removeMarker((Marker) this.node);
		}
		else {
			this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		}
		StorableObjectPool.delete(this.node.getId());
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		if(this.node instanceof UnboundNode) {
			this.logicalNetLayer.getMapView().removeUnboundNode((UnboundNode) this.node);
		}
		else if(this.node instanceof Marker) {
			this.logicalNetLayer.getMapView().removeMarker((Marker) this.node);
		}
		else {
			this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
		}
		StorableObjectPool.delete(this.node.getId());
	}

	@Override
	public void undo() {
		Log.errorMessage(NOT_IMPLEMENTED);

//		Этот метод всё равно не работает
//		try {
//			StorableObjectPool.putStorableObject(this.node);
//			if(this.node instanceof UnboundNode) {
//				this.logicalNetLayer.getMapView().addUnboundNode((UnboundNode) this.node);
//			}
//			else if(this.node instanceof Marker) {
//				this.logicalNetLayer.getMapView().addMarker((Marker) this.node);
//			}
//			else {
//				this.logicalNetLayer.getMapView().getMap().addNode(this.node);
//			}
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}
}
