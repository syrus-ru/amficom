/**
 * $Id: MoveFixedDistanceCommand.java,v 1.6 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * Команда позволяет перемещать топологический узел вокруг другого
 * топологического узла, связанного с ним фрагментом линии, при сохранении
 * длины фрагмента
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class MoveFixedDistanceCommand extends MoveSelectionCommandBundle
{
	AbstractNode fixedNode;
	AbstractNode movedNode;
	DoublePoint fixedPoint;
	DoublePoint projectedPoint;
	double fixedDistance;
	
	Point fixedScreenPoint;
	double fixedScreenDistance;

	public MoveFixedDistanceCommand(Point point, AbstractNode fixedNode, AbstractNode movedNode)
	{
		super(point);
		this.fixedNode = fixedNode;
		this.movedNode = movedNode;

		this.fixedPoint = fixedNode.getLocation();

		double fixedX = fixedNode.getLocation().getX();
		double fixedY = fixedNode.getLocation().getY();
		double movedX = movedNode.getLocation().getX();
		double movedY = movedNode.getLocation().getY();

		this.fixedDistance = Math.sqrt((movedX - fixedX) * (movedX - fixedX) 
				+ (movedY - fixedY) * (movedY - fixedY));
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		super.setLogicalNetLayer(logicalNetLayer);

		this.fixedScreenPoint = logicalNetLayer.convertMapToScreen(this.fixedNode.getLocation());
		Point movedScreenPoint = logicalNetLayer.convertMapToScreen(this.movedNode.getLocation());
		
		this.fixedScreenDistance = Math.sqrt(
				(movedScreenPoint.x - this.fixedScreenPoint.x) 
					* (movedScreenPoint.x - this.fixedScreenPoint.x) 
				+ (movedScreenPoint.y - this.fixedScreenPoint.y) 
					* (movedScreenPoint.y - this.fixedScreenPoint.y));
	}

	public MoveFixedDistanceCommand(LogicalNetLayer logicalNetLayer)
	{
		super(logicalNetLayer);
	}

	/**
	 * создать отдельные команды на перемещение для всех выделенных
	 * точечных объектов
	 */
	protected void setElements()
	{
		super.add(new MoveNodeCommand(this.movedNode));
	}
	
	/**
	 * обновить абсолютное смещение по начальной и конечной точкам сдвига
	 */
	protected void setShift()
	{
		double dist1 = Math.sqrt( 
			(super.endPoint.x - this.fixedScreenPoint.x) 
				* (super.endPoint.x - this.fixedScreenPoint.x) 
			+ (super.endPoint.y - this.fixedScreenPoint.y) 
				* (super.endPoint.y - this.fixedScreenPoint.y) );

		double sinB1 = (super.endPoint.y - this.fixedScreenPoint.y) / dist1;

		double cosB1 = (super.endPoint.x - this.fixedScreenPoint.x) / dist1;

		Point targetScreenPoint = new Point(
			(int )(this.fixedScreenPoint.x + cosB1 * this.fixedScreenDistance),
			(int )(this.fixedScreenPoint.y + sinB1 * this.fixedScreenDistance));

		DoublePoint targetMapPoint = this.logicalNetLayer.convertScreenToMap(targetScreenPoint);
		DoublePoint startMapPoint = this.logicalNetLayer.convertScreenToMap(super.startPoint);

		super.deltaX = targetMapPoint.getX() - startMapPoint.getX();
		super.deltaY = targetMapPoint.getY() - startMapPoint.getY();
	}
	
}
