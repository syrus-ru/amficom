/**
 * $Id: MoveFixedDistanceCommand.java,v 1.4 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.AbstractNode;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Команда позволяет перемещать топологический узел вокруг другого
 * топологического узла, связанного с ним фрагментом линии, при сохранении
 * длины фрагмента
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveFixedDistanceCommand extends MoveSelectionCommandBundle
{
	AbstractNode fixedNode;
	AbstractNode movedNode;
	DoublePoint fixedPoint;
	DoublePoint projectedPoint;
	double fixedDistance;
	
	Point fp;
	double fd;

	public MoveFixedDistanceCommand(Point point, AbstractNode fixedNode, AbstractNode movedNode)
	{
		super(point);
		this.fixedNode = fixedNode;
		this.movedNode = movedNode;

		fixedPoint = fixedNode.getLocation();

		double fx = fixedNode.getLocation().x;
		double fy = fixedNode.getLocation().y;
		double mx = movedNode.getLocation().x;
		double my = movedNode.getLocation().y;

		fixedDistance = Math.sqrt((mx - fx) * (mx - fx) + (my - fy) * (my - fy));
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		super.setLogicalNetLayer(logicalNetLayer);

		fp = logicalNetLayer.convertMapToScreen(fixedNode.getLocation());
		Point ep = logicalNetLayer.convertMapToScreen(movedNode.getLocation());
		
		fd = Math.sqrt((ep.x - fp.x) * (ep.x - fp.x) + (ep.y - fp.y) * (ep.y - fp.y));
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
		super.add(new MoveNodeCommand(movedNode));
	}
	
	/**
	 * обновить абсолютное смещение по начальной и конечной точкам сдвига
	 */
	protected void setShift()
	{
		double dist1 = Math.sqrt( 
			(endPoint.x - fp.x) * (endPoint.x - fp.x) +
			(endPoint.y - fp.y) * (endPoint.y - fp.y) );

		double sinB1 = (endPoint.y - fp.y) / dist1;

		double cosB1 = (endPoint.x - fp.x) / dist1;

		Point targetp1 = new Point(
			(int )(fp.x + cosB1 * fd),
			(int )(fp.y + sinB1 * fd));

		DoublePoint tp = logicalNetLayer.convertScreenToMap(targetp1);
		DoublePoint sp = logicalNetLayer.convertScreenToMap(startPoint);

		super.deltaX = tp.getX() - sp.getX();
		super.deltaY = tp.getY() - sp.getY();
/*
		Point2D.Double sp = logicalNetLayer.convertScreenToMap(startPoint);
		Point2D.Double ep = logicalNetLayer.convertScreenToMap(endPoint);

		double dist = Math.sqrt( 
			(ep.x - fixedPoint.x) * (ep.x - fixedPoint.x) +
			(ep.y - fixedPoint.y) * (ep.y - fixedPoint.y) );

		double sinB = (ep.y - fixedPoint.y) / dist;

		double cosB = (ep.x - fixedPoint.x) / dist;
		
		Point2D.Double targetp = new Point2D.Double(
			fixedPoint.x + cosB * fixedDistance,
			fixedPoint.y + sinB * fixedDistance);

		super.deltaX = targetp.getX() - sp.getX();
		super.deltaY = targetp.getY() - sp.getY();
*/
	}
	
}
