/**
 * $Id: DeleteNodeLinkCommandBundle.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.util.Iterator;

/**
 * � ������ ������ ����������� �������� �������� NodeLink. � �����������
 * �� ����, ����� �������� ����� �� ������ ���������� �������� �������� 
 * ���������� �����, �����, �����  (� �����). �������
 * ������� �� ������������������ ��������� ��������
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteNodeLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ��������
	 */
	MapNodeLinkElement nodeLink;
	
	/**
	 * �����
	 */
	Map map;

	public DeleteNodeLinkCommandBundle(MapNodeLinkElement nodeLink)
	{
		super();
		this.nodeLink = nodeLink;
	}

	/**
	 * ������� �������� ����� ����� �������� ������. ��������� � ����
	 * ������ ������� ���������� ���������� �����, ������� �� ����
	 */
	protected void removeNodeLinkBetweenTwoSites(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink)
	{
		removeNodeLink(nodeLink);
		removePhysicalLink(physicalLink);
	}

	/**
	 * ������� �������� ����� ����� ����������� �������������� ������. 
	 * ���������� � ���� ������ ������� ���������� ���������� �����, ������� 
	 * ��, � �������������� ���� ����
	 */
	protected void removeNodeLinkBetweenInactiveNodes(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapPhysicalNodeElement leftNode,
			MapPhysicalNodeElement rightNode)
	{
		removeNode(leftNode);
		removeNode(rightNode);
		removeNodeLink(nodeLink);
		removePhysicalLink(physicalLink);
	}

	/**
	 * ������� �������� ����� ����� ����������� �������������� ������.
	 * � ���� ������ ���������� ����� ������� �� ��� ���������� ����� 
	 */
	protected void removeNodeLinkBetweenActiveNodes(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapPhysicalNodeElement leftNode,
			MapPhysicalNodeElement rightNode)
	{
		DataSourceInterface dataSource = getContext().getDataSourceInterface();

		// ������� �������� �����
		removeNodeLink(nodeLink);

		// ��� ���� ��� �������� �������������� ���� ���������� �����������
		leftNode.setActive(false);
		rightNode.setActive(false);

		MapElementState pls = physicalLink.getState();

		// ������ �������� �� ���������� �����
		physicalLink.removeNodeLink(nodeLink);

		// ���� ����������� ��������� �����, ���������� ������ ��������������
		// ����, �� ������ �������� �������� ����
		if(physicalLink.getStartNode() == physicalLink.getEndNode()
			&& physicalLink.getStartNode() instanceof MapPhysicalNodeElement)
		{
			physicalLink.setStartNode(leftNode);
			physicalLink.setEndNode(rightNode);
		}
		else
		// � ��������� ������ ��������� ����� �� ���
		{
			// ������� ������ ���������� �����
			MapPhysicalLinkElement newPhysicalLink = createPhysicalLink(
					physicalLink.getStartNode(),
					null);
		
			// �������� ��� ��������� ������ ���������� �����
			java.util.List nodelinks = physicalLink.getNodeLinks();
		
			// ���������� ��������� ���� � ��������� �������� ���������� �����
			MapNodeLinkElement startNodeLink = null;
			MapNodeElement startNode = physicalLink.getStartNode();
			for(Iterator it = nodelinks.iterator(); it.hasNext();)
			{
				startNodeLink = (MapNodeLinkElement )it.next();
				if(startNodeLink.getStartNode() == physicalLink.getStartNode()
					|| startNodeLink.getEndNode() == physicalLink.getStartNode())
				{
					break;
				}
			}
		
			// ������� ���� �� ���������� ����� - ������������ ��������� � ����� 
			// ���������� �����. �������� �� ���������� �� ������� ���� �� ���������
			// �� ��������, �������� � ���������
			for(;;)
			{
				// ���������� �������� � ����� �����
				physicalLink.removeNodeLink(startNodeLink);
				newPhysicalLink.addNodeLink(startNodeLink);
				
				MapElementState nls = startNodeLink.getState();
				
				startNodeLink.setPhysicalLinkId(newPhysicalLink.getId());
				
				registerStateChange(startNodeLink, nls, startNodeLink.getState());
				
				// ������� � ���������� ����
				startNode = startNodeLink.getOtherNode(startNode);
				
				// ���� ���������� �� ������ ����� �����, �� ��������
				// �������� ���� � ���������
				if(startNode == leftNode)
				{
					newPhysicalLink.setEndNode(leftNode);
					physicalLink.setStartNode(rightNode);
					break;
				}
				else
				if(startNode == rightNode)
				{
					newPhysicalLink.setEndNode(rightNode);
					physicalLink.setStartNode(leftNode);
					break;
				}
	
				// ������� � ���������� ���������
				for(Iterator it = startNode.getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
					if(startNodeLink != mnle)
						startNodeLink = mnle;
				}
			}//for(;;)
			registerStateChange(physicalLink, pls, physicalLink.getState());
		}
	}

	/**
	 * ������� �������� �������� �����
	 */
	protected void removeNodeLinkBetweenActiveAndInactive(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapPhysicalNodeElement activeNode,
			MapPhysicalNodeElement inactiveNode)
	{
		removeNodeLink(nodeLink);
		removeNode(inactiveNode);
		changePhysicalNodeActivity(activeNode, false);

		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink(nodeLink);

		// �������� �������� ����
		if(physicalLink.getStartNode() == inactiveNode)
		{
			physicalLink.setStartNode(activeNode);
		}
		else
		{
			physicalLink.setEndNode(activeNode);
		}

		registerStateChange(physicalLink, pls, physicalLink.getState());

		// �������� ���������?, ��������� �������������� ���� ��� �������
		// � ������ � ����� ���� ������ ��������
		if(physicalLink.getNodeLinks().size() == 0)
		{
			removeNode(activeNode);
			removePhysicalLink( physicalLink);
		}
	}
	
	/**
	 * ������� �������� ����� ������� ����� � �������������� �������� �����
	 */
	protected void removeNodeLinkBetweenSiteAndActiveNode(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapSiteNodeElement site,
			MapPhysicalNodeElement node)
	{
		changePhysicalNodeActivity(node, false);

		removeNodeLink(nodeLink);
		
		MapElementState pls = physicalLink.getState();

		physicalLink.removeNodeLink( nodeLink);

		// �������� �������� ����
		if (physicalLink.getStartNode() == site)
		{
			physicalLink.setStartNode(node);
		}
		else
		{
			physicalLink.setEndNode(node);
		}

		registerStateChange(physicalLink, pls, physicalLink.getState());

		// �������� ���������?, ��������� �������������� ���� ��� �������
		// � ������ � ����� ���� ������ ��������
		if(physicalLink.getNodeLinks().size() == 0)
		{
			removeNode(node);
			removePhysicalLink( physicalLink);
		}
	}

	/**
	 * ������� �������� ����� ������� ����� � �������������� ���������� �����.
	 * ���������� � ���� ������ ������� ���������� ���������� �����, ������� 
	 * �� � �������������� ���� ����
	 */
	protected void removeNodeLinkBetweenSiteAndInactiveNode(
			MapNodeLinkElement nodeLink, 
			MapPhysicalLinkElement physicalLink,
			MapSiteNodeElement site,
			MapPhysicalNodeElement node)
	{
		removeNode(node);
		removeNodeLink(nodeLink);
		removePhysicalLink(physicalLink);
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");
		
		// �������� ����� ���� ������ � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � ���� ����� ���������
		// ���� isRemoved
		if(nodeLink.isRemoved())
			return;

		map = logicalNetLayer.getMapView().getMap();

		MapPhysicalLinkElement physicalLink = map.getPhysicalLink(nodeLink.getPhysicalLinkId());

		if (nodeLink.getStartNode() instanceof MapSiteNodeElement
			&& nodeLink.getEndNode() instanceof MapSiteNodeElement)
        {
			removeNodeLinkBetweenTwoSites(nodeLink, physicalLink);
        }//MapSiteNodeElement && MapSiteNodeElement
		else
		if(nodeLink.getStartNode() instanceof MapPhysicalNodeElement
			&& nodeLink.getEndNode() instanceof MapPhysicalNodeElement)
		{
			MapPhysicalNodeElement leftNode = (MapPhysicalNodeElement )nodeLink.getStartNode();
			MapPhysicalNodeElement rightNode = (MapPhysicalNodeElement )nodeLink.getEndNode();
			
			if(leftNode.isActive() && rightNode.isActive())
			{
				removeNodeLinkBetweenActiveNodes(nodeLink, physicalLink, leftNode, rightNode);
			}// isActive() && isActive()
			else
			if( !leftNode.isActive() && !rightNode.isActive())
			{
				removeNodeLinkBetweenInactiveNodes(nodeLink, physicalLink, leftNode, rightNode);
			}// ! isActive() && ! isActive()
			else
			{
				MapPhysicalNodeElement activeNode = (leftNode.isActive() ) ? leftNode : rightNode;
				MapPhysicalNodeElement inactiveNode = (leftNode.isActive() ) ? rightNode : leftNode;

				removeNodeLinkBetweenActiveAndInactive(nodeLink, physicalLink, activeNode, inactiveNode);
			}// isActive() && ! isActive()
			
		}//MapPhysicalNodeElement && MapPhysicalNodeElement
		else
		// defaults to MapSiteNodeElement && MapPhysicalNodeElement
		{
			MapSiteNodeElement site = null;
			MapPhysicalNodeElement node = null;
			
			if(nodeLink.getStartNode() instanceof MapSiteNodeElement
				&& nodeLink.getEndNode() instanceof MapPhysicalNodeElement)
			{
				site = (MapSiteNodeElement )nodeLink.getStartNode();
				node = (MapPhysicalNodeElement )nodeLink.getEndNode();
	
			}
			else
			if(nodeLink.getEndNode() instanceof MapSiteNodeElement
				&& nodeLink.getStartNode() instanceof MapPhysicalNodeElement)
			{
				site = (MapSiteNodeElement )nodeLink.getEndNode();
				node = (MapPhysicalNodeElement )nodeLink.getStartNode();
			}

			if (node.isActive())
			{
				removeNodeLinkBetweenSiteAndActiveNode(nodeLink, physicalLink, site, node);
			}//if (node.isActive())
			else
			{
				removeNodeLinkBetweenSiteAndInactiveNode(nodeLink, physicalLink, site, node);
			}//if ! (node.isActive())
		}//MapSiteNodeElement && MapPhysicalNodeElement
			
//		logicalNetLayer.getMapView().removePath( mapPhysicalLinkElement.getId() );

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
