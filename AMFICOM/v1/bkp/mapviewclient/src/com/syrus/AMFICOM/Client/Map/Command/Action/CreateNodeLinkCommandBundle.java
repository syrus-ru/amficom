/**
 * $Id: CreateNodeLinkCommandBundle.java,v 1.8 2004/12/24 15:42:11 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * �������� ��������� �����, ������� �������� �� 
 * ������������������ ��������� ��������. ������������� ��������� ���������
 * �������������: ������������ �������� �� ���� ����� �������, �������
 * ������ � ������ ����� � ���������. � ������ ���������� ����������� 
 * ������ �������
 * 
 * 
 * @version $Revision: 1.8 $, $Date: 2004/12/24 15:42:11 $
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
	AbstractNode startNode;
	
	Map map;
	
	/** �����, � ������� ������ ���� �������� ���� ��������� */
	Point endPoint;

	/**
	 * 
	 * @param startNode
	 */
	public CreateNodeLinkCommandBundle(AbstractNode startNode)
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		// ������������� ������� � �����, � ������� �������� �����		
		MapElement curElementAtPoint = logicalNetLayer.getMapElementAtPoint(endPoint);

		// ���� ����� �������� �� ��� �� ��������, �� ����� �� ��������
		if(curElementAtPoint == startNode)
			return;
	
		Map map = logicalNetLayer.getMapView().getMap();
	
		AbstractNode endNode = null;
		PhysicalLink physicalLink = null;
		NodeLink nodeLink = null;

		DoublePoint mapEndPoint = logicalNetLayer.convertScreenToMap(endPoint);

		// ���� � �������� ����� ��� ���� �������, ���������, ����� ��� ����
		if ( curElementAtPoint != null
			&& curElementAtPoint instanceof AbstractNode)
		{
			endNode = (AbstractNode )curElementAtPoint;

			// �������� ������� - �������������� ����
			if(endNode instanceof TopologicalNode)
			{
				TopologicalNode mpne = (TopologicalNode )endNode;
		
				// ���� �� ��������, �� ���� ��������� � �������� ������ �����,
				// �� � ��� �� ����� ��������� �����
				if(mpne.isActive())
				{
					// node created with fake physicalLink
					// should be later updated (e.g. through call to
					// nodelink.setPhysicalLink or node.setPhysicalLink)
					endNode = super.createPhysicalNode(mpne.getPhysicalLink(), mapEndPoint);
				}
				else
				// ���� �� - �������� ��� �����, �� �������� ����� ��������
				// �� ���� (��������� ������������ ����� ��� �����������
				// ���������� �����)
				{
					super.changePhysicalNodeActivity(mpne, true);
				}
			}
		}
		else
		// ���� � �������� ����� ��� ��������, �� ������� ����� �������� 
		// �������������� ����
		{
			endNode = super.createPhysicalNode(mapEndPoint);
		}

//		nodeLink = super.createNodeLink(startNode, endNode);

		//����� � ����������� �� ���� ����� ���������� startNode � endNode
		if (startNode instanceof SiteNode 
			&& endNode instanceof SiteNode )
		{
			// ��������� ����� ���������� ����� �� ������ ���������
			
			physicalLink = super.createPhysicalLink(startNode, endNode);

			nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
			nodeLink.setPhysicalLink(physicalLink);

			physicalLink.addNodeLink( nodeLink);
		}//if ( MapSiteNodeElement && MapSiteNodeElement)
		else
		if ( startNode instanceof SiteNode
			&& endNode instanceof TopologicalNode )
		{
			TopologicalNode mpne = (TopologicalNode)endNode;

			// ���� � ��������� ���� ��� �������� ��������� (������� ������ 
			// ��� ���������), �� ���� �������� ���������� � ������������ �����,
			// �� �������� ��� �����
			if(endNode.getNodeLinks().size() == 2)
			{
				physicalLink = mpne.getPhysicalLink();

				MapElementState pls = physicalLink.getState();

				nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
				nodeLink.setPhysicalLink(physicalLink);

				physicalLink.addNodeLink(nodeLink);

				// ��������� ���������� � ��������� ����� �����
				if(physicalLink.getEndNode() == mpne)
					physicalLink.setEndNode(physicalLink.getStartNode());
				physicalLink.setStartNode(startNode);

				super.registerStateChange(physicalLink, pls, physicalLink.getState());
			}//if(endNode.getNodeLinks().size() == 2
			//  � ��������� ������ ��������� ����� ����� �� ������ ���������
			else
			{
				physicalLink = super.createPhysicalLink(startNode, endNode);

				nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
				nodeLink.setPhysicalLink(physicalLink);

				physicalLink.addNodeLink( nodeLink);
			}
		}//if ( MapSiteNodeElement && MapPhysicalNodeElement )
		else
		if ( startNode instanceof TopologicalNode
			&& endNode instanceof SiteNode )
		{
			// ������������ ���������� ����� ����������� �� ���� site

			TopologicalNode mpne = (TopologicalNode)startNode;

			physicalLink = mpne.getPhysicalLink();

			MapElementState pls = physicalLink.getState();

			nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
			nodeLink.setPhysicalLink(physicalLink);

			physicalLink.addNodeLink(nodeLink);

			super.changePhysicalNodeActivity(mpne, true);

			// ��������� ���������� � ��������� ����� �����
			if(physicalLink.getStartNode() == startNode)
				physicalLink.setStartNode(physicalLink.getEndNode());
			physicalLink.setEndNode(endNode);

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}// if ( MapPhysicalNodeElement && MapSiteNodeElement )
		else
		// ����� ����������� ��� �����, � ���� �� ����� ����������� ��������� 
		// ������, ������� ���������
		if ( startNode instanceof TopologicalNode 
			&& endNode instanceof TopologicalNode )
		{
			TopologicalNode smpne = (TopologicalNode)startNode;
			TopologicalNode empne = (TopologicalNode)endNode;

			physicalLink = smpne.getPhysicalLink();
			PhysicalLink emple = empne.getPhysicalLink();

			MapElementState pls = physicalLink.getState();

			nodeLink = super.createNodeLink(physicalLink, startNode, endNode);
			nodeLink.setPhysicalLink(physicalLink);

			physicalLink.addNodeLink(nodeLink);

			super.changePhysicalNodeActivity(smpne, true);

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

				// ��������� ������, ��������� ������������ �������� ��������
				LinkedList nodeLinksToMove = new LinkedList();
				nodeLinksToMove.addAll(emple.getNodeLinks());

				// ������� ���������� ����� �� ����� ����� � ������
				for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();)
				{
					NodeLink mnle = (NodeLink)it.next();
					emple.removeNodeLink(mnle);
					physicalLink.addNodeLink(mnle);
					mnle.setPhysicalLink(physicalLink);
				}			
			
				// ��������� ���������� � ��������� ����� �����
				if(emple.getStartNode() == endNode)
					physicalLink.setEndNode(emple.getEndNode());
				else
					physicalLink.setEndNode(emple.getStartNode());

				super.registerStateChange(emple, pls2, emple.getState());

				super.removePhysicalLink(emple);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				physicalLink, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.notifySchemeEvent(physicalLink);
	}
}
