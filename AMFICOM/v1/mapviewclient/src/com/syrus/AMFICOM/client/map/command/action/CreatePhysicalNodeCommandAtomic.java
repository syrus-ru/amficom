/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.8 2004/12/23 16:57:59 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.Client.Resource.Map.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.geom.Point2D;
import com.syrus.AMFICOM.map.Map;

/**
 * создание топологического узла, внесение его в пул и на карту - 
 * атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand
{
	/** создаваемый топологический узел */
	TopologicalNode node;
	
	/** географические координаты узла */
	DoublePoint point;
	
	public CreatePhysicalNodeCommandAtomic(DoublePoint point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
	}
	
	public TopologicalNode getNode()
	{
		return this.node;
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
			node = TopologicalNode.createInstance(
				new Identifier(aContext.getSessionInterface().getAccessIdentifier().user_id),
				point);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		TopologicalNodeController tnc = (TopologicalNodeController )getLogicalNetLayer().getMapViewController().getController(node);

		// установить коэффициент для масштабирования изображения
		// в соответствии с текущим масштабом отображения карты
		tnc.updateScaleCoefficient(node);

		// по умолчанию топологиеский узел не активен
		node.setActive(false);
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
	}
}
