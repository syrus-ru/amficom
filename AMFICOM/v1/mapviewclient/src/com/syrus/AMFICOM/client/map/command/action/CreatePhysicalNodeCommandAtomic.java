/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.13 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * создание топологического узла, внесение его в пул и на карту - 
 * атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand
{
	/** создаваемый топологический узел */
	TopologicalNode node;
	
	/** географические координаты узла */
	DoublePoint point;
	
	PhysicalLink physicalLink;

	public CreatePhysicalNodeCommandAtomic(PhysicalLink physicalLink, DoublePoint point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
		this.physicalLink = physicalLink;
	}
	
	/**
	 * @deprecated
	 */	
	public CreatePhysicalNodeCommandAtomic(DoublePoint point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
		this.physicalLink = null;
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

		try
		{
			this.node = TopologicalNode.createInstance(
				this.logicalNetLayer.getUserId(),
				this.physicalLink,
				this.point);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		TopologicalNodeController tnc = (TopologicalNodeController)getLogicalNetLayer().getMapViewController().getController(this.node);

		// по умолчанию топологиеский узел не активен
		tnc.setActive(this.node, false);
		// установить коэффициент для масштабирования изображения
		// в соответствии с текущим масштабом отображения карты
		tnc.updateScaleCoefficient(this.node);

		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}
	
	public void redo() 
	{
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}
}
