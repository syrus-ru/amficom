/*-
 * $$Id: RemoveCablePathCommandAtomic.java,v 1.21 2005/10/30 15:20:31 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * удаление кабельного пути из карты - атомарное действие
 * 
 * @version $Revision: 1.21 $, $Date: 2005/10/30 15:20:31 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class RemoveCablePathCommandAtomic extends MapActionCommand {
	CablePath cablePath;

	public RemoveCablePathCommandAtomic(CablePath cp) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.cablePath = cp;
	}

	public CablePath getPath() {
		return this.cablePath;
	}

	@Override
	public void execute() {
		assert Log.debugMessage(
			getClass().getName() + "::execute() | " //$NON-NLS-1$
				+ "remove cable path " //$NON-NLS-1$
				+ this.cablePath.getName()
				+ " (" + this.cablePath.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
			Level.FINEST);

		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}
}
