/**
 * $Id: MapActionCommandBundle.java,v 1.7 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.CommandBundle;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapActionCommandBundle extends CommandBundle
{
	LogicalNetLayer logicalNetLayer = null;
	ApplicationContext aContext = null;
	
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	protected ApplicationContext getContext()
	{
		return aContext;
	}
	
	protected LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationContext"))
			aContext = (ApplicationContext )value;
		super.setParameter(field, value);
	}
	
	/**
	 * ��������� �������������� �������� ���� � ���������� ���������
	 */
	protected MapSiteNodeElement createSite(Point2D.Double point,  MapNodeProtoElement proto)
	{
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(proto, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getSite();
	}

	/**
	 * ��������� �������������� �������� ���� � ���������� ���������
	 */
	protected MapPhysicalNodeElement createPhysicalNode(Point2D.Double point)
	{
		CreatePhysicalNodeCommandAtomic cmd = new CreatePhysicalNodeCommandAtomic(point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getNode();
	}

	/**
	 * ��������� �������� �����, �� ���������� �� � ����� �����
	 */
	protected MapNodeLinkElement createNodeLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateNodeLinkCommandAtomic cmd = new CreateNodeLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getNodeLink();
	}

	/**
	 * ��������� ����� �����
	 */
	protected MapPhysicalLinkElement createPhysicalLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreatePhysicalLinkCommandAtomic cmd = new CreatePhysicalLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getLink();
	}
	
	/**
	 * ��������� ����� �����
	 */
	protected MapCablePathElement createCablePath(
			SchemeCableLink scl,
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateCablePathCommandAtomic cmd = new CreateCablePathCommandAtomic(scl, startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * ��������� ����� �����
	 */
	protected void removeCablePath(MapCablePathElement mcpe)
	{
		RemoveCablePathCommandAtomic cmd = new RemoveCablePathCommandAtomic(mcpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * ��������� ����� �����
	 */
	protected MapMeasurementPathElement createMeasurementPath(
			SchemePath path,
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateMeasurementPathCommandAtomic cmd = new CreateMeasurementPathCommandAtomic(path, startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * ��������� ����� �����
	 */
	protected void removeMeasurementPath(MapMeasurementPathElement mpe)
	{
		RemoveMeasurementPathCommandAtomic cmd = new RemoveMeasurementPathCommandAtomic(mpe);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * ��������� ����� �����
	 */
	protected MapUnboundLinkElement createUnboundLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		CreateUnboundLinkCommandAtomic cmd = new CreateUnboundLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
		return cmd.getLink();
	}
	
	protected MapUnboundLinkElement createUnboundLinkWithNodeLink(
			MapNodeElement startNode,
			MapNodeElement endNode)
	{
		MapUnboundLinkElement unbound = this.createUnboundLink(startNode, endNode);

		MapNodeLinkElement nodeLink = this.createNodeLink(startNode, endNode);
		unbound.addNodeLink(nodeLink);
		nodeLink.setPhysicalLinkId(unbound.getId());
		
		return unbound;
	}

	
	/**
	 * ��������� ����� �����
	 */
	protected void removePhysicalLink(MapPhysicalLinkElement mple)
	{
		RemovePhysicalLinkCommandAtomic cmd = new RemovePhysicalLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * ��������� ����� �����
	 */
	protected void removeNodeLink(MapNodeLinkElement mple)
	{
		RemoveNodeLinkCommandAtomic cmd = new RemoveNodeLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * ��������� ����� �����
	 */
	protected void removeNode(MapNodeElement mne)
	{
		RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(mne);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}

	/**
	 * ��������� ������� ��������������� ����
	 */
	protected void changePhysicalNodeActivity(MapPhysicalNodeElement mpne, boolean active)
	{
		ChangePhysicalNodeActivityCommandAtomic cmd = new ChangePhysicalNodeActivityCommandAtomic(mpne, active);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
		add(cmd);
	}
	
	/**
	 * �������� ��������� �������, �������� ����������
	 */
	protected void registerStateChange(MapElement me, MapElementState initialState, MapElementState finalState)
	{
		MapElementStateChangeCommand cmd = new MapElementStateChangeCommand(me, initialState, finalState);
		cmd.setLogicalNetLayer(logicalNetLayer);
		add(cmd);
	}

	protected void removeUnboundLink(MapUnboundLinkElement link)
	{
		this.removePhysicalLink(link);
		link.sortNodes();
		List sortedNodes = new LinkedList();
		sortedNodes.addAll(link.getSortedNodes());
		for(Iterator it2 = sortedNodes.iterator(); it2.hasNext();)
		{
			MapNodeElement node = (MapNodeElement )it2.next();
			if(node instanceof MapPhysicalNodeElement)
				this.removeNode(node);
		}
		List sortedNodeLinks = new LinkedList();
		sortedNodeLinks.addAll(link.getNodeLinks());
		for(Iterator it3 = sortedNodeLinks.iterator(); it3.hasNext();)
		{
			this.removeNodeLink((MapNodeLinkElement )it3.next());
		}
	}

	protected void removeCablePathLinks(MapCablePathElement cablePath)
	{
		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link instanceof MapUnboundLinkElement)
			{
				this.removeUnboundLink((MapUnboundLinkElement )link);
			}
			else
			{
				link.getBinding().remove(cablePath);
			}
		}
		cablePath.clearLinks();
	}

	protected void removeMeasurementPathCables(MapMeasurementPathElement mPath)
	{
		mPath.clearCablePaths();
	}


	/**
	 * ��������� ����� link ����������� � ����� newLink ������� �� ����������
	 * ���������, ���� �� ���������� ���� firstConditionalNodeExit ���
	 * secondConditionalNodeExit. ������������ ����������� ����.
	 * 
	 * @param link
	 * @param newLink
	 * @param registerStateChange
	 * @param firstConditionalNodeExit
	 * @param secondConditionalNodeExit
	 * @return 
	 */
	protected MapNodeElement moveNodeLinks(
		MapPhysicalLinkElement link, 
		MapPhysicalLinkElement newLink,
		boolean registerStateChange,
		MapNodeElement firstConditionalNodeExit,
		MapNodeElement secondConditionalNodeExit)
	{
		MapNodeElement foundNode = null;
		MapElementState state = null;
		
		// ���������� ��������� ���� � ��������� �������� ���������� �����
		MapNodeLinkElement startNodeLink = link.getStartNodeLink();
		MapNodeElement startNode = link.getStartNode();

		// ������� ���� �� ���������� ����� - ������������ ��������� � ����� 
		// ���������� �����. �������� �� ���������� �� ������� ���� ��
		// ��������� �� ��������, �������� � ���������
		for(;;)
		{
			// ���������� �������� � ����� �����
			link.removeNodeLink(startNodeLink);
			newLink.addNodeLink(startNodeLink);
			
			if(registerStateChange)
				state = startNodeLink.getState();
			
			startNodeLink.setPhysicalLinkId(newLink.getId());
			
			if(registerStateChange)
				this.registerStateChange(
					startNodeLink, 
					state, 
					startNodeLink.getState());
			
			// ������� � ���������� ����
			startNode = startNodeLink.getOtherNode(startNode);
			
			// ���� ���������� �� ������ ����� �����, �� ��������
			// �������� ���� � ���������
			if(startNode.equals(firstConditionalNodeExit))
			{
				foundNode = firstConditionalNodeExit;
				newLink.setEndNode(firstConditionalNodeExit);
				break;
			}
			else
			if(startNode.equals(secondConditionalNodeExit))
			{
				foundNode = secondConditionalNodeExit;
				newLink.setEndNode(secondConditionalNodeExit);
				break;
			}

			startNodeLink = startNode.getOtherNodeLink(startNodeLink);
		}//for(;;)
		
		return foundNode;
	}
}
