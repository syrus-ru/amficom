/*-
 * $$Id: UnPlaceSchemePathCommand.java,v 1.25 2005/10/30 15:20:30 bass Exp $$
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
 * убрать привязку измерительного пути с карты
 * 
 * @version $Revision: 1.25 $, $Date: 2005/10/30 15:20:30 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnPlaceSchemePathCommand extends MapActionCommandBundle {
	/**
	 * Выбранный фрагмент линии
	 */
	MeasurementPath path = null;

	public UnPlaceSchemePathCommand(MeasurementPath path) {
		super();
		this.path = path;
	}

	@Override
	public void execute() {
		assert Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "unplace measurement path " //$NON-NLS-1$
					+ this.path.getName()
					+ " (" + this.path.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		try {
			super.removeMeasurementPath(this.path);
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}
}
