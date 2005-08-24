/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.26 2005/08/24 08:19:58 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
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
 * @version $Revision: 1.26 $, $Date: 2005/08/24 08:19:58 $
 * @module mapviewclient
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

	@Override
	public void execute()
	{
		try
		{
			Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
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
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}

	}
}
