/*-
 * $$Id: LinkPopupMenu.java,v 1.35 2006/06/23 14:13:38 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.command.action.CreateMarkCommandAtomic;
import com.syrus.AMFICOM.client.map.command.action.RemoveUnboundLinkCommandBundle;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.35 $, $Date: 2006/06/23 14:13:38 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class LinkPopupMenu extends MapPopupMenu {
	private static final long serialVersionUID = 201932651602063281L;
	private JMenuItem removeMenuItem = new JMenuItem();
	private JMenuItem addMarkMenuItem = new JMenuItem();
	private JMenuItem addToCollectorMenuItem = new JMenuItem();
	private JMenuItem removeFromCollectorMenuItem = new JMenuItem();
	private JMenuItem newCollectorMenuItem = new JMenuItem();
	private JMenuItem removeCollectorMenuItem = new JMenuItem();
	
	private JMenu bindCableMenuItem = new JMenu();

	private PhysicalLink link;
//	private CablePath cablePath;
	private Set<CablePath> unboundCablePaths = new HashSet<CablePath>();

	private static LinkPopupMenu instance;

	private LinkPopupMenu() {
		super();
		try {
			jbInit();
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public static LinkPopupMenu getInstance() {
		if (instance == null) {
			instance  = new LinkPopupMenu();
		}
		return instance;
	}

	@Override
	public void setElement(Object me) {
		this.link = (PhysicalLink) me;
		this.unboundCablePaths.clear();
		Collector collector = null;
		
		this.bindCableMenuItem.removeAll();
				
		final boolean editable = isEditable();
		
		if (editable) {
			final MapView mapView = this.netMapViewer.getLogicalNetLayer().getMapView();
			collector = mapView.getMap().getCollector(this.link);
			
			final ApplicationModel aModel = this.netMapViewer.getLogicalNetLayer().getContext().getApplicationModel();
			boolean schemeActionsEnabled = aModel.isSelected(MapApplicationModel.MODE_CABLE_PATH)
					|| aModel.isSelected(MapApplicationModel.MODE_LINK);
			
			if (schemeActionsEnabled) { 
				final AbstractNode startNode = this.link.getStartNode();
				final AbstractNode endNode = this.link.getEndNode();
								
				Set<CablePath> paths = new HashSet<CablePath>();
				paths.addAll(mapView.getCablePaths(startNode));		
				paths.addAll(mapView.getCablePaths(endNode));
				try {
					for (CablePath cablePath2 : paths) {
						SiteNode unboundStartNode = (SiteNode)cablePath2.getStartUnboundNode();
						if(unboundStartNode != null) {
							this.unboundCablePaths.add(cablePath2);
							continue;
						}
						SiteNode unboundEndNode = (SiteNode)cablePath2.getEndUnboundNode();
						if(unboundEndNode != null) {
							this.unboundCablePaths.add(cablePath2);
							continue;
						}
					}
				} catch (ApplicationException e) {
					Log.errorMessage(e);
				}
			}
		}
		
		this.addToCollectorMenuItem.setVisible(editable && collector == null);
		this.newCollectorMenuItem.setVisible(editable && collector == null);
		this.removeCollectorMenuItem.setVisible(collector != null);
		this.removeFromCollectorMenuItem.setVisible(collector != null);
		this.bindCableMenuItem.setVisible(!this.unboundCablePaths.isEmpty());
		
		this.addMarkMenuItem.setVisible(editable);
		
		if(collector != null) {
			this.removeCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR)
					+ " '" + collector.getName() + "'");  //$NON-NLS-1$//$NON-NLS-2$
			this.removeFromCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR)
					+ " '" + collector.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		for (final CablePath path : this.unboundCablePaths) {
			JMenuItem item = new JMenuItem(new AbstractAction() {
				private static final long serialVersionUID = 7864849928422747189L;
				public void actionPerformed(ActionEvent e) {
					bindCable(path);
				}
			});
			item.setText(path.getName());
			this.bindCableMenuItem.add(item);
		}
	}

	private void jbInit() {
		this.removeMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_DELETE));
		this.removeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLink();
			}
		});
		
		this.addMarkMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_MARK));
		this.addMarkMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addMark();
			}
		});
		
		this.newCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_CREATE_COLLECTOR));
		this.newCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					newCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});
		
		this.removeCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_COLLECTOR));
		this.removeCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});

		this.addToCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_ADD_TO_COLLECTOR));
		this.addToCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addToCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});
		
		this.removeFromCollectorMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_REMOVE_FROM_COLLECTOR));
		this.removeFromCollectorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					removeFromCollector();
				} catch(ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}
		});
		
		this.bindCableMenuItem.setText(I18N.getString(MapEditorResourceKeys.POPUP_BIND_CABLE));

		this.add(this.removeMenuItem);
		this.add(this.addMarkMenuItem);
		// this.addSeparator();
		this.add(this.addToCollectorMenuItem);
		this.add(this.removeFromCollectorMenuItem);
		// this.addSeparator();
		this.add(this.newCollectorMenuItem);
		this.add(this.removeCollectorMenuItem);
		
		this.add(this.bindCableMenuItem);
	}

	void removeLink() {
		if (confirmDelete()) {
			super.removeMapElement(this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addMark() {
		CreateMarkCommandAtomic command = new CreateMarkCommandAtomic(
				this.link,
				this.point);
		command.setLogicalNetLayer(this.netMapViewer.getLogicalNetLayer());
		this.netMapViewer.getLogicalNetLayer().getCommandList().add(command);
		this.netMapViewer.getLogicalNetLayer().getCommandList().execute();
		this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
	}

	void newCollector() throws ApplicationException {
		Collector collector = super.createCollector();
		if(collector != null) {
			super.addLinkToCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void addToCollector() throws ApplicationException {
		Collector collector = super.selectCollector();
		if(collector != null) {
			super.addLinkToCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeFromCollector() throws ApplicationException {
		Collector collector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(this.link);
		if(collector != null) {
			super.removeLinkFromCollector(collector, this.link);
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}

	void removeCollector() throws ApplicationException {
		this.netMapViewer.getLogicalNetLayer().deselectAll();

		Collector collector = this.netMapViewer.getLogicalNetLayer().getMapView().getMap().getCollector(this.link);
		if(collector != null) {
			Set<PhysicalLink> set = new HashSet<PhysicalLink>();
			set.addAll(collector.getPhysicalLinks());
			super.removeLinksFromCollector(collector, set);
			super.removeCollector(collector);
			this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
			this.netMapViewer.getLogicalNetLayer().sendMapEvent(MapEvent.MAP_CHANGED);
		}
	}
	
	void bindCable(CablePath cablePath) {
		try {
			final AbstractNode startUnboundNode = cablePath.getStartUnboundNode();
			final AbstractNode endUnboundNode = cablePath.getEndUnboundNode();
			if (startUnboundNode.equals(this.link.getStartNode())
					|| startUnboundNode.equals(this.link.getEndNode())) {
				addChainBinding(this.link, null, cablePath);
			} else if (endUnboundNode.equals(this.link.getStartNode())
					|| endUnboundNode.equals(this.link.getEndNode())) {
				addChainBinding(null, this.link, cablePath);
			} else {
				Log.debugMessage("Can't bind cable ", Level.FINEST);
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}		
	}
	
	void addChainBinding(PhysicalLink selectedStartLink, PhysicalLink selectedEndLink, CablePath cablePath) throws ApplicationException {
		final LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		final Map map = logicalNetLayer.getMapView().getMap();
		
		while(selectedStartLink != null || selectedEndLink != null) {
			addBinding(selectedStartLink, selectedEndLink, cablePath);

			final CableChannelingItem startLastBound = cablePath.getStartLastBoundLink();
			final AbstractNode startUnboundNode = cablePath.getStartUnboundNode();
			
			Set<PhysicalLink> availableLinksFromStart = Collections.emptySet();
			if (startUnboundNode != null) {
				availableLinksFromStart = map.getPhysicalLinksAt(startUnboundNode);
			}
			if(startLastBound != null) {
				availableLinksFromStart.remove(startLastBound.getPhysicalLink());
			}
			for (Iterator<PhysicalLink> it = availableLinksFromStart.iterator(); it.hasNext();) {
				if (it.next() instanceof UnboundLink) {
					it.remove();
				}
			}

			if(availableLinksFromStart.size() == 1) {
				selectedStartLink = availableLinksFromStart.iterator().next();
				continue;
			}
			selectedStartLink = null;
			
			final CableChannelingItem endLastBound = cablePath.getEndLastBoundLink();
			final AbstractNode endUnboundNode = cablePath.getEndUnboundNode();
			
			Set<PhysicalLink> availableLinksFromEnd = Collections.emptySet();
			if (endUnboundNode != null) {
				availableLinksFromEnd = map.getPhysicalLinksAt(endUnboundNode);
			}
			if(endLastBound != null) {
				availableLinksFromEnd.remove(endLastBound.getPhysicalLink());
			}
			for (Iterator<PhysicalLink> it = availableLinksFromEnd.iterator(); it.hasNext();) {
				if (it.next() instanceof UnboundLink) {
					it.remove();
				}
			}

			if(availableLinksFromEnd.size() == 1) {
				selectedEndLink = availableLinksFromEnd.iterator().next();
				continue;
			}
			selectedEndLink = null;
		}
		logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
	}
	
	private void addBinding(PhysicalLink fisrtLink, PhysicalLink lastLink, CablePath cablePath) throws ApplicationException {
		if(fisrtLink != null) {
			final CableChannelingItem startLastBound = cablePath.getStartLastBoundLink();
			final CableChannelingItem unboundCableChannelingItem = cablePath.nextLink(startLastBound);
			if(unboundCableChannelingItem != null) {
				final AbstractNode startUnboundNode = cablePath.getStartUnboundNode();
				assert startUnboundNode != null; 
				addLinkBinding(cablePath, fisrtLink, 
						unboundCableChannelingItem, 
						(SiteNode)startUnboundNode, 
						(SiteNode)fisrtLink.getOtherNode(startUnboundNode), 
						true);
			}
		}
		
		if(lastLink != null) {
			if(!lastLink.equals(fisrtLink)) {
				final CableChannelingItem endLastBound = cablePath.getEndLastBoundLink();
				final CableChannelingItem unboundCableChannelingItem = cablePath.previousLink(endLastBound);
				final AbstractNode endUnboundNode = cablePath.getEndUnboundNode();
				assert endUnboundNode != null;
				if(unboundCableChannelingItem != null) {
					addLinkBinding(cablePath, lastLink, 
							unboundCableChannelingItem, 
							(SiteNode)lastLink.getOtherNode(endUnboundNode), 
							(SiteNode)endUnboundNode, 
							false);
				}
			}
		}
	}
	
	private void addLinkBinding(CablePath cablePath, PhysicalLink link1, CableChannelingItem unboundCCI,
			SiteNode fromSite, SiteNode toSite, boolean insertBefore) throws ApplicationException {

		CableChannelingItem newCableChannelingItem = CableController.generateCCI(cablePath, link1, fromSite, toSite);

		Log.debugMessage("CablePathAddEditor | addLinkBinding : try to insert " + 
				newCableChannelingItem + " with seqNum '" + newCableChannelingItem.getSequentialNumber() + 
				(insertBefore ? "' before " : "' after ")+ unboundCCI + " with seqNum '" + unboundCCI.getSequentialNumber() + "'", Level.FINER);

		if(insertBefore) {
			newCableChannelingItem.insertSelfBefore(unboundCCI);
		} else {
			newCableChannelingItem.insertSelfAfter(unboundCCI);
		}

		cablePath.addLink(link1, newCableChannelingItem);
		link1.getBinding().add(cablePath);

		UnboundLink unboundLink = (UnboundLink)cablePath.getBinding().get(unboundCCI); 

		if(unboundCCI.getStartSiteNode().equals(fromSite)
				&& unboundCCI.getEndSiteNode().equals(toSite)) {
			RemoveUnboundLinkCommandBundle command = new RemoveUnboundLinkCommandBundle(unboundLink);
			command.setNetMapViewer(this.netMapViewer);
			command.execute();

			unboundCCI.setParentPathOwner(null, false);
			cablePath.removeLink(unboundCCI);
			this.netMapViewer.getLogicalNetLayer().getCommandList().flush();
		} else {
			if(insertBefore) {
				if(unboundLink.getStartNode().equals(fromSite)) {
					unboundLink.setStartNode(toSite);
				} else {
					unboundLink.setEndNode(toSite);
				}
	
				for(NodeLink nl : unboundLink.getNodeLinksAt(fromSite)) {
					if(nl.getStartNode().equals(fromSite)) {
						nl.setStartNode(toSite);
					} else {
						nl.setEndNode(toSite);
					}
				}
				unboundCCI.setStartSiteNode(toSite);
			}	else {
				if(unboundLink.getStartNode().equals(toSite)) {
					unboundLink.setStartNode(fromSite);
				} else {
					unboundLink.setEndNode(fromSite);
				}
				
				for(NodeLink nl : unboundLink.getNodeLinksAt(toSite)) {
					if(nl.getStartNode().equals(toSite)) {
						nl.setStartNode(fromSite);
					} else {
						nl.setEndNode(fromSite);
					}
				}
				unboundCCI.setEndSiteNode(fromSite);
			}
		}
	}
}
