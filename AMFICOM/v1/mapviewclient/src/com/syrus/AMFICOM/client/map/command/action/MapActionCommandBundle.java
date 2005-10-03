/*-
 * $$Id: MapActionCommandBundle.java,v 1.36 2005/10/03 10:35:00 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.CommandBundle;
import com.syrus.AMFICOM.map.AbstractNode;
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
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * 
 * @version $Revision: 1.36 $, $Date: 2005/10/03 10:35:00 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapActionCommandBundle extends CommandBundle {
	protected Throwable exception = null;

	LogicalNetLayer logicalNetLayer = null;
	ApplicationContext aContext = null;

	protected NetMapViewer netMapViewer;

	protected boolean undoable = true;

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		this.aContext = this.logicalNetLayer.getContext();
	}

	protected ApplicationContext getContext() {
		return this.aContext;
	}

	/**
	 * ��������� ������� ����
	 */
	protected SiteNode createSite(DoublePoint point, SiteNodeType proto)
			throws Throwable {
		CreateSiteCommandAtomic cmd = new CreateSiteCommandAtomic(proto, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
		return cmd.getSite();
	}

	/**
	 * ��������� ������������� �������
	 */
	protected UnboundNode createUnboundNode(DoublePoint point, SchemeElement se)
			throws Throwable {
		CreateUnboundNodeCommandAtomic cmd = new CreateUnboundNodeCommandAtomic(se, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
		return cmd.getUnbound();
	}

	/**
	 * ��������� �������������� �������� ���� � ���������� ���������
	 */
	protected TopologicalNode createPhysicalNode(PhysicalLink physicalLink, DoublePoint point)
			throws Throwable {
		CreatePhysicalNodeCommandAtomic cmd = new CreatePhysicalNodeCommandAtomic(physicalLink, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
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
			throws Throwable {
		CreateNodeLinkCommandAtomic cmd = new CreateNodeLinkCommandAtomic(physicalLink, startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
		NodeLink nodeLink = cmd.getNodeLink();
		physicalLink.addNodeLink(nodeLink);
		return nodeLink;
	}

	/**
	 * ��������� ������� ����� �����
	 */
	protected PhysicalLink createPhysicalLink(
			AbstractNode startNode,
			AbstractNode endNode)
			throws Throwable {
		CreatePhysicalLinkCommandAtomic cmd = new CreatePhysicalLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
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
			throws Throwable {
		CreateCablePathCommandAtomic cmd = new CreateCablePathCommandAtomic(schemeCableLink, startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * ��������� ��������� ����
	 */
	protected void removeCablePath(CablePath cablePath)
			throws Throwable {
		RemoveCablePathCommandAtomic cmd = new RemoveCablePathCommandAtomic(cablePath);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
	}

	/**
	 * ��������� ������������� ����
	 */
	protected MeasurementPath createMeasurementPath(
			SchemePath schemePath,
			AbstractNode startNode,
			AbstractNode endNode)
			throws Throwable {
		CreateMeasurementPathCommandAtomic cmd = new CreateMeasurementPathCommandAtomic(schemePath, startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
		return cmd.getPath();
	}
	
	/**
	 * ��������� ������������� ����
	 */
	protected void removeMeasurementPath(MeasurementPath mpe)
			throws Throwable {
		RemoveMeasurementPathCommandAtomic cmd = new RemoveMeasurementPathCommandAtomic(mpe);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
	}

	/**
	 * ��������� ������������� �����
	 */
	protected UnboundLink createUnboundLink(
			AbstractNode startNode,
			AbstractNode endNode)
			throws Throwable {
		CreateUnboundLinkCommandAtomic cmd = new CreateUnboundLinkCommandAtomic(startNode, endNode);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
		return cmd.getLink();
	}
	
	/**
	 * ��������� ������������� �����, ��������� �� ������ ���������
	 */
	protected UnboundLink createUnboundLinkWithNodeLink(
			AbstractNode startNode,
			AbstractNode endNode)
			throws Throwable {
		UnboundLink unbound = this.createUnboundLink(startNode, endNode);

		NodeLink nodeLink = this.createNodeLink(unbound, startNode, endNode);
		
		return unbound;
	}

	
	/**
	 * ��������� ����� �����
	 */
	protected void removePhysicalLink(PhysicalLink mple)
			throws Throwable {
		RemovePhysicalLinkCommandAtomic cmd = new RemovePhysicalLinkCommandAtomic(mple);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
	}

	/**
	 * ��������� �������� �����
	 */
	protected void removeNodeLink(NodeLink nodeLink)
			throws Throwable {
		RemoveNodeLinkCommandAtomic cmd = new RemoveNodeLinkCommandAtomic(nodeLink);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
	}

	/**
	 * ��������� �������� ������
	 */
	protected void removeNode(AbstractNode mne)
			throws Throwable {
		RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(mne);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
	}

	/**
	 * ��������� ������� ��������������� ����
	 */
	protected void changePhysicalNodeActivity(TopologicalNode mpne, boolean active)
			throws Throwable {
		ChangePhysicalNodeActivityCommandAtomic cmd = new ChangePhysicalNodeActivityCommandAtomic(mpne, active);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
		if(cmd.getResult() != Command.RESULT_OK) {
			throw cmd.getException();
		}
		add(cmd);
	}
	
	/**
	 * �������� ��������� �������, �������� ����������
	 */
	protected void registerStateChange(MapElement me, MapElementState initialState, MapElementState finalState)
			throws Throwable {
		MapElementStateChangeCommand cmd = new MapElementStateChangeCommand(me, initialState, finalState);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		add(cmd);
	}

	/**
	 * ��������� ������������� �����, ������� �� ����������
	 * �������������� ���� � ���������
	 */
	protected void removeUnboundLink(UnboundLink link)
			throws Throwable {
		link.sortNodes();
		List sortedNodes = new LinkedList();
		sortedNodes.addAll(link.getSortedNodes());
		for(Iterator it2 = sortedNodes.iterator(); it2.hasNext();) {
			AbstractNode node = (AbstractNode)it2.next();
			if(node instanceof TopologicalNode) {
				this.removeNode(node);
			}
		}
		List<NodeLink> sortedNodeLinks = new LinkedList<NodeLink>();
		sortedNodeLinks.addAll(link.getNodeLinks());
		for(NodeLink nodeLink : sortedNodeLinks) {
			this.removeNodeLink(nodeLink);
		}
		this.removePhysicalLink(link);
	}

	/**
	 * ��������� ���������� � �������� ������. �� �������� ������
	 * ������������, ������������� ����� ���������
	 */
	protected void removeCablePathLinks(CablePath cablePath)
			throws Throwable {
		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink) {
				this.removeUnboundLink((UnboundLink)link);
			}
			else {
				link.getBinding().remove(cablePath);
			}
		}
		cablePath.clearLinks();
		setUndoable(false);
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
			throws Throwable {
		AbstractNode foundNode = null;
		MapElementState state = null;
		
		// ���������� ��������� ���� � ��������� �������� ���������� �����
		NodeLink startNodeLink = link.getStartNodeLink();
		AbstractNode startNode = link.getStartNode();

		// ������� ���� �� ���������� ����� - ������������ ��������� � ����� 
		// ���������� �����. �������� �� ���������� �� ������� ���� ��
		// ��������� �� ��������, �������� � ���������
		for(;;) {
			if(registerStateChange) {
				state = startNodeLink.getState();
			}

			link.removeNodeLink(startNodeLink);
			startNodeLink.setPhysicalLink(newLink);
			newLink.addNodeLink(startNodeLink);
			
			if(registerStateChange) {
				this.registerStateChange(
					startNodeLink, 
					state, 
					startNodeLink.getState());
			}
			
			// ������� � ���������� ����
			startNode = startNodeLink.getOtherNode(startNode);
			
			// ���� ���������� �� ������ ����� �����, �� ��������
			// �������� ���� � ���������
			if(startNode.equals(firstConditionalNodeExit)) {
				foundNode = firstConditionalNodeExit;
				newLink.setEndNode(firstConditionalNodeExit);
				break;
			}
			else if(startNode.equals(secondConditionalNodeExit)) {
				foundNode = secondConditionalNodeExit;
				newLink.setEndNode(secondConditionalNodeExit);
				break;
			}

			startNodeLink = map.getOtherNodeLink(startNode, startNodeLink);
		}//for(;;)
		
		return foundNode;
	}

	public Throwable getException() {
		return this.exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public boolean isUndoable() {
		for(Command command : this.commands) {
			if(command instanceof MapActionCommand) {
				MapActionCommand mapActionCommand = (MapActionCommand) command;
				if(!mapActionCommand.isUndoable()) {
					this.undoable = false;
				}
			}
			if(command instanceof MapActionCommandBundle) {
				MapActionCommandBundle mapActionCommandBundle = (MapActionCommandBundle) command;
				if(!mapActionCommandBundle.isUndoable()) {
					this.undoable = false;
				}
			}
		}
		return this.undoable;
	}

	protected void setUndoable(boolean undoable) {
		this.undoable = undoable;
	}

}
