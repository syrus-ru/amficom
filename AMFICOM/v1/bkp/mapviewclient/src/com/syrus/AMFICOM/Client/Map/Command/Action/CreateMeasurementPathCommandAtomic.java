/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.12 2005/04/26 16:16:16 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * создание прокладки измерительного пути 
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/04/26 16:16:16 $
 * @module mapviewclient_v1
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	/** создаваемый измерительный путь */
	MeasurementPath measurementPath;
	
	/** схемный путь */
	SchemePath schemePath;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath schemePath)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemePath = schemePath;
	}
	
	public MeasurementPath getPath()
	{
		return this.measurementPath;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		this.measurementPath = MeasurementPath.createInstance(
				this.schemePath,
				this.logicalNetLayer.getMapView());

		this.logicalNetLayer.getMapView().addMeasurementPath(this.measurementPath);
		setResult(Command.RESULT_OK);
	}
}

