/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление физической линии из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand
{
	MapMeasurementPathElement mp;
	
	public RemoveMeasurementPathCommandAtomic(MapMeasurementPathElement mp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.mp = mp;
	}
	
	public MapMeasurementPathElement getPath()
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
		Pool.remove(MapMeasurementPathElement.typ, mp.getId());
	}

	public void redo()
	{
		logicalNetLayer.getMapView().removeMeasurementPath(mp);
		Pool.remove(MapMeasurementPathElement.typ, mp.getId());
	}

	public void undo()
	{
		logicalNetLayer.getMapView().addMeasurementPath(mp);
		Pool.put(MapMeasurementPathElement.typ, mp.getId(), mp);
	}
}

