/**
 * $Id: DeleteNodeCommandBundle.java,v 1.6 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
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
 * @version $Revision: 1.6 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteNodeCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	MapNodeElement node;
	
	/**
	 * �����, �� ������� ������������ ��������
	 */
	Map map;

	public DeleteNodeCommandBundle(MapNodeElement node)
	{
		this.node = node;
	}
	
	/**
	 * ������� ���� ����
	 */
	protected void deleteSite(MapSiteNodeElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionDeleteEquipment"))
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
				MapPhysicalLinkElement left = null;
				MapPhysicalLinkElement right = null;
				
				// ��������� "�����" � "�������" ����� ���� ������������
				// ���������� ����. �� ����� ���, ��������� ������
				// ����� ����� ���������� ���������� ����
				for(Iterator it2 = cpath.getLinks().iterator(); it2.hasNext();)
				{
					MapPhysicalLinkElement le = (MapPhysicalLinkElement )it2.next();
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
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			MapPhysicalLinkElement physicalLink = map
					.getPhysicalLink(nodeLink.getPhysicalLinkId());
					
			if(physicalLink instanceof MapUnboundLinkElement)
			{
				// ������ ������ ��������� � ���������� �����
				// (������������� ����� �������)
				continue;
			}

			// ���� �� ������ ����� ��������� �����
			MapNodeElement oppositeNode = nodeLink.getOtherNode(node);

			// ���� �������� ��������� ��� ���� ����, �� ���������� ����
			// ���������� �����, �������, �������������, ���������� �������
			if(oppositeNode instanceof MapSiteNodeElement)
			{
				super.removeNodeLink(nodeLink);
				super.removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else
			{
				MapPhysicalNodeElement physicalNode = (MapPhysicalNodeElement )oppositeNode;
				
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
	public void deletePhysicalNode(MapPhysicalNodeElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionDeleteNode"))
			return;

		MapPhysicalLinkElement physicalLink = map
				.getPhysicalLink(node.getPhysicalLinkId());

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
				MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
				MapNodeElement oppositeNode = nodeLink.getOtherNode(node);
						
				// ���� �������� ��������� �������������� ���� � ���� ����
				// �� �� ���������� ���� ���������� �����, �� ���������� 
				// �������, � ����� �������� �������������� ����
				if(oppositeNode instanceof MapSiteNodeElement)
				{
					super.removeNode(node);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// � ��������� ������ �������������� ���� - �������� ���
				// ���������� �����. ������� ���
				{
					MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )oppositeNode;

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
			MapNodeLinkElement nodeLinkLeft = 
					(MapNodeLinkElement )node.getNodeLinks().get(0);
			MapNodeLinkElement nodeLinkRight = 
					(MapNodeLinkElement )node.getNodeLinks().get(1);
			
			// �������� �������� ���� ������� ����������
			MapNodeElement nodeLeft =
					(MapNodeElement )nodeLinkLeft.getOtherNode(node);
			MapNodeElement nodeRight =
					(MapNodeElement )nodeLinkRight.getOtherNode(node);

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
						&& nodeLeft instanceof MapPhysicalNodeElement
						&& nodeRight instanceof MapPhysicalNodeElement);

			MapElementState pls = physicalLink.getState();

			//������� ��� ������� ��������� �� ����� � �������� ������ ��� �����
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(doRemoveLink)
			{
				// ��������� �����
				super.removePhysicalLink(physicalLink);
				if(nodeLeft instanceof MapPhysicalNodeElement)
				{
					super.removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						super.removeNode(nodeRight);
				}
			}
			else
			{
				// ������� ����� �������� ����� � �������� �� ����� � � �����
				MapNodeLinkElement newNodeLink = super.createNodeLink(nodeLeft, nodeRight);
				newNodeLink.setPhysicalLinkId(physicalLink.getId());

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
	public void deleteMark(MapMarkElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionMarkDelete"))
			return;

		super.removeNode(node);
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
		if ( node instanceof MapSiteNodeElement)
		{
			this.deleteSite((MapSiteNodeElement )node);
		}
		else
		if ( node instanceof MapPhysicalNodeElement)
		{
			this.deletePhysicalNode((MapPhysicalNodeElement )node);
		}
		else
		if ( node instanceof MapMarkElement)
		{
			this.deleteMark((MapMarkElement )node);
		}
	}

}

