/**
 * $Id: CreateNodeLinkCommandBundle.java,v 1.29 2005/08/26 15:39:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * �������� ��������� �����, ������� �������� �� 
 * ������������������ ��������� ��������. ������������� ��������� ���������
 * �������������: ������������ �������� �� ���� ����� �������, �������
 * ������ � ������ ����� � ���������. � ������ ���������� ����������� 
 * ������ �������
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.29 $, $Date: 2005/08/26 15:39:54 $
 * @module mapviewclient
 */
public class CreateNodeLinkCommandBundle extends MapActionCommandBundle {
	/**
	 * ��������� ������������ ��� �������� � ������� ��������� (��������
	 * ����� ��� ���������)
	 */
	public static final String END_POINT = "endpoint";

	/** ��������� ���� ��������� */
	AbstractNode startNode;
	
	Map map;
	
	/** �����, � ������� ������ ���� �������� ���� ��������� */
	Point endPoint;

	private PhysicalLink physicalLink;

	private NodeLink nodeLink;

	/**
	 * 
	 * @param startNode
	 */
	public CreateNodeLinkCommandBundle(AbstractNode startNode) {
		super();
		this.startNode = startNode;
	}

	@Override
	public void setParameter(String field, Object value) {
		if(field.equals(END_POINT)) {
			this.endPoint = (Point) value;
		}
	}

	void createSiteToSite(AbstractNode endNode) throws Throwable {
		// ��������� ����� ���������� ����� �� ������ ���������
		this.physicalLink = super.createPhysicalLink(this.startNode, endNode);
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);
	}

	void createSiteToNewNode(DoublePoint mapEndPoint) throws Throwable {
		this.physicalLink = super.createPhysicalLink(this.startNode, this.startNode);
		AbstractNode endNode = super.createPhysicalNode(this.physicalLink, mapEndPoint);
		this.physicalLink.setEndNode(endNode);
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);
	}

	void createSiteToNode(TopologicalNode endNode) throws Throwable {
		this.physicalLink = endNode.getPhysicalLink();
		MapElementState pls = this.physicalLink.getState();
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);
		super.changePhysicalNodeActivity(endNode, true);

		// ��������� ���������� � ��������� ����� �����
		if(this.physicalLink.getEndNode().equals(endNode))
			this.physicalLink.setEndNode(this.physicalLink.getStartNode());
		this.physicalLink.setStartNode(this.startNode);
		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	void createNodeToSite(SiteNode endNode) throws Throwable {
		// ������������ ���������� ����� ����������� �� ���� site
		TopologicalNode topologicalNode = (TopologicalNode )this.startNode;
		this.physicalLink = topologicalNode.getPhysicalLink();
		MapElementState pls = this.physicalLink.getState();
		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);

		super.changePhysicalNodeActivity(topologicalNode, true);

		// ��������� ���������� � ��������� ����� �����
		if(this.physicalLink.getStartNode().equals(this.startNode))
			this.physicalLink.setStartNode(this.physicalLink.getEndNode());
		this.physicalLink.setEndNode(endNode);
		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	void createNodeToNewNode(DoublePoint mapEndPoint) throws Throwable {
		TopologicalNode startTopologicalNode = (TopologicalNode )this.startNode;

		this.physicalLink = startTopologicalNode.getPhysicalLink();

		MapElementState pls = this.physicalLink.getState();

		TopologicalNode endNode = super.createPhysicalNode(this.physicalLink, mapEndPoint);

		this.nodeLink = super.createNodeLink(this.physicalLink, this.startNode, endNode);

		super.changePhysicalNodeActivity(startTopologicalNode, true);

		// ��������� ���������� � ��������� ����� �����
		if(this.physicalLink.getStartNode().equals(this.startNode))
			this.physicalLink.setStartNode(this.physicalLink.getEndNode());
		this.physicalLink.setEndNode(endNode);

		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	void createNodeToNode(TopologicalNode endNode) throws Throwable {
		TopologicalNode startTopologicalNode = (TopologicalNode )this.startNode;
		TopologicalNode endTopologicalNode = endNode;

		this.physicalLink = startTopologicalNode.getPhysicalLink();
		PhysicalLink physicalLinkToRemove = endTopologicalNode.getPhysicalLink();

		MapElementState pls = this.physicalLink.getState();

		this.nodeLink = super.createNodeLink(this.physicalLink, startTopologicalNode, endTopologicalNode);

		super.changePhysicalNodeActivity(startTopologicalNode, true);
		super.changePhysicalNodeActivity(endTopologicalNode, true);

		// ��������� ���������� � ��������� ����� �����
		if(this.physicalLink.getStartNode().equals(this.startNode))
			this.physicalLink.setStartNode(this.physicalLink.getEndNode());
		this.physicalLink.setEndNode(endNode);

		if(physicalLinkToRemove != null
				&& !physicalLinkToRemove.equals(this.physicalLink)) {
			MapElementState pls2 = physicalLinkToRemove.getState();
	
			// ��������� ������, ��������� ������������ �������� ��������
			LinkedList nodeLinksToMove = new LinkedList();
			nodeLinksToMove.addAll(physicalLinkToRemove.getNodeLinks());
	
			// ������� ���������� ����� �� ����� ����� � ������
			for(Iterator it = nodeLinksToMove.iterator(); it.hasNext();) {
				NodeLink mnle = (NodeLink)it.next();
				mnle.setPhysicalLink(this.physicalLink);
			}			
		
			// ��������� ���������� � ��������� ����� �����
			if(physicalLinkToRemove.getStartNode().equals(endNode))
				this.physicalLink.setEndNode(physicalLinkToRemove.getEndNode());
			else
				this.physicalLink.setEndNode(physicalLinkToRemove.getStartNode());
	
			super.registerStateChange(physicalLinkToRemove, pls2, physicalLinkToRemove.getState());
	
			super.removePhysicalLink(physicalLinkToRemove);
		}

		super.registerStateChange(this.physicalLink, pls, this.physicalLink.getState());
	}

	@Override
	public void execute() {
		try {
			Log.debugMessage(
				getClass().getName() + "::execute() | "
					+ "create node link from node " + this.startNode.getName()
					+ " (" + this.startNode.getId() + ")", 
				Level.FINEST);
			// ������������� ������� � �����, � ������� �������� �����		
			MapElement curElementAtPoint = this.logicalNetLayer.getMapElementAtPoint(this.endPoint, this.netMapViewer.getVisibleBounds());
			// ���� ����� �������� �� ��� �� ��������, �� ����� �� ��������
			if(curElementAtPoint.equals(this.startNode))
				return;
			this.map = this.logicalNetLayer.getMapView().getMap();
			AbstractNode endNode = null;
			DoublePoint mapEndPoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.endPoint);
			// ���� � �������� ����� ��� ���� �������, ���������, ����� ��� ����
			if ( curElementAtPoint != null
				&& curElementAtPoint instanceof AbstractNode) {
				endNode = (AbstractNode )curElementAtPoint;

				// �������� ������� - �������������� ����
				if(endNode instanceof TopologicalNode) {
					TopologicalNode mpne = (TopologicalNode )endNode;
			
					// ���� �� ��������, �� ���� ��������� � �������� ������ �����,
					// �� � ��� �� ����� ��������� �����
					if(mpne.isActive()) {
						// node created with fake this.physicalLink
						// should be later updated (e.g. through call to
						// nodelink.setPhysicalLink or node.setPhysicalLink)
						endNode = null;
//					endNode = super.createPhysicalNode(mpne.getPhysicalLink(), mapEndPoint);
					}
					else {
						// ���� �� - �������� ��� �����, �� �������� ����� ��������
						// �� ���� (��������� ������������ ����� ��� �����������
						// ���������� �����)
						super.changePhysicalNodeActivity(mpne, true);
					}
				}
			}
			else {
				// ���� � �������� ����� ��� ��������, �� ������� ����� �������� 
				// �������������� ����
				endNode = null;
//			endNode = super.createPhysicalNode(mapEndPoint);
			}
			if(endNode == null) {
				if (this.startNode instanceof SiteNode) {
					createSiteToNewNode(mapEndPoint);
				}
				else if (this.startNode instanceof TopologicalNode) {
					createNodeToNewNode(mapEndPoint);
				}
			}
			else {
				if (this.startNode instanceof SiteNode 
					&& endNode instanceof SiteNode ) {
					createSiteToSite(endNode);
				}
				else if ( this.startNode instanceof SiteNode
					&& endNode instanceof TopologicalNode ) {
					createSiteToNode((TopologicalNode )endNode);
				}
				else if ( this.startNode instanceof TopologicalNode
					&& endNode instanceof SiteNode ) {
					createNodeToSite((SiteNode )endNode);
				}
				else if ( this.startNode instanceof TopologicalNode 
					&& endNode instanceof TopologicalNode ) {
					createNodeToNode((TopologicalNode )endNode);
				}
			}
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
