/**
 * $Id: DeleteNodeCommandBundle.java,v 1.14 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  ������� �������� �������� ���������� ������ MapNodeElement. �������
 * ������� ��  ������������������ ��������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.14 $, $Date: 2005/01/31 12:19:18 $
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
		for(Iterator it = getLogicalNetLayer().getMapViewController().getCablePaths(node).iterator(); it.hasNext();)
		{
			CablePath cablePath = (CablePath)it.next();
			
			// ���� ��������� ���� �������� �������� ��������� ��������
			// ���������� ����, ��������� ���� ��������� � �����
			if(cablePath.getStartNode().equals(node)
				|| cablePath.getEndNode().equals(node))
			{
				super.removeCablePathLinks(cablePath);
				super.removeCablePath(cablePath);
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
				for(Iterator it2 = cablePath.getLinks().iterator(); it2.hasNext();)
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
				cablePath.removeLink(left);
				cablePath.removeLink(right);

				// ������ ��� ��������� ����� �������������
				UnboundLink unbound = 
					super.createUnboundLinkWithNodeLink(
						left.getOtherNode(node),
						right.getOtherNode(node));
				unbound.setCablePath(cablePath);
				CableController cableController = (CableController )
					getLogicalNetLayer().getMapViewController().getController(cablePath);
				cablePath.addLink(unbound, cableController.generateCCI(unbound));

				// ���� "�����" ���� ��������������, ��� ��������� (������ 
				// �� ������ �����������
				if(left instanceof UnboundLink)
				{
					super.removeUnboundLink((UnboundLink)left);
				}
				// ���� "������" ���� ��������������, ��� ��������� (������ 
				// �� ������ �����������
				if(right instanceof UnboundLink)
				{
					super.removeUnboundLink((UnboundLink)right);
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
					
			if(physicalLink instanceof UnboundLink)
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
	protected void deleteUnbound(UnboundNode unbound)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		MapView mapView = logicalNetLayer.getMapView();
	
		super.removeNode(unbound);

		// ��������� ������ ��� ��������		
		List cablePaths = new LinkedList();
		cablePaths.addAll(getLogicalNetLayer().getMapViewController().getCablePaths(unbound));
		
		for(Iterator it = cablePaths.iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
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
	public void deleteMarker(Marker node)
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
			return;

		getLogicalNetLayer().getMapViewController().removeMarker(node);
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
		if ( node instanceof UnboundNode)
		{
			this.deleteUnbound((UnboundNode)node);
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
		if ( node instanceof Marker)
		{
			this.deleteMarker((Marker)node);
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

