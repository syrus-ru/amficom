/**
 * $Id: MoveFixedDistanceCommand.java,v 1.7 2005/02/18 12:19:44 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * ������� ��������� ���������� �������������� ���� ������ �������
 * ��������������� ����, ���������� � ��� ���������� �����, ��� ����������
 * ����� ���������
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/18 12:19:44 $
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

		try {
			this.fixedScreenPoint = logicalNetLayer.convertMapToScreen(this.fixedNode.getLocation());
			Point movedScreenPoint = logicalNetLayer.convertMapToScreen(this.movedNode.getLocation());
			this.fixedScreenDistance = Math.sqrt(
					(movedScreenPoint.x - this.fixedScreenPoint.x) 
						* (movedScreenPoint.x - this.fixedScreenPoint.x) 
					+ (movedScreenPoint.y - this.fixedScreenPoint.y) 
						* (movedScreenPoint.y - this.fixedScreenPoint.y));
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MoveFixedDistanceCommand(LogicalNetLayer logicalNetLayer)
	{
		super(logicalNetLayer);
	}

	/**
	 * ������� ��������� ������� �� ����������� ��� ���� ����������
	 * �������� ��������
	 */
	protected void setElements()
	{
		super.add(new MoveNodeCommand(this.movedNode));
	}
	
	/**
	 * �������� ���������� �������� �� ��������� � �������� ������ ������
	 */
	protected void setShift()
	{
		try {
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
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
