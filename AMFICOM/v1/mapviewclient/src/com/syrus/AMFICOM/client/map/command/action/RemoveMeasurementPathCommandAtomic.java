/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.8 2005/01/31 12:19:18 krupenn Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/01/31 12:19:18 $
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

		logicalNetLayer.getMapViewController().removeMeasurementPath(mp);
	}

	public void redo()
	{
		logicalNetLayer.getMapViewController().removeMeasurementPath(mp);
	}

	public void undo()
	{
		logicalNetLayer.getMapViewController().addMeasurementPath(mp);
	}
}

