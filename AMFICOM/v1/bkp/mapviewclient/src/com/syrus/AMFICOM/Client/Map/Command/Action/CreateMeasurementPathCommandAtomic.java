/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.10 2005/02/18 12:19:44 krupenn Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

/**
 * создание прокладки измерительного пути 
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/02/18 12:19:44 $
 * @module mapviewclient_v1
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	/** создаваемый измерительный путь */
	MeasurementPath mp;
	
	/** схемный путь */
	SchemePath path;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath path)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.path = path;
	}
	
	public MeasurementPath getPath()
	{
		return this.mp;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		try
		{
			this.mp = MeasurementPath.createInstance(
					this.path,
					this.logicalNetLayer.getMapView());
	
			this.logicalNetLayer.getMapView().addMeasurementPath(this.mp);
			setResult(Command.RESULT_OK);
		}
		catch (ApplicationException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}

