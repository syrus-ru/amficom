/**
 * $Id: CreateNodeLinkCommandBundle.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * �������� ��������� �����, ������� �������� �� 
 * ������������������ ��������� ��������
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateNodeLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ������������ ��� �������� � ������� ��������� (��������
	 * ����� ��� ���������)
	 */
	public static final String END_POINT = "endpoint";

	/** ��������� ���� ��������� */
	MapNodeElement startNode;
	
	Map map;
	
	/** �����, � ������� ������ ���� �������� ���� ��������� */
	Point endPoint;

	/**
	 * 
	 * @param startNode
	 */
	public CreateNodeLinkCommandBundle(MapNodeElement startNode)
	{
		super();
		this.startNode = startNode;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(END_POINT))
		{
			endPoint = (Point )value;
		}
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		// ������������� ������� � �����, � ������� �������� �����		
		MapElement curElementAtPoint = logicalNetLayer.getMapElementAtPoint(endPoint);

		// ���� ����� �������� �� ��� �� ��������, �� ����� �� ��������
		if(curElementAtPoint == startNode)
			return;
	
		Map map = logicalNetLayer.getMapView().getMap();
	
		MapNodeElement endNode = null;
		MapPhysicalLinkElement physicalLink = null;
		MapNodeLinkElement nodeLink = null;

		Point2D.Double mapEndPoint = logicalNetLayer.convertScreenToMap(endPoint);

		// ���� � �������� ����� ��� ���� �������, ���������, ����� ��� ����
		if ( curElementAtPoint != null
			&& curElementAtPoint instanceof MapNodeElement)
		{
			endNode = (MapNodeElement )curElementAtPoint;

			// �������� ������� - �������������� ����
			if(endNode instanceof MapPhysicalNodeElement)
			{
				MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )endNode;
		
				// ���� �� ��������, �� ���� ��������� � �������� ������ �����,
				// �� � ��� �� ����� ��������� �����
				if(mpne.isActive())
				{
					endNode = createPhysicalNode(mapEndPoint);
				}
				else
				// ���� �� - �������� ��� �����, �� �������� ����� ��������
				// �� ���� (��������� ������������ ����� ��� �����������
				// ���������� �����)
				{
					changePhysicalNodeActivity(mpne, true);
				}
			}
		}
		else
		// ���� � �������� ����� ��� ��������, �� ������� ����� �������� 
		// �������������� ����
		{
			endNode = createPhysicalNode(mapEndPoint);
		}

		nodeLink = createNodeLink(startNode, endNode);

		//����� � ����������� �� ���� ����� ���������� startNode � endNode
		if (startNode instanceof MapSiteNodeElement 
			&& endNode instanceof MapSiteNodeElement )
		{
			// ��������� ����� ���������� ����� �� ������ ���������
			
			physicalLink = createPhysicalLink(startNode, endNode);
			physicalLink.addNodeLink( nodeLink);
			nodeLink.setPhysicalLinkId(physicalLink.getId());
		}//if (startNode instanceof MapSiteNodeElement && endNode instanceof MapSiteNodeElement)
		else
		if ( startNode instanceof MapSiteNodeElement
			&& endNode instanceof MapPhysicalNodeElement )
		{
			MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )endNode;

			// ���� � ��������� ���� ��� �������� ��������� (������� ������ 
			// ��� ���������), �� ���� �������� ���������� � ������������ �����,
			// �� �������� ��� �����
			if(endNode.getNodeLinks().size() == 2)
			{
				physicalLink = map.getPhysicalLink(mpne.getPhysicalLinkId());

				MapElementState pls = physicalLink.getState();

				physicalLink.addNodeLink(nodeLink);
				nodeLink.setPhysicalLinkId(physicalLink.getId());

				// ��������� ���������� � ��������� ����� �����
				if(physicalLink.getEndNode() == startNode)
					physicalLink.setEndNode(physicalLink.getStartNode());
				physicalLink.setStartNode(startNode);

				registerStateChange(physicalLink, pls, physicalLink.getState());
			}//if(endNode.getNodeLinks().size() == 2
			//  � ��������� ������ ��������� ����� ����� �� ������ ���������
			else
			{
				physicalLink = createPhysicalLink(startNode, endNode);
				physicalLink.addNodeLink( nodeLink);
				nodeLink.setPhysicalLinkId(physicalLink.getId());
			}
		}//if ( startNode instanceof MapSiteNodeElement && endNode instanceof MapPhysicalNodeElement )
		else
		if ( startNode instanceof MapPhysicalNodeElement
			&& endNode instanceof MapSiteNodeElement )
		{
			// ������������ ���������� ����� ����������� �� ���� site

			MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )startNode;

			physicalLink = map.getPhysicalLink(mpne.getPhysicalLinkId());

			MapElementState pls = physicalLink.getState();

			physicalLink.addNodeLink(nodeLink);
			nodeLink.setPhysicalLinkId(physicalLink.getId());

			changePhysicalNodeActivity(mpne, true);

			// ��������� ���������� � ��������� ����� �����
			if(physicalLink.getStartNode() == startNode)
				physicalLink.setStartNode(physicalLink.getEndNode());
			physicalLink.setEndNode(endNode);

			registerStateChange(physicalLink, pls, physicalLink.getState());
		}
		else
		// ����� ����������� ��� �����, � ���� �� ����� ����������� ��������� 
		// ������, ������� ���������
		if ( startNode instanceof MapPhysicalNodeElement 
			&& endNode instanceof MapPhysicalNodeElement )
		{
			MapPhysicalNodeElement smpne = (MapPhysicalNodeElement )startNode;
			MapPhysicalNodeElement empne = (MapPhysicalNodeElement )endNode;

			physicalLink = map.getPhysicalLink(smpne.getPhysicalLinkId());
			MapPhysicalLinkElement emple = map.getPhysicalLink(empne.getPhysicalLinkId());

			MapElementState pls = physicalLink.getState();

			physicalLink.addNodeLink(nodeLink);
			nodeLink.setPhysicalLinkId(physicalLink.getId());

			changePhysicalNodeActivity(smpne, true);

			// ��������� ���������� � ��������� ����� �����
			if(physicalLink.getStartNode() == startNode)
				physicalLink.setStartNode(physicalLink.getEndNode());
			physicalLink.setEndNode(endNode);

			// ������������ ��������� �� ����� ����� � ������ � �������
			// �������� �����
			if(emple != null)
				if(emple != physicalLink)
			{
				MapElementState pls2 = emple.getState();

				LinkedList nodeLinksToMove = new LinkedList();
				// ������� ���������� ����� �� ����� ����� � ������
				for(Iterator it = emple.getNodeLinks().iterator(); it.hasNext();)
				{
					nodeLinksToMove.add(it.next());
				}
				for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
					emple.removeNodeLink(mnle);
					physicalLink.addNodeLink(mnle);
					mnle.setPhysicalLinkId(physicalLink.getId());
				}			
			
				// ��������� ���������� � ��������� ����� �����
				if(emple.getStartNode() == endNode)
					physicalLink.setEndNode(emple.getEndNode());
				else
					physicalLink.setEndNode(emple.getStartNode());

				registerStateChange(emple, pls2, emple.getState());

				removePhysicalLink(emple);
			}

			registerStateChange(physicalLink, pls, physicalLink.getState());
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				physicalLink, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.notifySchemeEvent(physicalLink);
		logicalNetLayer.notifyCatalogueEvent(physicalLink);
	}
}
