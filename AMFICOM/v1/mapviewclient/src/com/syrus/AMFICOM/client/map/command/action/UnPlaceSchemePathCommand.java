/**
 * $Id: UnPlaceSchemePathCommand.java,v 1.3 2004/10/19 10:07:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;

/**
 * убрать привязку измерительного пути с карты
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:07:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class UnPlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * Выбранный фрагмент линии
	 */
	MapMeasurementPathElement path = null;

	public UnPlaceSchemePathCommand(MapMeasurementPathElement path)
	{
		super();
		this.path = path;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		super.removeMeasurementPath(path);

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
