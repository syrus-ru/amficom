/**
 * $Id: DeleteNodeLinkCommandBundle.java,v 1.17 2005/03/01 15:37:03 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;

/**
 * � ������ ������ ����������� �������� �������� NodeLink. � �����������
 * �� ����, ����� �������� ����� �� ������ ���������� �������� �������� 
 * ���������� �����, �����, �����  (� �����). �������
 * ������� �� ������������������ ��������� ��������
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/03/01 15:37:03 $
 * @module mapviewclient_v1
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
		throws Throwable
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
		throws Throwable
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
		throws Throwable
	{
		// ������� �������� �����
		super.removeNodeLink(nodeLink);

		// ��� ���� ��� �������� �������������� ���� ���������� �����������
		super.changePhysicalNodeActivity(leftNode, false);
		super.changePhysicalNodeActivity(rightNode, false);

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
					physicalLink.getStartNode());

			// ��������� ��������� � ����� ����� ���� �� ��������� ��
			// ���� �� �������� ����� ���������� ���������
			AbstractNode foundNode = super.moveNodeLinks(
					physicalLink,
					newPhysicalLink,
					false,
					leftNode,
					rightNode);

			newPhysicalLink.setEndNode(foundNode);

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
		throws Throwable
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
		throws Throwable
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
			TopologicalNode node)
		throws Throwable
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
		if(this.nodeLink.isRemoved())
			return;

		this.map = this.logicalNetLayer.getMapView().getMap();

		try {
			PhysicalLink physicalLink = 
					this.nodeLink.getPhysicalLink();
			if (this.nodeLink.getStartNode() instanceof SiteNode
				&& this.nodeLink.getEndNode() instanceof SiteNode)
			{
				this.removeNodeLinkBetweenTwoSites(this.nodeLink, physicalLink);
			}//MapSiteNodeElement && MapSiteNodeElement
			else
			if(this.nodeLink.getStartNode() instanceof TopologicalNode
				&& this.nodeLink.getEndNode() instanceof TopologicalNode)
			{
				TopologicalNode leftNode = 
						(TopologicalNode)this.nodeLink.getStartNode();
				TopologicalNode rightNode = 
						(TopologicalNode)this.nodeLink.getEndNode();
				
				if(leftNode.isActive() && rightNode.isActive())
				{
					this.removeNodeLinkBetweenActiveNodes(
							this.nodeLink, 
							physicalLink, 
							leftNode, 
							rightNode);
				}// isActive() && isActive()
				else
				if( !leftNode.isActive() && !rightNode.isActive())
				{
					this.removeNodeLinkBetweenInactiveNodes(
							this.nodeLink, 
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
							this.nodeLink, 
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
				
				if(this.nodeLink.getStartNode() instanceof SiteNode
					&& this.nodeLink.getEndNode() instanceof TopologicalNode)
				{
					site = (SiteNode)this.nodeLink.getStartNode();
					node = (TopologicalNode)this.nodeLink.getEndNode();

				}
				else
				if(this.nodeLink.getEndNode() instanceof SiteNode
					&& this.nodeLink.getStartNode() instanceof TopologicalNode)
				{
					site = (SiteNode)this.nodeLink.getEndNode();
					node = (TopologicalNode)this.nodeLink.getStartNode();
				}

				if (node.isActive())
				{
					this.removeNodeLinkBetweenSiteAndActiveNode(
							this.nodeLink, 
							physicalLink, 
							site, 
							node);
				}//if (node.isActive())
				else
				{
					this.removeNodeLinkBetweenSiteAndInactiveNode(
							this.nodeLink, 
							physicalLink, 
							node);
				}//if ! (node.isActive())
			}//MapSiteNodeElement && MapPhysicalNodeElement
			MapView mapView = this.logicalNetLayer.getMapView();
			if(physicalLink.getStartNode() instanceof SiteNode
				&& physicalLink.getEndNode() instanceof SiteNode)
			{
				for(Iterator it = mapView.getCablePaths(physicalLink).iterator(); it.hasNext();)
				{
					CablePath cpath = (CablePath)it.next();
					cpath.removeLink(physicalLink);
					UnboundLink unbound = 
						super.createUnboundLinkWithNodeLink(
							physicalLink.getStartNode(),
							physicalLink.getEndNode());
					unbound.setCablePath(cpath);
					cpath.addLink(unbound, CableController.generateCCI(unbound));
				}
			}
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
