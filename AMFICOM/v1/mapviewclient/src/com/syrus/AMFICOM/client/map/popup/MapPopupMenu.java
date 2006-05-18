/*-
 * $$Id: MapPopupMenu.java,v 1.60 2005/10/11 08:56:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedComboChooserDialog;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.action.BindUnboundNodeToSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.CreateCollectorCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateNodeLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreatePhysicalLinkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.CreateSiteCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.DeleteSelectionCommand;
import com.syrus.AMFICOM.client.map.command.action.GenerateCablePathCablingCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.GenerateUnboundLinkCablingCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.InsertSiteCommandBundle;
import com.syrus.AMFICOM.client.map.command.action.MapElementStateChangeCommand;
import com.syrus.AMFICOM.client.map.command.action.RemoveCollectorCommandAtomic;
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Контекстное меню элемента карты
 * 
 * @version $Revision: 1.60 $, $Date: 2005/10/11 08:56:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class MapPopupMenu extends JPopupMenu {

	protected Point point;
	protected NetMapViewer netMapViewer;

	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return this.point;
	}

	public abstract void setElement(Object object);

	protected Collector selectCollector() {
		Collection list = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getAllCollectors();
		return (Collector )WrapperedComboChooserDialog.showChooserDialog(list);
	}
	
	protected SiteNodeType selectSiteNodeType() {
		Object toSelect = null;
		Collection list = NodeTypeController.getTopologicalNodeTypes(this.netMapViewer.getLogicalNetLayer().getMapView().getMap());
		Iterator listIt = list.iterator();
		if (listIt.hasNext())
			toSelect = listIt.next();

		return (SiteNodeType )WrapperedComboChooserDialog.showNameChooserDialog(list, toSelect);
	}

	protected SiteNodeType selectAttachedSiteNodeType() {
		Object toSelect = null;
		Collection<SiteNodeType> list = NodeTypeController.getCableInletTypes();
		for(SiteNodeType type : new LinkedList<SiteNodeType>(list)) {
			if(type.getSort().value() != SiteNodeTypeSort._CABLE_INLET) {
				list.remove(type);
			}
		}
		Iterator listIt = list.iterator();
		if (listIt.hasNext())
			toSelect = listIt.next();

		return (SiteNodeType )WrapperedComboChooserDialog.showNameChooserDialog(list, toSelect);
	}

	protected SiteNode selectSiteNode() {
		List list = new LinkedList();
		for(Iterator it = this.netMapViewer.getLogicalNetLayer().getMapView().getMap()
				.getAllSiteNodes().iterator(); it.hasNext();) {
			SiteNode s = (SiteNode)it.next();
			if(!( s instanceof UnboundNode))
				list.add(s);
		}
		
		return (SiteNode )WrapperedComboChooserDialog.showChooserDialog(list);
	}

	protected PhysicalLink selectPhysicalLinkAt(UnboundLink unbound) {
		Map map = this.netMapViewer.getLogicalNetLayer().getMapView().getMap();
		
		AbstractNode node1 = unbound.getStartNode();
		AbstractNode node2 = unbound.getEndNode();

		List list = new LinkedList();

		// select physical links that connect same end nodes as link
		Collection list2 = map.getPhysicalLinksAt(node1);
		for(Iterator it = map.getPhysicalLinksAt(node2).iterator(); it.hasNext();) {
			PhysicalLink le = (PhysicalLink)it.next();
			if(! (le instanceof UnboundLink))
				if(list2.contains(le))
					list.add(le);
		}

		return (PhysicalLink )WrapperedComboChooserDialog.showChooserDialog(list);
	}

	protected Collector createCollector() {
		String inputValue = JOptionPane.showInputDialog(
				Environment.getActiveWindow(), 
				I18N.getString(MapEditorResourceKeys.MESSAGE_ENTER_COLLECTOR_NAME),
				I18N.getString(MapEditorResourceKeys.VALUE_DEFAULT_COLLECTOR_NAME));
		if(inputValue != null) {
			CreateCollectorCommandAtomic command = new CreateCollectorCommandAtomic(inputValue);
			LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
			command.setLogicalNetLayer(logicalNetLayer);
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			
			return command.getCollector();
		}
		
		return null;
	}
	
	protected void addLinksToCollector(Collector collector, Set links) throws ApplicationException {
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();

			addLinkToCollector(collector, link);
		}
	}

	protected void addLinkToCollector(Collector collector, PhysicalLink mple) throws ApplicationException {
		PhysicalLinkType collectorType = LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_COLLECTOR);

		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		Collector prevCollector = logicalNetLayer.getMapView().getMap().getCollector(mple);
		if(prevCollector != null)
			prevCollector.removePhysicalLink(mple);
	
		collector.addPhysicalLink(mple);

		MapElementState state = mple.getState();
		mple.setType(collectorType);

		MapElementStateChangeCommand command2 = new MapElementStateChangeCommand(mple, state, mple.getState());
		command2.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command2);
		logicalNetLayer.getCommandList().execute();
	}

	protected void removeLinksFromCollector(Collector collector, Set links) throws ApplicationException {
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink mple = (PhysicalLink )it.next();

			removeLinkFromCollector(collector, mple);
		}
	}

	protected void removeLinkFromCollector(
			Collector collector,
			PhysicalLink mple) throws ApplicationException {
		collector.removePhysicalLink(mple);

		MapElementState state = mple.getState();

		mple.setType(LinkTypeController.getPhysicalLinkType(PhysicalLinkType.DEFAULT_TUNNEL));

		MapElementStateChangeCommand command = new MapElementStateChangeCommand(mple, state, mple.getState());
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		
		if(collector.getPhysicalLinks().size() == 0)
			removeCollector(collector);
	}
	
	protected void removeCollector(Collector collector) {
		RemoveCollectorCommandAtomic command = new RemoveCollectorCommandAtomic(collector);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
	}

	protected void removeMapElement(MapElement me) {
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		logicalNetLayer.deselectAll();
		logicalNetLayer.getMapView().getMap().setSelected(me, true);
		DeleteSelectionCommand command = new DeleteSelectionCommand();
		command.setNetMapViewer(this.netMapViewer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		if(!command.isUndoable()) {
			logicalNetLayer.getCommandList().flush();
		}
	}

	protected void insertSiteInPlaceOfANode(TopologicalNode node, SiteNodeType proto) {
		InsertSiteCommandBundle command = new InsertSiteCommandBundle(node, proto);
		command.setNetMapViewer(this.netMapViewer);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		if(!command.isUndoable()) {
			logicalNetLayer.getCommandList().flush();
		}
	}

	protected void convertUnboundNodeToSite(UnboundNode unbound, SiteNodeType proto) {
		if(unbound.isRemoved())
			return;

		CreateSiteCommandAtomic command = new CreateSiteCommandAtomic(proto, unbound.getLocation());
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		
		SiteNode site = command.getSite();

		BindUnboundNodeToSiteCommandBundle command2 = new BindUnboundNodeToSiteCommandBundle(unbound, site);
		command2.setNetMapViewer(this.netMapViewer);
		logicalNetLayer.getCommandList().add(command2);
		logicalNetLayer.getCommandList().execute();
		logicalNetLayer.getCommandList().flush();
	}

	protected void generatePathCabling(CablePath path, SiteNodeType proto) {
		GenerateCablePathCablingCommandBundle command = 
				new GenerateCablePathCablingCommandBundle(path, proto);
		command.setNetMapViewer(this.netMapViewer);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		logicalNetLayer.getCommandList().flush();
	}

	protected void convertUnboundLinkToPhysicalLink(UnboundLink unbound) {
		GenerateUnboundLinkCablingCommandBundle command = 
				new GenerateUnboundLinkCablingCommandBundle(unbound);
		command.setNetMapViewer(this.netMapViewer);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		logicalNetLayer.getCommandList().flush();
	}

	protected void createAttachedSiteNode(SiteNode siteNode, SiteNodeType attachedSiteNodeType) throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.netMapViewer.getLogicalNetLayer().getConverter();
		Point point = converter.convertMapToScreen(siteNode.getLocation());
		if(point.x > 30) {
			point.x -= 20;
		}
		else {
			point.x += 20;
		}
		SiteNode attachedSiteNode = this.createSiteNode(attachedSiteNodeType, point);
		attachedSiteNode.setAttachmentSiteNode(siteNode);
		PhysicalLink physicalLink = this.createPhysicalLink(siteNode, attachedSiteNode);
		physicalLink.setType(LinkTypeController.getIndoorLinkType());
		this.createNodeLink(physicalLink, siteNode, attachedSiteNode);
	}

	protected SiteNode createSiteNode(SiteNodeType siteNodeType, DoublePoint doublePoint) {
		CreateSiteCommandAtomic command = 
			new CreateSiteCommandAtomic(siteNodeType, doublePoint);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		return command.getSite();
	}

	protected SiteNode createSiteNode(SiteNodeType siteNodeType, Point point) {
		CreateSiteCommandAtomic command = 
			new CreateSiteCommandAtomic(siteNodeType, point);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		return command.getSite();
	}

	protected void createNodeLink(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode) {
		CreateNodeLinkCommandAtomic command = new CreateNodeLinkCommandAtomic(physicalLink, startNode, endNode);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		NodeLink nodeLink = command.getNodeLink();
		physicalLink.addNodeLink(nodeLink);
	}

	protected PhysicalLink createPhysicalLink( AbstractNode startNode, AbstractNode endNode) {
		CreatePhysicalLinkCommandAtomic command = new CreatePhysicalLinkCommandAtomic(startNode, endNode);
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		command.setLogicalNetLayer(logicalNetLayer);
		logicalNetLayer.getCommandList().add(command);
		logicalNetLayer.getCommandList().execute();
		return command.getLink();
	}
}
