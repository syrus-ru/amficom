/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.4 2004/12/22 16:38:39 krupenn Exp $
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
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

/**
 * создание прокладки измерительного пути 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	/** создаваемый измерительный путь */
	MapMeasurementPathElement mp;
	
	/** схемный путь */
	SchemePath path;
	
	/** начальный узел */
	AbstractNode startNode;
	
	/** конечный узел */
	AbstractNode endNode;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath path,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.path = path;
		this.startNode = startNode;
		this.endNode = endNode;
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
		DataSourceInterface dataSource = aContext.getDataSource();
		
		try
		{
			mp = new MapMeasurementPathElement(
					path,
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE),
					startNode, 
					endNode, 
					logicalNetLayer.getMapView());
	
			logicalNetLayer.getMapView().addMeasurementPath(mp);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
		}
	}
}

