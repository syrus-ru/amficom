/**
 * $Id: UnPlaceSchemePathCommand.java,v 1.19 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * убрать привязку измерительного пути с карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/08/26 15:39:54 $
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
		Log.debugMessage(
				getClass().getName() + "::execute() | "
					+ "unplace measurement path "
					+ this.path.getName()
					+ " (" + this.path.getId() + ")", 
				Level.FINEST);

		try {
			super.removeMeasurementPath(this.path);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
