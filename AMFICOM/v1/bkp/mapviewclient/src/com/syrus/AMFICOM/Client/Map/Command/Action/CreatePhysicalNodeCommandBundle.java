/**
 * $Id: CreatePhysicalNodeCommandBundle.java,v 1.3 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * � ������ ������ ����������� �������� ���������� ��������������� ���� �� 
 * �������� �����. ��� ���� �������� ����� ���������, � ������ ���� ��������� 
 * ��� ������ ���������, ����������� ������ ��������������� �����. �������
 * ������� �� ������������������ ��������� ��������
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 15:33:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� �������� �����
	 */
	MapNodeLinkElement nodeLink;
	
	Map map;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public CreatePhysicalNodeCommandBundle(
			MapNodeLinkElement nodeLink,
			Point point)
	{
		super();
		this.nodeLink = nodeLink;
		this.point = point;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		Point2D.Double coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();

		// �������� ����� �����, ������� ����������� ��������
		MapPhysicalLinkElement physicalLink = 
			map.getPhysicalLink( nodeLink.getPhysicalLinkId());
	
		// ������� ����� �������� �������������� ����
		MapPhysicalNodeElement node = createPhysicalNode(coordinatePoint);
		changePhysicalNodeActivity(node, true);

		// ����� ��������� � �������� ���� ���������
		MapNodeElement startNode = nodeLink.getStartNode();
		MapNodeElement endNode = nodeLink.getEndNode();

		// ������� �������� �� ��� ����� - �.�. ������� ��� ����� ���������
		MapNodeLinkElement link1 = createNodeLink(startNode, node);
		link1.setPhysicalLinkId(physicalLink.getId());
		MapNodeLinkElement link2 = createNodeLink(node, endNode);
		link2.setPhysicalLinkId(physicalLink.getId());

		// ��������� ������ �������� � �����
		removeNodeLink(nodeLink);	
		
		MapElementState pls = physicalLink.getState();

		// ��������� ������ �������� �� �����
		physicalLink.removeNodeLink(nodeLink);
		// ����������� ��� ����� ���������
		physicalLink.addNodeLink(link1);
		physicalLink.addNodeLink(link2);
		
		registerStateChange(physicalLink, pls, physicalLink.getState());

		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					node, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(node);
		logicalNetLayer.notifySchemeEvent(node);
		logicalNetLayer.notifyCatalogueEvent(node);

	}
}
