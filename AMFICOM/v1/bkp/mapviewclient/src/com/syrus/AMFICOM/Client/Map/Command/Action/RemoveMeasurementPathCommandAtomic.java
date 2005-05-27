/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.12 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * удаление измерительного пути из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand
{
	MeasurementPath measuremetnPath;
	
	public RemoveMeasurementPathCommandAtomic(MeasurementPath mp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.measuremetnPath = mp;
	}
	
	public MeasurementPath getPath()
	{
		return this.measuremetnPath;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		this.logicalNetLayer.getMapView().removeMeasurementPath(this.measuremetnPath);
		setResult(Command.RESULT_OK);
	}

	public void redo()
	{
		this.logicalNetLayer.getMapView().removeMeasurementPath(this.measuremetnPath);
	}

	public void undo()
	{
		this.logicalNetLayer.getMapView().addMeasurementPath(this.measuremetnPath);
	}
}

