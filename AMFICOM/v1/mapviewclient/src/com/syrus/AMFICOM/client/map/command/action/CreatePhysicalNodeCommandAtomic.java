/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.3 2004/10/06 09:27:27 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

import java.awt.geom.Point2D;

/**
 * создание топологического узла, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/06 09:27:27 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand
{
	MapPhysicalNodeElement node;
	Point2D.Double point;
	
	public CreatePhysicalNodeCommandAtomic(Point2D.Double point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
	}
	
	public MapPhysicalNodeElement getNode()
	{
		return this.node;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		node = new MapPhysicalNodeElement(
				dataSource.GetUId( MapPhysicalNodeElement.typ),
				null,
				point,
				logicalNetLayer.getMapView().getMap(),
				MapNodeElement.DEFAULT_BOUNDS);

		Pool.put(MapPhysicalNodeElement.typ, node.getId(), node);

		// установить коэффициент для масштабирования изображения
		// в соответствии с текущим масштабом отображения карты
		node.setScaleCoefficient(
				logicalNetLayer.getDefaultScale() / logicalNetLayer.getScale());

		// по умолчанию топологиеский узел не активен
		node.setActive(false);
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
		Pool.put(MapPhysicalNodeElement.typ, node.getId(), node);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
		Pool.remove(MapPhysicalNodeElement.typ, node.getId());
	}
}
