/*-
 * $$Id: RemoveMeasurementPathCommandAtomic.java,v 1.21 2005/10/30 15:20:31 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * удаление измерительного пути из карты - атомарное действие
 * 
 * @version $Revision: 1.21 $, $Date: 2005/10/30 15:20:31 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand {
	MeasurementPath measuremetnPath;

	public RemoveMeasurementPathCommandAtomic(MeasurementPath mp) {
		super(MapActionCommand.ACTION_DROP_LINE);
		this.measuremetnPath = mp;
	}

	public MeasurementPath getPath() {
		return this.measuremetnPath;
	}

	@Override
	public void execute() {
		assert Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "remove measurement path " //$NON-NLS-1$
					+ this.measuremetnPath.getName()
					+ " (" + this.measuremetnPath.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.logicalNetLayer.getMapView().removeMeasurementPath(
				this.measuremetnPath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo() {
		this.logicalNetLayer.getMapView().removeMeasurementPath(
				this.measuremetnPath);
	}

	@Override
	public void undo() {
		this.logicalNetLayer.getMapView().addMeasurementPath(
				this.measuremetnPath);
	}
}
