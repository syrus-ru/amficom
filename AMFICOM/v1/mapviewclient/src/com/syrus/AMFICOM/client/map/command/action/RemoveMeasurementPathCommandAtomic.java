/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.9 2005/02/01 11:34:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление измерительного пути из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand
{
	MeasurementPath mp;
	
	public RemoveMeasurementPathCommandAtomic(MeasurementPath mp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.mp = mp;
	}
	
	public MeasurementPath getPath()
	{
		return mp;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().removeMeasurementPath(mp);
	}

	public void redo()
	{
		logicalNetLayer.getMapView().removeMeasurementPath(mp);
	}

	public void undo()
	{
		logicalNetLayer.getMapView().addMeasurementPath(mp);
	}
}

