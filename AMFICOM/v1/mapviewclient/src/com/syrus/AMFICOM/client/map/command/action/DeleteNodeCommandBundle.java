/**
 * $Id: DeleteNodeCommandBundle.java,v 1.21 2005/04/13 11:09:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 *  ������� �������� �������� ���������� ������ MapNodeElement. �������
 * ������� ��  ������������������ ��������� ��������
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/04/13 11:09:42 $
 * @module mapviewclient_v1
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
	protected void deleteSite(SiteNode site)
		throws Throwable
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			return;
		}

		MapView mapView = this.logicalNetLayer.getMapView();
		
		// ���� ��������� ������� ���� (�� ������������� �������),
		// ���������� ��������� ��� ��������� ����, ���������� ���
		for(Iterator it = mapView.getCablePaths(site).iterator(); it.hasNext();)
		{
			CablePath cablePath = (CablePath)it.next();
			
			// ���� ��������� ���� �������� �������� ��������� ��������
			// ���������� ����, ��������� ���� ��������� � �����
			if(cablePath.getStartNode().equals(site)
				|| cablePath.getEndNode().equals(site))
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
					if(le.getStartNode().equals(site)
						|| le.getEndNode().equals(site))
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
						left.getOtherNode(site),
						right.getOtherNode(site));
				unbound.setCablePath(cablePath);
				cablePath.addLink(unbound, CableController.generateCCI(unbound));

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
		java.util.Set nodeLinksToDelete = this.map.getNodeLinks(site);
		Iterator iter = nodeLinksToDelete.iterator();

		// ����� �� ������ ��������� ����������
		while(iter.hasNext())
		{
			NodeLink nodeLink = (NodeLink)iter.next();
			PhysicalLink physicalLink = nodeLink.getPhysicalLink();
					
			if(physicalLink instanceof UnboundLink)
			{
				// ������ ������ ��������� � ���������� �����
				// (������������� ����� �������)
				continue;
			}

			// ���� �� ������ ����� ��������� �����
			AbstractNode oppositeNode = nodeLink.getOtherNode(site);

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

					if(physicalLink.getEndNode() == site)
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

		super.removeNode(site);
	}
	
	/**
	 * �������� ��������������� ����
	 */
	public void deletePhysicalNode(TopologicalNode topologicalNode)
		throws Throwable
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		PhysicalLink physicalLink = topologicalNode.getPhysicalLink();

		// ���� ���� �� �������, �� ���� �������� �������� ����� ���������� �����
		if ( !topologicalNode.isActive() )
		{
			//��� �������� ���� ��������� ��� ��������� �����, ��������� �� ����
			java.util.Set nodeLinksToDelete = this.map.getNodeLinks(topologicalNode);
			Iterator iter = nodeLinksToDelete.iterator();
	
			// ����� �� ������ ��������� ���������� (���������� ��� ������ 
			// ���� �������)
			while(iter.hasNext())
			{
				NodeLink nodeLink = (NodeLink)iter.next();
				AbstractNode oppositeNode = nodeLink.getOtherNode(topologicalNode);
						
				// ���� �������� ��������� �������������� ���� � ���� ����
				// �� �� ���������� ���� ���������� �����, �� ���������� 
				// �������, � ����� �������� �������������� ����
				if(oppositeNode instanceof SiteNode)
				{
					super.removeNode(topologicalNode);
					super.removeNodeLink(nodeLink);
					super.removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// � ��������� ������ �������������� ���� - �������� ���
				// ���������� �����. ������� ���
				{
					TopologicalNode mpne = (TopologicalNode)oppositeNode;

					super.removeNode(topologicalNode);
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
						
						if(physicalLink.getEndNode() == topologicalNode)
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
		if ( topologicalNode.isActive() )
		{
			// �������� ������� ��������� �����
			Iterator nodeLinksIterator = this.map.getNodeLinks(topologicalNode).iterator();
			NodeLink nodeLinkLeft = 
					(NodeLink)nodeLinksIterator.next();
			NodeLink nodeLinkRight = 
					(NodeLink)nodeLinksIterator.next();
			
			// �������� �������� ���� ������� ����������
			AbstractNode nodeLeft =
					nodeLinkLeft.getOtherNode(topologicalNode);
			AbstractNode nodeRight =
					nodeLinkRight.getOtherNode(topologicalNode);

			// ������� ��� ��������� � ����������� �� ���� � �����
			super.removeNodeLink(nodeLinkLeft);
			super.removeNodeLink(nodeLinkRight);
			super.removeNode(topologicalNode);

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
		throws Throwable
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;

		MapView mapView = this.logicalNetLayer.getMapView();
	
		super.removeNode(unbound);

		// ��������� ������ ��� ��������		
		List cablePaths = new LinkedList();
		cablePaths.addAll(mapView.getCablePaths(unbound));
		
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
	public void deleteMark(Mark mark)
		throws Throwable
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;

		super.removeNode(mark);
	}

	/**
	 * ������� ������
	 */
	public void deleteMarker(Marker marker)
		throws Throwable
	{
		if ( !getContext().getApplicationModel().isEnabled(MapApplicationModel.ACTION_USE_MARKER))
			return;

		getLogicalNetLayer().getMapView().removeMarker(marker);
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
		if(this.node.isRemoved())
			return;
			
		this.map = this.logicalNetLayer.getMapView().getMap();
		
		try {
			//� ����������� �� ���� ������ ���� node � �� ������ ���������� �������
			if ( this.node instanceof UnboundNode)
			{
				this.deleteUnbound((UnboundNode)this.node);
			}
			else
			if ( this.node instanceof SiteNode)
			{
				this.deleteSite((SiteNode)this.node);
			}
			else
			if ( this.node instanceof TopologicalNode)
			{
				this.deletePhysicalNode((TopologicalNode)this.node);
			}
			else
			if ( this.node instanceof Mark)
			{
				this.deleteMark((Mark)this.node);
			}
			else
			if ( this.node instanceof Marker)
			{
				this.deleteMarker((Marker)this.node);
			}
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}

