/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.11 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * � ������ ������ ����������� �������� ���������� ��������������� ���� �� 
 * �������� �����. ��� ���� �������� ����� ���������, � ������ ���� ��������� 
 * ��� ������ ���������, ����������� ������ ��������������� �����. �������
 * ������� �� ������������������ ��������� ��������
 * 
 * @version $Revision: 1.11 $, $Date: 2005/05/27 15:14:55 $
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
			Environment.log(
					Environment.LOG_LEVEL_FINER, 
					"method call", 
					getClass().getName(), 
					"execute()");
			DoublePoint coordinatePoint = this.logicalNetLayer.convertScreenToMap(this.point);
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
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
						node, 
						MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
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