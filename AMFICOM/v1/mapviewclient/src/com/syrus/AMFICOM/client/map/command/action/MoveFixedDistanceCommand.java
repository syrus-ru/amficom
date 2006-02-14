/*-
 * $$Id: MoveFixedDistanceCommand.java,v 1.21 2006/02/14 10:20:06 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ������� ��������� ���������� �������������� ���� ������ �������
 * ��������������� ����, ���������� � ��� ���������� �����, ��� ����������
 * ����� ���������
 * 
 * @version $Revision: 1.21 $, $Date: 2006/02/14 10:20:06 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MoveFixedDistanceCommand extends MoveSelectionCommandBundle {
	AbstractNode fixedNode;
	AbstractNode movedNode;
	DoublePoint fixedPoint;
	DoublePoint projectedPoint;
	double fixedDistance;
	
	Point fixedScreenPoint;
	double fixedScreenDistance;

	public MoveFixedDistanceCommand(Point point, AbstractNode fixedNode, AbstractNode movedNode) {
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

	@Override
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		super.setNetMapViewer(netMapViewer);

		try {
			this.fixedScreenPoint = this.logicalNetLayer.getConverter().convertMapToScreen(this.fixedNode.getLocation());
			Point movedScreenPoint = this.logicalNetLayer.getConverter().convertMapToScreen(this.movedNode.getLocation());
			this.fixedScreenDistance = Math.sqrt(
					(movedScreenPoint.x - this.fixedScreenPoint.x) 
						* (movedScreenPoint.x - this.fixedScreenPoint.x) 
					+ (movedScreenPoint.y - this.fixedScreenPoint.y) 
						* (movedScreenPoint.y - this.fixedScreenPoint.y));
		} catch(MapException e) {
			// TODO Auto-generated catch block
			Log.errorMessage(e);
		}
	}

	public MoveFixedDistanceCommand(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	/**
	 * ������� ��������� ������� �� ����������� ��� ���� ���������� ��������
	 * ��������
	 */
	@Override
	protected void setElements() {
		super.add(new MoveNodeCommand(this.movedNode));
	}

	/**
	 * �������� ���������� �������� �� ��������� � �������� ������ ������
	 */
	@Override
	protected void setShift() {
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
			DoublePoint targetMapPoint = this.logicalNetLayer.getConverter().convertScreenToMap(targetScreenPoint);
			DoublePoint startMapPoint = this.logicalNetLayer.getConverter().convertScreenToMap(super.startPoint);
			super.deltaX = targetMapPoint.getX() - startMapPoint.getX();
			super.deltaY = targetMapPoint.getY() - startMapPoint.getY();
		} catch(MapException e) {
			// TODO Auto-generated catch block
			Log.errorMessage(e);
		}
	}
	
}
