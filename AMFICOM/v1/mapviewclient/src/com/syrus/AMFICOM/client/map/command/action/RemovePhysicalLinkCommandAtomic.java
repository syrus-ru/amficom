/*-
 * $$Id: RemovePhysicalLinkCommandAtomic.java,v 1.22 2006/04/14 12:04:07 arseniy Exp $$
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление физической линии из карты - атомарное действие
 * 
 * @version $Revision: 1.22 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class RemovePhysicalLinkCommandAtomic extends MapActionCommand {
	PhysicalLink link;

	public RemovePhysicalLinkCommandAtomic(PhysicalLink link) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.link = link;
	}

	public PhysicalLink getLink() {
		return this.link;
	}

	@Override
	public void execute() {
		Log.debugMessage("remove physicalLink " //$NON-NLS-1$
					+ this.link.getName()
					+ " (" + this.link.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		final MapView mapView = this.logicalNetLayer.getMapView();
		if(this.link instanceof UnboundLink) {
			mapView.removeUnboundLink((UnboundLink) this.link);
		}
		else {
			mapView.getMap().removePhysicalLink(this.link);
		}
		StorableObjectPool.delete(this.link.getId());
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		final MapView mapView = this.logicalNetLayer.getMapView();
		if(this.link instanceof UnboundLink) {
			mapView.removeUnboundLink((UnboundLink) this.link);
		}
		else {
			mapView.getMap().removePhysicalLink(this.link);
		}
		StorableObjectPool.delete(this.link.getId());
	}

	@Override
	public void undo() {
		Log.errorMessage(NOT_IMPLEMENTED);

//		Этот метод всё равно не работает
//		try {
//			StorableObjectPool.putStorableObject(this.link);
//			final MapView mapView = this.logicalNetLayer.getMapView();
//			if(this.link instanceof UnboundLink) {
//				mapView.addUnboundLink((UnboundLink) this.link);
//			}
//			else {
//				mapView.getMap().addPhysicalLink(this.link);
//			}
//		} catch(IllegalObjectEntityException e) {
//			Log.errorMessage(e);
//		}
	}
}
