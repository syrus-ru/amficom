/**
 * $Id: DeleteNodeCommandBundle.java,v 1.11 2004/12/23 16:57:59 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  ������� �������� �������� ���������� ������ MapNodeElement. �������
 * ������� ��  ������������������ ��������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	AbstractNode node;
	
	/**
	 * �����, �� ������� ������������ ��������
	 */
	Map map;

	public DeleteNodeCommandBundle(AbstractNode node)
	{
		this.node = node;
	}
	
	/**
	 * ������� ���� ����
	 */
	protected void deleteSite(SiteNode node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			return;
		}

		MapView mapView = logicalNetLayer.getMapView();
		
		// ���� ��������� ������� ���� (�� ������������� �������),
		// ���������� ��������� ��� ��������� ����, ���������� ���
		for(Iterator it = mapView.getCablePaths(node).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			
			// ���� ��������� ���� �������� �������� ��������� ��������
			// ���������� ����, ��������� ���� ��������� � �����
			if(cpath.getStartNode().equals(node)
				|| cpath.getEndNode().equals(node))
			{
				super.removeCablePathLinks(cpath);
				super.removeCablePath(cpath);
			}
			else
			// � ��������� ������ ����������� ���������� ���� ����� ����
			// ���������� �� ������������� �����
			{
				PhysicalLink left = null;
				PhysicalLink right = null;
				
				// ��������� "�����" � "�������" ����� ���� ������������
				// ���������� ����. �� ����� ���, ��������� ������
				// ����� ����� ���������� ���������� ����
				for(Iterator it2 = cpath.getLinks().iterator(); it2.hasNext();)
				{
					PhysicalLink le = (PhysicalLink)it2.next();
					if(le.getStartNode().equals(node)
						|| le.getEndNode().equals(node))
					{
						if(left == null)
							left = le;
						else
							right = le;
					}
				}
				
				// ��������� �����
				cpath.removeLink(left);
				cpath.removeLink(right);

				// ������ ��� ��������� ����� �������������
				MapUnboundLinkElement unbound = 
					super.createUnboundLinkWithNodeLink(
						left.getOtherNode(node),
						right.getOtherNode(node));
				unbound.setCablePath(cpath);
				cpath.addLink(unbound);

				// ���� "�����" ���� ��������������, ��� ��������� (������ 
				// �� ������ �����������
				if(left instanceof MapUnboundLinkElement)
				{
					super.removeUnboundLink((MapUnboundLinkElement )left);
				}
				// ���� "������" ���� ��������������, ��� ��������� (������ 
				// �� ������ �����������
				if(right instanceof MapUnboundLinkElement)
				{
					super.removeUnboundLink((MapUnboundLinkElement )right);
				}
			}
		}

		//��� �������� ���� ��������� ��� ��������� �����, ��������� �� ����
		java.util.List nodeLinksToDelete = node.getNodeLinks();
		Iterator e = nodeLinksToDelete.iterator();

		// ����� �� ������ ��������� ����������
		while(e.hasNext())
		{
			NodeLink nodeLink = (NodeLink)e.next();
			PhysicalLink physicalLink = nodeLink.getPhysicalLink();
					
			if(physicalLink instanceof MapUnboundLinkElement)
			{
				// ������ ������ ��������� � ���������� �����
				// (������������� ����� �������)
				continue;
			}

			// ���� �� ������ ����� ��������� �����
			AbstractNode oppositeNode = nodeLink.getOtherNode(node);

			// ���� �������� ��������� ��� ���� ����, �� ���������� ����
			// ���������� �����, �������, �������������, ���������� �������
			if(oppositeNode instanceof SiteNode)
			{
				super.removeNodeLink(nodeLink);
				super.removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else
			{
				TopologicalNode physicalNode = (TopologicalNode)oppositeNode;
				
				// ���� �������������� ���� �������, �� �������� ����� ���������
				// �� ���������� �����
				if(physicalNode.isActive())
				{
					super.changePhysicalNodeActivity(physicalNode, false);
					
					super.removeNodeLink(nodeLink);

					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					if(physicalLink.getEndNode() == node)
						physicalLink.setEndNode(oppositeNode);
					else
						physicalLink.setStartNode(oppositeNode);

					super.registerStateChange(physicalLink, pls, physicalLink.getState());
				}//if(mpne.isActive())
				else
				// � ��������� ������ �������� ���������� ����
				// ���������� �����, �� ���������� �������, � �����
				// �������� �������������� ����
				{
					super.removeNode(physicalNode);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}
			}//if ! (oppositeNode instanceof MapSiteNodeElement)
		}//while(e.hasNext())

		super.removeNode(node);
	}
	
	/**
	 * �������� ��������������� ����
	 */
	public void deletePhysicalNode(TopologicalNode node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		PhysicalLink physicalLink = node.getPhysicalLink();

		// ���� ���� �� �������, �� ���� �������� �������� ����� ���������� �����
		if ( !node.isActive() )
		{
			//��� �������� ���� ��������� ��� ��������� �����, ��������� �� ����
			java.util.List nodeLinksToDelete = node.getNodeLinks();
			Iterator e = nodeLinksToDelete.iterator();
	
			// ����� �� ������ ��������� ���������� (���������� ��� ������ 
			// ���� �������)
			while(e.hasNext())
			{
				NodeLink nodeLink = (NodeLink)e.next();
				AbstractNode oppositeNode = nodeLink.getOtherNode(node);
						
				// ���� �������� ��������� �������������� ���� � ���� ����
				// �� �� ���������� ���� ���������� �����, �� ���������� 
				// �������, � ����� �������� �������������� ����
				if(oppositeNode instanceof SiteNode)
				{
					super.removeNode(node);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// � ��������� ������ �������������� ���� - �������� ���
				// ���������� �����. ������� ���
				{
					TopologicalNode mpne = (TopologicalNode)oppositeNode;

					super.removeNode(node);
					super.removeNodeLink(nodeLink);
					
					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					super.registerStateChange(physicalLink, pls, physicalLink.getState());
					
					// ���� ������ �������������� ���� ���� �� �������, �� 
					// ������� ���������� ����� �� ������ ��������� � ����
					// �����, ��� ��� ��������� ���� �������
					if(!mpne.isActive())
					{
						super.removeNode(mpne);
						super.removePhysicalLink(physicalLink);
					}
					else
					// � ��������� ������ �������� �������� ���� ���������� �����
					{
						super.changePhysicalNodeActivity(mpne, false);

						MapElementState pls2 = physicalLink.getState();
						
						if(physicalLink.getEndNode() == node)
							physicalLink.setEndNode(mpne);
						else
							physicalLink.setStartNode(mpne);

						super.registerStateChange(physicalLink, pls2, physicalLink.getState());
					}
				}// if ! (oppositeNode instanceof MapSiteNodeElement)
			}//while(e.hasNext())
		}//if ( !node.isActive() )
		else
		// ���� ���� �������, �� ��� ��� �������� ��� ��������� ��������� � ����
		if ( node.isActive() )
		{
			// �������� ������� ��������� �����
			NodeLink nodeLinkLeft = 
					(NodeLink)node.getNodeLinks().get(0);
			NodeLink nodeLinkRight = 
					(NodeLink)node.getNodeLinks().get(1);
			
			// �������� �������� ���� ������� ����������
			AbstractNode nodeLeft =
					(AbstractNode)nodeLinkLeft.getOtherNode(node);
			AbstractNode nodeRight =
					(AbstractNode)nodeLinkRight.getOtherNode(node);

			// ������� ��� ��������� � ����������� �� ���� � �����
			super.removeNodeLink(nodeLinkLeft);
			super.removeNodeLink(nodeLinkRight);
			super.removeNode(node);

			// � ������, ���� ���������� ����� ������� �� ���� ����������,
			// �������� 5 ����������:
			// 1. ��� ���� ����� ����� �������
			// 2. ��� ���� ����� ��� ����� � ������
			// 3. ��� ���� ����� ����� ��������� ��� ������
			// 4. ��� ���� ����������� ����� ����������� � ������
			// 5. ��� ���� ����������� ����� ����������� � ��� �����
			//
			// � ������� 1 � 2 ��������� ����� ��, ��� � � ������� ������
			// (����� ������ 2-� ����������).
			// � ������� 3, 4, 5 ��������� ���������� ����� � ��� ���. ����.
			// �� ���������� ���� ��������� �������� ���������� do_remove_link
			// � �������������� ��������������� ��������
			
			boolean doRemoveLink = (nodeLeft == nodeRight)
					|| ((physicalLink.getNodeLinks().size() == 2)
						&& nodeLeft instanceof TopologicalNode
						&& nodeRight instanceof TopologicalNode);

			MapElementState pls = physicalLink.getState();

			//������� ��� ������� ��������� �� ����� � �������� ������ ��� �����
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(doRemoveLink)
			{
				// ��������� �����
				super.removePhysicalLink(physicalLink);
				if(nodeLeft instanceof TopologicalNode)
				{
					super.removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						super.removeNode(nodeRight);
				}
			}
			else
			{
				// ������� ����� �������� ����� � �������� �� ����� � � �����
				NodeLink newNodeLink = super.createNodeLink(physicalLink, nodeLeft, nodeRight);
				newNodeLink.setPhysicalLink(physicalLink);

				physicalLink.addNodeLink(newNodeLink);
			}

			super.registerStateChange(physicalLink, pls, physicalLink.getState());

		}//if ( node.isActive() )
	}

	/**
	 * �������� �������������� ����.
	 * ��� ���� � ����� ��������� ��� ��������� ����, ���������� 
	 * ��������� �������
	 */
	protected void deleteUnbound(MapUnboundNodeElement unbound)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		MapView mapView = logicalNetLayer.getMapView();
	
		super.removeNode(unbound);

		// ��������� ������ ��� ��������		
		List cablePaths = new LinkedList();
		cablePaths.addAll(mapView.getCablePaths(unbound));
		
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			super.removeCablePathLinks(cpath);
			super.removeCablePath(cpath);
		}
	}

	/**
	 * ������� ����� �� ���������� �����
	 */
	public void deleteMark(Mark node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		super.removeNode(node);
	}

	/**
	 * ������� ������
	 */
	public void deleteMarker(MapMarker node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
			return;

		node.getMapView().removeMarker(node);
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		// ���� ����� ���� ������ � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � ���� ����� ���������
		// ���� isRemoved
		if(node.isRemoved())
			return;
			
		map = logicalNetLayer.getMapView().getMap();
		
		//� ����������� �� ���� ������ ���� node � �� ������ ���������� �������
		if ( node instanceof MapUnboundNodeElement)
		{
			this.deleteUnbound((MapUnboundNodeElement )node);
		}
		else
		if ( node instanceof SiteNode)
		{
			this.deleteSite((SiteNode)node);
		}
		else
		if ( node instanceof TopologicalNode)
		{
			this.deletePhysicalNode((TopologicalNode)node);
		}
		else
		if ( node instanceof Mark)
		{
			this.deleteMark((Mark)node);
		}
		else
		if ( node instanceof MapMarker)
		{
			this.deleteMarker((MapMarker )node);
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

