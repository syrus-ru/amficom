/**
 * $Id: UnPlaceSchemePathCommand.java,v 1.12 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * убрать привязку измерительного пути с карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		try {
			super.removeMeasurementPath(this.path);
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
