/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.15 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.util.Log;

/**
 * � ������ ������ ����������� �������� ���������� ��������������� ���� �� 
 * �������� �����. ��� ���� �������� ����� ���������, � ������ ���� ��������� 
 * ��� ������ ���������, ����������� ������ ��������������� �����. �������
 * ������� �� ������������������ ��������� ��������
 * 
 * @version $Revision: 1.15 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class CreatePhysicalNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� �������� �����
	 */
	NodeLink nodeLink;
	
	Map map;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public CreatePhysicalNodeCommandBundle(
			NodeLink nodeLink,
			Point point)
	{
		super();
		this.nodeLink = nodeLink;
		this.point = point;
	}

	public void execute()
	{
		try
		{
			Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);
			DoublePoint coordinatePoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.point);
			this.map = this.logicalNetLayer.getMapView().getMap();
			// �������� ����� �����, ������� ����������� ��������
			PhysicalLink physicalLink = this.nodeLink.getPhysicalLink();
			// ������� ����� �������� �������������� ����
			TopologicalNode node = super.createPhysicalNode(physicalLink, coordinatePoint);
			super.changePhysicalNodeActivity(node, true);
			// ����� ��������� � �������� ���� ���������
			AbstractNode startNode = this.nodeLink.getStartNode();
			AbstractNode endNode = this.nodeLink.getEndNode();
			// ������� �������� �� ��� ����� - �.�. ������� ��� ����� ���������
			NodeLink link1 = super.createNodeLink(physicalLink, startNode, node);
			link1.setPhysicalLink(physicalLink);
			NodeLink link2 = super.createNodeLink(physicalLink, node, endNode);
			link2.setPhysicalLink(physicalLink);
			// ��������� ������ �������� � �����
			super.removeNodeLink(this.nodeLink);
			MapElementState pls = physicalLink.getState();
			// ��������� ������ �������� �� �����
			physicalLink.removeNodeLink(this.nodeLink);
			// ����������� ��� ����� ���������
			physicalLink.addNodeLink(link1);
			physicalLink.addNodeLink(link2);
			super.registerStateChange(physicalLink, pls, physicalLink.getState());
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
			this.logicalNetLayer.setCurrentMapElement(node);
			this.logicalNetLayer.notifySchemeEvent(node);
			setResult(Command.RESULT_OK);
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}

	}
}
