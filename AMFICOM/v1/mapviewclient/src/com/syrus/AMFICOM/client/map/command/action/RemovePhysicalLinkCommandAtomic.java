/*-
 * $$Id: RemovePhysicalLinkCommandAtomic.java,v 1.16 2005/10/12 13:07:08 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление физической линии из карты - атомарное действие
 * 
 * @version $Revision: 1.16 $, $Date: 2005/10/12 13:07:08 $
 * @author $Author: krupenn $
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
		Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "remove physicalLink " //$NON-NLS-1$
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
	}

	@Override
	public void undo() {
		final MapView mapView = this.logicalNetLayer.getMapView();
		if(this.link instanceof UnboundLink) {
			mapView.addUnboundLink((UnboundLink) this.link);
		}
		else {
			mapView.getMap().addPhysicalLink(this.link);
		}
	}
}
