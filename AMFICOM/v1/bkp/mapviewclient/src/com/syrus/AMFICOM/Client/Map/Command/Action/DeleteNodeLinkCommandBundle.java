/**
 * $Id: DeleteNodeLinkCommandBundle.java,v 1.7 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;

/**
 * � ������ ������ ����������� �������� �������� NodeLink. � �����������
 * �� ����, ����� �������� ����� �� ������ ���������� �������� �������� 
 * ���������� �����, �����, �����  (� �����). �������
 * ������� �� ������������������ ��������� ��������
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteNodeLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ��������
	 */
	NodeLink nodeLink;
	
	/**
	 * �����
	 */
	Map map;

	public DeleteNodeLinkCommandBundle(NodeLink nodeLink)
	{
		super();
		this.nodeLink = nodeLink;
	}

	/**
	 * ������� �������� ����� ����� �������� ������. ��������� � ����
	 * ������ ������� ���������� ���������� �����, ������� �� ����
	 */
	protected void removeNodeLinkBetweenTwoSites(
			NodeLink nodeLink, 
			PhysicalLink physicalLink)
	{
		super.removeNodeLink(nodeLink);
		super.removePhysicalLink(physicalLink);
	}

	/**
	 * ������� �������� ����� ����� ����������� �������������� ������. 
	 * ���������� � ���� ������ ������� ���������� ���������� �����, ������� 
	 * ��, � �������������� ���� ����
	 */
	protected void removeNodeLinkBetweenInactiveNodes(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			TopologicalNode leftNode,
			TopologicalNode rightNode)
	{
		super.removeNode(leftNode);
		super.removeNode(rightNode);
		super.removeNodeLink(nodeLink);
		super.removePhysicalLink(physicalLink);
	}

	/**
	 * ������� �������� ����� ����� ����������� �������������� ������.
	 * � ���� ������ ���������� ����� ������� �� ��� ���������� ����� 
	 */
	protected void removeNodeLinkBetweenActiveNodes(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			TopologicalNode leftNode,
			TopologicalNode rightNode)
	{
		// ������� �������� �����
		super.removeNodeLink(nodeLink);

		// ��� ���� ��� �������� �������������� ���� ���������� �����������
		leftNode.setActive(false);
		rightNode.setActive(false);

		MapElementState pls = physicalLink.getState();

		// ������ �������� �� ���������� �����
		physicalLink.removeNodeLink(nodeLink);

		// ���� ����������� ��������� �����, ���������� ������ ��������������
		// ����, �� ������ �������� �������� ����
		if(physicalLink.getStartNode() == physicalLink.getEndNode()
			&& physicalLink.getStartNode() instanceof TopologicalNode)
		{
			physicalLink.setStartNode(leftNode);
			physicalLink.setEndNode(rightNode);
		}
		else
		// � ��������� ������ ��������� ����� �� ���
		{
			// ������� ������ ���������� �����
			PhysicalLink newPhysicalLink = super.createPhysicalLink(
					physicalLink.getStartNode(),
					null);

			// ��������� ��������� � ����� ����� ���� �� ��������� ��
			// ���� �� �������� ����� ���������� ���������
			AbstractNode foundNode = super.moveNodeLinks(
					physicalLink,
					newPhysicalLink,
					false,
					leftNode,
					rightNode);

			// ���� ���������� �� ������ ����� �����, �� ��������
			// �������� ���� � ���������
			if(foundNode.equals(leftNode))
			{
				physicalLink.setStartNode(rightNode);
			}
			else
			if(foundNode.equals(rightNode))
			{
				physicalLink.setStartNode(leftNode);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());
		}
	}

	/**
	 * ������� �������� �������� �����
	 */
	protected void removeNodeLinkBetweenActiveAndInactive(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			TopologicalNode activeNode,
			TopologicalNode inactiveNode)
	{
		super.removeNodeLink(nodeLink);
		super.removeNode(inactiveNode);
		super.changePhysicalNodeActivity(activeNode, false);

		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink(nodeLink);

		// �������� �������� ����
		if(physicalLink.getStartNode().equals(inactiveNode))
		{
			physicalLink.setStartNode(activeNode);
		}
		else
		{
			physicalLink.setEndNode(activeNode);
		}

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}
	
	/**
	 * ������� �������� ����� ������� ����� � �������������� �������� �����
	 */
	protected void removeNodeLinkBetweenSiteAndActiveNode(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			SiteNode site,
			TopologicalNode node)
	{
		super.changePhysicalNodeActivity(node, false);

		super.removeNodeLink(nodeLink);
		
		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink( nodeLink);

		// �������� �������� ����
		if (physicalLink.getStartNode().equals(site))
		{
			physicalLink.setStartNode(node);
		}
		else
		{
			physicalLink.setEndNode(node);
		}

		super.registerStateChange(physicalLink, pls, physicalLink.getState());
	}

	/**
	 * ������� �������� ����� ������� ����� � �������������� ���������� �����.
	 * ���������� � ���� ������ ������� ���������� ���������� �����, ������� 
	 * �� � �������������� ���� ����
	 */
	protected void removeNodeLinkBetweenSiteAndInactiveNode(
			NodeLink nodeLink, 
			PhysicalLink physicalLink,
			SiteNode site,
			TopologicalNode node)
	{
		super.removeNode(node);
		super.removeNodeLink(nodeLink);
		super.removePhysicalLink(physicalLink);
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		// �������� ����� ���� ������ � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � ���� ����� ���������
		// ���� isRemoved
		if(nodeLink.isRemoved())
			return;

		map = logicalNetLayer.getMapView().getMap();

		PhysicalLink physicalLink = 
				nodeLink.getPhysicalLink();

		if (nodeLink.getStartNode() instanceof SiteNode
			&& nodeLink.getEndNode() instanceof SiteNode)
        {
			this.removeNodeLinkBetweenTwoSites(nodeLink, physicalLink);
        }//MapSiteNodeElement && MapSiteNodeElement
		else
		if(nodeLink.getStartNode() instanceof TopologicalNode
			&& nodeLink.getEndNode() instanceof TopologicalNode)
		{
			TopologicalNode leftNode = 
					(TopologicalNode)nodeLink.getStartNode();
			TopologicalNode rightNode = 
					(TopologicalNode)nodeLink.getEndNode();
			
			if(leftNode.isActive() && rightNode.isActive())
			{
				this.removeNodeLinkBetweenActiveNodes(
						nodeLink, 
						physicalLink, 
						leftNode, 
						rightNode);
			}// isActive() && isActive()
			else
			if( !leftNode.isActive() && !rightNode.isActive())
			{
				this.removeNodeLinkBetweenInactiveNodes(
						nodeLink, 
						physicalLink, 
						leftNode, 
						rightNode);
			}// ! isActive() && ! isActive()
			else
			{
				TopologicalNode activeNode = 
					(leftNode.isActive() ) 
						? leftNode 
						: rightNode;
				TopologicalNode inactiveNode = 
					(leftNode.isActive() ) 
						? rightNode 
						: leftNode;

				this.removeNodeLinkBetweenActiveAndInactive(
						nodeLink, 
						physicalLink, 
						activeNode, 
						inactiveNode);
			}// isActive() && ! isActive()
			
		}//MapPhysicalNodeElement && MapPhysicalNodeElement
		else
		// defaults to MapSiteNodeElement && MapPhysicalNodeElement
		{
			SiteNode site = null;
			TopologicalNode node = null;
			
			if(nodeLink.getStartNode() instanceof SiteNode
				&& nodeLink.getEndNode() instanceof TopologicalNode)
			{
				site = (SiteNode)nodeLink.getStartNode();
				node = (TopologicalNode)nodeLink.getEndNode();
	
			}
			else
			if(nodeLink.getEndNode() instanceof SiteNode
				&& nodeLink.getStartNode() instanceof TopologicalNode)
			{
				site = (SiteNode)nodeLink.getEndNode();
				node = (TopologicalNode)nodeLink.getStartNode();
			}

			if (node.isActive())
			{
				this.removeNodeLinkBetweenSiteAndActiveNode(
						nodeLink, 
						physicalLink, 
						site, 
						node);
			}//if (node.isActive())
			else
			{
				this.removeNodeLinkBetweenSiteAndInactiveNode(
						nodeLink, 
						physicalLink, 
						site, 
						node);
			}//if ! (node.isActive())
		}//MapSiteNodeElement && MapPhysicalNodeElement
			
		MapView mapView = logicalNetLayer.getMapView();

		if(physicalLink.getStartNode() instanceof SiteNode
			&& physicalLink.getEndNode() instanceof SiteNode)
		{
			for(Iterator it = mapView.getCablePaths(physicalLink).iterator(); it.hasNext();)
			{
				MapCablePathElement cpath = (MapCablePathElement )it.next();
				cpath.removeLink(physicalLink);
				MapUnboundLinkElement unbound = 
					super.createUnboundLinkWithNodeLink(
						physicalLink.getStartNode(),
						physicalLink.getEndNode());
				unbound.setCablePath(cpath);
				cpath.addLink(unbound);
			}
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
