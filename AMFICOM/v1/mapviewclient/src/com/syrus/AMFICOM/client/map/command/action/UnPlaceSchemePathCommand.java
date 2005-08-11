/**
 * $Id: UnPlaceSchemePathCommand.java,v 1.16 2005/08/11 12:43:30 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class UnPlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	MeasurementPath path = null;

	public UnPlaceSchemePathCommand(MeasurementPath path)
	{
		super();
		this.path = path;
	}

	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try {
			super.removeMeasurementPath(this.path);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
