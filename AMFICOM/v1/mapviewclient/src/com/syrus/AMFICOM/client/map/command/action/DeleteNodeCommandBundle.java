/**
 * $Id: DeleteNodeCommandBundle.java,v 1.3 2004/10/09 13:33:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import java.util.Iterator;

/**
 *  ������� �������� �������� ���������� ������ MapNodeElement. �������
 * ������� ��  ������������������ ��������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/09 13:33:40 $
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

		for(Iterator it = mapView.getCablePaths(node).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			mapView.scanCable(cpath.getSchemeCableLink());
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
			MapNodeElement oppositeNode = nodeLink.getOtherNode(node);

			// ���� �������� ��������� ��� ���� ����, �� ���������� ����
			// ���������� �����, �������, �������������, ���������� �������
			if(oppositeNode instanceof MapSiteNodeElement)
			{
				removeNodeLink(nodeLink);
				removePhysicalLink(physicalLink);
			}//if(oppositeNode instanceof MapSiteNodeElement)
			else
			{
				MapPhysicalNodeElement physicalNode = (MapPhysicalNodeElement )oppositeNode;
				
				// ���� �������������� ���� �������, �� �������� ����� ���������
				// �� ���������� �����
				if(physicalNode.isActive())
				{
					changePhysicalNodeActivity(physicalNode, false);
					
					removeNodeLink(nodeLink);

					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					if(physicalLink.getEndNode() == node)
						physicalLink.setEndNode(oppositeNode);
					else
						physicalLink.setStartNode(oppositeNode);

					registerStateChange(physicalLink, pls, physicalLink.getState());
				}//if(mpne.isActive())
				else
				// � ��������� ������ �������� ���������� ����
				// ���������� �����, �� ���������� �������, � �����
				// �������� �������������� ����
				{
					removeNode(physicalNode);
					removeNodeLink(nodeLink);
					removePhysicalLink(physicalLink);
				}
			}//if ! (oppositeNode instanceof MapSiteNodeElement)
		}//while(e.hasNext())

		removeNode(node);

		for(Iterator it = mapView.getCablePaths(node).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			mapView.scanCable(cpath.getSchemeCableLink());
		}
	}
	
	/**
	 * �������� ��������������� ����
	 */
	public void deletePhysicalNode(MapPhysicalNodeElement node)
	{
		DataSourceInterface dataSource = getContext().getDataSource();

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
					removeNode(node);
					removeNodeLink(nodeLink);
					removePhysicalLink(physicalLink);
				}//if(oppositeNode instanceof MapSiteNodeElement)
				else
				// � ��������� ������ �������������� ���� - �������� ���
				// ���������� �����. ������� ���
				{
					MapPhysicalNodeElement mpne = (MapPhysicalNodeElement )oppositeNode;

					removeNode(node);
					removeNodeLink(nodeLink);
					
					MapElementState pls = physicalLink.getState();

					physicalLink.removeNodeLink(nodeLink);

					registerStateChange(physicalLink, pls, physicalLink.getState());
					
					// ���� ������ �������������� ���� ���� �� �������, �� 
					// ������� ���������� ����� �� ������ ��������� � ����
					// �����, ��� ��� ��������� ���� �������
					if(!mpne.isActive())
					{
						removeNode(mpne);
						removePhysicalLink(physicalLink);
					}
					else
					// � ��������� ������ �������� �������� ���� ���������� �����
					{
						changePhysicalNodeActivity(mpne, false);

						MapElementState pls2 = physicalLink.getState();
						
						if(physicalLink.getEndNode() == node)
							physicalLink.setEndNode(mpne);
						else
							physicalLink.setStartNode(mpne);

						registerStateChange(physicalLink, pls2, physicalLink.getState());
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
			removeNodeLink(nodeLinkLeft);
			removeNodeLink(nodeLinkRight);
			removeNode(node);

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
			
			boolean do_remove_link = (nodeLeft == nodeRight)
					|| ((physicalLink.getNodeLinks().size() == 2)
						&& nodeLeft instanceof MapPhysicalNodeElement
						&& nodeRight instanceof MapPhysicalNodeElement);

			MapElementState pls = physicalLink.getState();

			//������� ��� ������� ��������� �� ����� � �������� ������ ��� �����
			physicalLink.removeNodeLink(nodeLinkLeft);
			physicalLink.removeNodeLink(nodeLinkRight);
			
			if(do_remove_link)
			{
				removePhysicalLink(physicalLink);
				if(nodeLeft instanceof MapPhysicalNodeElement)
				{
					removeNode(nodeLeft);
					if(nodeLeft != nodeRight)
						removeNode(nodeRight);
				}
			}
			else
			{
				// ������� ����� �������� ����� � �������� �� ����� � � �����
				MapNodeLinkElement newNodeLink = createNodeLink(nodeLeft, nodeRight);
				newNodeLink.setPhysicalLinkId(physicalLink.getId());

				physicalLink.addNodeLink(newNodeLink);
			}

			registerStateChange(physicalLink, pls, physicalLink.getState());

		}//if ( node.isActive() )
	}

	protected void deleteUnbound(MapUnboundNodeElement unbound)
	{
		MapView mapView = logicalNetLayer.getMapView();
	
		deleteSite(unbound);
		
		for(Iterator it = mapView.getCablePaths(unbound).iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			mapView.scanCable(cpath.getSchemeCableLink());
		}
	}

	/**
	 * ������� ����� �� ���������� �����
	 */
	public void deleteMark(MapMarkElement node)
	{
		if ( !getContext().getApplicationModel().isEnabled("mapActionMarkDelete"))
			return;

		removeNode(node);
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		// ���� ����� ���� ������ � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � ���� ����� ���������
		// ���� isRemoved
		if(node.isRemoved())
			return;
			
		map = logicalNetLayer.getMapView().getMap();
		
		//� ����������� �� ���� ������ ���� node � �� ������ ���������� �������
		if ( node instanceof MapUnboundNodeElement)
		{
			deleteUnbound((MapUnboundNodeElement )node);
		}
		else
		if ( node instanceof MapSiteNodeElement)
		{
			deleteSite((MapSiteNodeElement )node);
		}
		else
		if ( node instanceof MapPhysicalNodeElement)
		{
			deletePhysicalNode((MapPhysicalNodeElement )node);
		}
		else
		if ( node instanceof MapMarkElement)
		{
			deleteMark((MapMarkElement )node);
		}
	}

}

