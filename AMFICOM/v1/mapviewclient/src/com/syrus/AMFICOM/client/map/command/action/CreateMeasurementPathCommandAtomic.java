/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.8 2005/02/01 11:34:56 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

/**
 * создание прокладки измерительного пути 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return mp;
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
			mp = com.syrus.AMFICOM.mapview.MeasurementPath.createInstance(
					path,
					logicalNetLayer.getMapView());
	
			logicalNetLayer.getMapView().addMeasurementPath(mp);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}
	}
}

