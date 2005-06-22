/**
 * $Id: MapActionCommandBundle.java,v 1.24 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.CommandBundle;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.24 $, $Date: 2005/06/22 08:43:47 $
 * @module maviewclient_v1
 */
public class MapActionCommandBundle extends CommandBundle
{
	protected Throwable exception = null;

	LogicalNetLayer logicalNetLayer = null;
	ApplicationContext aContext = null;
	
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	protected ApplicationContext getContext()
	{
		return this.aContext;
	}
	
	protected LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			this.logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationContext"))
			this.aContext = (ApplicationContext )value;
		super.setParameter(field, value);
	}
	
	/**
	 * ��������� ������� ����
	 */
	protected SiteNode createSite(DoublePoint point, SiteNodeType proto)
		throws Throwable
	{
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(proto, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getSite();
	}

	/**
	 * ��������� ������������� �������
	 */
	protected UnboundNode createUnboundNode(DoublePoint point, SchemeElement se)
		throws Throwable
	{
		CreateUnboundNodeCommandAtomic cmd = new CreateUnboundNodeCommandAtomic(se, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getUnbound();
	}

	/**
	 * ��������� �������������� �������� ���� � ���������� ���������
	 */
	protected TopologicalNode createPhysicalNode(PhysicalLink physicalLink, DoublePoint point)
		throws Throwable
	{
		CreatePhysicalNodeCommandAtomic cmd = new CreatePhysicalNodeCommandAtomic(physicalLink, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getNode();
	}

	/**
	 * ��������� �������� �����, �� ���������� �� � ����� �����
	 */
	protected NodeLink createNodeLink(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode)
		throws Throwable
	{
		CreateNodeLinkCommandAtomic cmd = new CreateNodeLinkCommandAtomic(physicalLink, startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getNodeLink();
	}

	/**
	 * ��������� ������� ����� �����
	 */
	protected PhysicalLink createPhysicalLink(
			AbstractNode startNode,
			AbstractNode endNode)
		throws Throwable
	{
		CreatePhysicalLinkCommandAtomic cmd = new CreatePhysicalLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getLink();
	}
	
	/**
	 * ��������� ��������� ����
	 */
	protected CablePath createCablePath(
			SchemeCableLink schemeCableLink,
			AbstractNode startNode,
			AbstractNode endNode)
		throws Throwable
	{
		CreateCablePathCommandAtomic cmd = new CreateCablePathCommandAtomic(schemeCableLink, startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * ��������� ��������� ����
	 */
	protected void removeCablePath(CablePath mcpe)
		throws Throwable
	{
		RemoveCablePathCommandAtomic cmd = new RemoveCablePathCommandAtomic(mcpe);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
	}

	/**
	 * ��������� ������������� ����
	 */
	protected MeasurementPath createMeasurementPath(
			SchemePath schemePath,
			AbstractNode startNode,
			AbstractNode endNode)
		throws Throwable
	{
		CreateMeasurementPathCommandAtomic cmd = new CreateMeasurementPathCommandAtomic(schemePath, startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * ��������� ������������� ����
	 */
	protected void removeMeasurementPath(MeasurementPath mpe)
		throws Throwable
	{
		RemoveMeasurementPathCommandAtomic cmd = new RemoveMeasurementPathCommandAtomic(mpe);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
	}

	/**
	 * ��������� ������������� �����
	 */
	protected UnboundLink createUnboundLink(
			AbstractNode startNode,
			AbstractNode endNode)
		throws Throwable
	{
		CreateUnboundLinkCommandAtomic cmd = new CreateUnboundLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
		return cmd.getLink();
	}
	
	/**
	 * ��������� ������������� �����, ��������� �� ������ ���������
	 */
	protected UnboundLink createUnboundLinkWithNodeLink(
			AbstractNode startNode,
			AbstractNode endNode)
		throws Throwable
	{
		UnboundLink unbound = this.createUnboundLink(startNode, endNode);

		NodeLink nodeLink = this.createNodeLink(unbound, startNode, endNode);
		unbound.addNodeLink(nodeLink);
		nodeLink.setPhysicalLink(unbound);
		
		return unbound;
	}

	
	/**
	 * ��������� ����� �����
	 */
	protected void removePhysicalLink(PhysicalLink mple)
		throws Throwable
	{
		RemovePhysicalLinkCommandAtomic cmd = new RemovePhysicalLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
	}

	/**
	 * ��������� �������� �����
	 */
	protected void removeNodeLink(NodeLink mple)
		throws Throwable
	{
		RemoveNodeLinkCommandAtomic cmd = new RemoveNodeLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
	}

	/**
	 * ��������� �������� ������
	 */
	protected void removeNode(AbstractNode mne)
		throws Throwable
	{
		RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(mne);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
	}

	/**
	 * ��������� ������� ��������������� ����
	 */
	protected void changePhysicalNodeActivity(TopologicalNode mpne, boolean active)
		throws Throwable
	{
		ChangePhysicalNodeActivityCommandAtomic cmd = new ChangePhysicalNodeActivityCommandAtomic(mpne, active);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK)
			throw cmd.getException();
		add(cmd);
	}
	
	/**
	 * �������� ��������� �������, �������� ����������
	 */
	protected void registerStateChange(MapElement me, MapElementState initialState, MapElementState finalState)
		throws Throwable
	{
		MapElementStateChangeCommand cmd = new MapElementStateChangeCommand(me, initialState, finalState);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		add(cmd);
	}

	/**
	 * ��������� ������������� �����, ������� �� ����������
	 * �������������� ���� � ���������
	 */
	protected void removeUnboundLink(UnboundLink link)
		throws Throwable
	{
		this.removePhysicalLink(link);
		link.sortNodes();
		List sortedNodes = new LinkedList();
		sortedNodes.addAll(link.getSortedNodes());
		for(Iterator it2 = sortedNodes.iterator(); it2.hasNext();)
		{
			AbstractNode node = (AbstractNode)it2.next();
			if(node instanceof TopologicalNode)
				this.removeNode(node);
		}
		List sortedNodeLinks = new LinkedList();
		sortedNodeLinks.addAll(link.getNodeLinks());
		for(Iterator it3 = sortedNodeLinks.iterator(); it3.hasNext();)
		{
			this.removeNodeLink((NodeLink)it3.next());
		}
	}

	/**
	 * ��������� ���������� � �������� ������. �� �������� ������
	 * ������������, ������������� ����� ���������
	 */
	protected void removeCablePathLinks(CablePath cablePath)
		throws Throwable
	{
		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink)
			{
				this.removeUnboundLink((UnboundLink)link);
			}
			else
			{
				link.getBinding().remove(cablePath);
			}
		}
		cablePath.clearLinks();
	}

	/**
	 * ��������� ������������� ����
	 */
	protected void removeMeasurementPathCables(MeasurementPath mPath)
		throws Throwable
	{
//		mPath.clearCablePaths();
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
	 * @return ����
	 */
	protected AbstractNode moveNodeLinks(
			Map map,
			PhysicalLink link, 
			PhysicalLink newLink,
			boolean registerStateChange,
			AbstractNode firstConditionalNodeExit,
			AbstractNode secondConditionalNodeExit)
		throws Throwable
	{
		AbstractNode foundNode = null;
		MapElementState state = null;
		
		// ���������� ��������� ���� � ��������� �������� ���������� �����
		NodeLink startNodeLink = link.getStartNodeLink();
		AbstractNode startNode = link.getStartNode();

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
			
			startNodeLink.setPhysicalLink(newLink);
			
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

			startNodeLink = map.getOtherNodeLink(startNode, startNodeLink);
		}//for(;;)
		
		return foundNode;
	}

	public Throwable getException()
	{
		return this.exception;
	}
	
	public void setException(Throwable exception)
	{
		this.exception = exception;
	}
}
