/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.17 2005/08/17 14:14:17 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * удаление измерительного пути из карты - атомарное действие 
 * @author $Author: arseniy $
 * @version $Revision: 1.17 $, $Date: 2005/08/17 14:14:17 $
 * @module mapviewclient
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
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.logicalNetLayer.getMapView().removeMeasurementPath(this.measuremetnPath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo()
	{
		this.logicalNetLayer.getMapView().removeMeasurementPath(this.measuremetnPath);
	}

	@Override
	public void undo()
	{
		this.logicalNetLayer.getMapView().addMeasurementPath(this.measuremetnPath);
	}
}

