/*-
 * $$Id: CreatePhysicalNodeCommandBundle.java,v 1.30 2005/10/18 07:21:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * � ������ ������ ����������� �������� ���������� ��������������� ���� ��
 * �������� �����. ��� ���� �������� ����� ���������, � ������ ���� ���������
 * ��� ������ ���������, ����������� ������ ��������������� �����. �������
 * ������� �� ������������������ ��������� ��������
 * 
 * @version $Revision: 1.30 $, $Date: 2005/10/18 07:21:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreatePhysicalNodeCommandBundle extends MapActionCommandBundle {
	/**
	 * ��������� �������� �����
	 */
	NodeLink nodeLink;

	Map map;

	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public CreatePhysicalNodeCommandBundle(NodeLink nodeLink, Point point) {
		super();
		this.nodeLink = nodeLink;
		this.point = point;
	}

	@Override
	public void execute() {
		try {
			Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "create topological node on node link " //$NON-NLS-1$
					+ this.nodeLink.getName() 
					+ " (" + this.nodeLink.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);
			DoublePoint coordinatePoint = this.logicalNetLayer.getConverter()
					.convertScreenToMap(this.point);
			this.map = this.logicalNetLayer.getMapView().getMap();
			// �������� ����� �����, ������� ����������� ��������
			PhysicalLink physicalLink = this.nodeLink.getPhysicalLink();
			// ������� ����� �������� �������������� ����
			TopologicalNode node = super.createPhysicalNode(
					physicalLink,
					coordinatePoint);
			super.changePhysicalNodeActivity(node, true);
			// ����� ��������� � �������� ���� ���������
			AbstractNode startNode = this.nodeLink.getStartNode();
			AbstractNode endNode = this.nodeLink.getEndNode();

			MapElementState pls = physicalLink.getState();
			// ������� �������� �� ��� ����� - �.�. ������� ��� ����� ���������
			NodeLink link1 = super.createNodeLink(physicalLink, startNode, node);
			NodeLink link2 = super.createNodeLink(physicalLink, node, endNode);
			// ��������� ������ �������� � �����
			super.removeNodeLink(this.nodeLink);
			// ��������� ������ �������� �� �����
			physicalLink.removeNodeLink(this.nodeLink);
			super.registerStateChange(physicalLink, pls, physicalLink.getState());

			this.logicalNetLayer.setCurrentMapElement(node);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			Log.debugException(e, Level.SEVERE);
		}

	}
}
