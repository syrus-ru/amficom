/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

/**
 * создание прокладки измерительного пути 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
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
	MapNodeElement startNode;
	
	/** конечный узел */
	MapNodeElement endNode;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath path,
			MapNodeElement startNode,
			MapNodeElement endNode)
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
		
		mp = new MapMeasurementPathElement(
				path,
				dataSource.GetUId( MapCablePathElement.typ ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapView());
		Pool.put(MapMeasurementPathElement.typ, mp.getId(), mp);

		logicalNetLayer.getMapView().addMeasurementPath(mp);
	}
}

