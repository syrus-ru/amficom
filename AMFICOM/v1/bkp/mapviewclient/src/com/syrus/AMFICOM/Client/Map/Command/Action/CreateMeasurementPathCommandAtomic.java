/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.2 2004/10/06 09:27:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

/**
 * создание физической линии, внесение ее в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/06 09:27:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	MapMeasurementPathElement mp;
	
	SchemePath path;
	
	MapNodeElement startNode;
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
	
	public void redo()
	{
		logicalNetLayer.getMapView().addMeasurementPath(mp);
		Pool.put(MapMeasurementPathElement.typ, mp.getId(), mp);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().removeMeasurementPath(mp);
		Pool.remove(MapMeasurementPathElement.typ, mp.getId());
	}
}

