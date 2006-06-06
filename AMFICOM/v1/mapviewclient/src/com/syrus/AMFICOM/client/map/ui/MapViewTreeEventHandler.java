/*-
 * $$Id: MapViewTreeEventHandler.java,v 1.19 2006/06/06 13:03:32 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * @version $Revision: 1.19 $, $Date: 2006/06/06 13:03:32 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewTreeEventHandler implements TreeSelectionListener, PropertyChangeListener, TreeWillExpandListener {

	private boolean performProcessing = true;
	private ApplicationContext aContext = null;
	private final JTree tree;
	private final IconedTreeUI iconedTreeUI;

	private MapFrame mapFrame;
	private MapView mapView = null;
	private final Item root;

	public MapViewTreeEventHandler(IconedTreeUI iconedTreeUI, ApplicationContext context, Item root) {
		this.iconedTreeUI = iconedTreeUI;
		this.root = root;
		this.tree = iconedTreeUI.getTree();
		setContext(context);
		updateTree(null);
	}

	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				Dispatcher disp = this.aContext.getDispatcher();
				disp.removePropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
			}
		this.aContext = aContext;
		if(aContext == null)
			return;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
	}

	public void valueChanged(TreeSelectionEvent e) {
		if(!this.performProcessing) {
			return;
		}
		if(this.mapView == null) {
			return;
		}
		Dispatcher dispatcher = this.aContext.getDispatcher();
		if(dispatcher != null) {
			this.performProcessing = false;
			TreePath paths[] = e.getPaths();
			Collection toSelect = new LinkedList();
			Collection toDeSelect = new LinkedList();
			boolean sendSelectionEvent = true;
			for (int i = 0; i < paths.length; i++) 
			{
				Item node = (Item )paths[i].getLastPathComponent();
				Object userObject = node.getObject();
				Object mapElement = null;
				if(userObject instanceof MapElement) {
					mapElement = userObject;
				} else if (userObject instanceof SchemeElement) {
					mapElement = this.mapView.findElement((SchemeElement)userObject);
				} else if (userObject instanceof SchemeCableLink) {
					mapElement = this.mapView.findCablePath((SchemeCableLink)userObject);
				} else if (userObject instanceof SchemePath) {
					mapElement = this.mapView.findMeasurementPath((SchemePath)userObject);
				} 
				else if(userObject instanceof Map) {
					Map map = (Map )userObject;
					if(e.isAddedPath(paths[i])) {
						dispatcher.firePropertyChange(new MapEvent(this, MapEvent.MAP_SELECTED, map));
						sendSelectionEvent = false;
					}
				}
				else if(userObject instanceof MapView) {
					MapView mapView = (MapView )userObject;
					if(e.isAddedPath(paths[i])) {
						dispatcher.firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_SELECTED, mapView));
						sendSelectionEvent = false;
					}
				}
				else if(userObject instanceof SiteNodeType
						|| userObject instanceof PhysicalLinkType
						|| userObject instanceof MapLibrary) {
					if(e.isAddedPath(paths[i])) {
						dispatcher.firePropertyChange(new MapEvent(this, MapEvent.OTHER_SELECTED, userObject));
						sendSelectionEvent = false;
					}
				}

				if(mapElement != null) {
					if (e.isAddedPath(paths[i])) {
						toSelect.add(mapElement);
					} else {
						toDeSelect.add(mapElement);
					}
				}
			}

			if(sendSelectionEvent) {
				for(Iterator iter = toSelect.iterator(); iter.hasNext();) {
					Object element = iter.next();
					if(element instanceof MapElement) {
						MapElement mapElement = (MapElement )element;
						this.mapView.getMap().setSelected(mapElement, true);
					}
				}

				for(Iterator iter = toDeSelect.iterator(); iter.hasNext();) {
					Object element = iter.next();
					if(element instanceof MapElement) {
						MapElement mapElement = (MapElement )element;
						this.mapView.getMap().setSelected(mapElement, false);
					}
				}

				dispatcher.firePropertyChange(new MapEvent(this, MapEvent.UPDATE_SELECTION));
				dispatcher.firePropertyChange(new MapEvent(this, MapEvent.SELECTION_CHANGED, this.mapView.getMap().getSelectedElements()));
			}
			this.performProcessing = true;
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		long d = System.currentTimeMillis();
		if(!this.performProcessing) {
			return;
		}
		this.performProcessing = false;

		if(pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent )pce;
			String mapEventType = mapEvent.getMapEventType();

			if(mapEventType.equals(MapEvent.MAP_VIEW_CLOSED)) {
				this.mapView = null;
				updateTree(null);
			}
			else if(mapEventType.equals(MapEvent.MAP_VIEW_SELECTED)) {
				MapView mapView = (MapView )pce.getNewValue();
				if(this.mapView == null || !this.mapView.equals(mapView)) {
					this.mapView = mapView;
					updateTree(mapView);
					updateLinkToNetMapViewer();
				}
			}
			else if(mapEventType.equals(MapEvent.MAP_CHANGED)) {
				if(this.iconedTreeUI.isLinkObjects()) {
					updateTree(this.mapView);
				}
			}
			else if(mapEventType.equals(MapEvent.MAP_SELECTED)) {
				updateTree(this.mapView);
			}
			else if(mapEventType.equals(MapEvent.LIBRARY_SET_CHANGED)) {
				updateTree(this.mapView);
			}
			else if(mapEventType.equals(MapEvent.MAP_VIEW_CHANGED)) {
				MapView mapView = (MapView )pce.getNewValue();
				this.mapView = mapView;
				updateTree(mapView);
				updateLinkToNetMapViewer();
			}
			else if(mapEventType.equals(MapEvent.SELECTION_CHANGED)) {
				if(this.iconedTreeUI.isLinkObjects()) {
					Collection selection = (Collection )pce.getNewValue();
					Collection items = this.iconedTreeUI.findNodes(this.root, selection, false);
					this.tree.getSelectionModel().clearSelection();
					ItemTreeModel treeModel = (ItemTreeModel )this.tree.getModel();
					for(Iterator iter = items.iterator(); iter.hasNext();) {
						Item node = (Item )iter.next();
						TreePath path = new TreePath(treeModel.getPathToRoot(node));
						this.tree.getSelectionModel().addSelectionPath(path);
						this.tree.scrollPathToVisible(path);
					}
				}
			}
			else if(mapEventType.equals(MapEvent.TOPOLOGY_CHANGED)) {
				updateTopologyTree(this.mapFrame.getMapViewer());
			}
			else if(mapEventType.equals(MapEvent.MAP_FRAME_SHOWN)) {
				this.mapFrame = (MapFrame) mapEvent.getNewValue();
				updateLinkToNetMapViewer();
			}
			else if(mapEventType.equals(MapEvent.MAP_REPAINTED)) {
				if(this.iconedTreeUI.isLinkObjects()) {
					Collection items = this.iconedTreeUI.findNodes(
							this.root, 
							Collections.singletonList(TopologyTreeModel.TOPOLOGY_BRANCH), 
							false);
					for(Iterator it = items.iterator(); it.hasNext();) {
						PopulatableIconedNode pin = (PopulatableIconedNode )it.next();
						if(pin.isPopulated()) {
							pin.populate();
						}
					}
					if(this.mapView != null) {
						items = this.iconedTreeUI.findNodes(
								this.root, 
								Collections.singletonList(this.mapView.getMap()), 
								false);
						for(Iterator it = items.iterator(); it.hasNext();) {
							PopulatableIconedNode pin = (PopulatableIconedNode )it.next();
							if(pin.isPopulated()) {
								pin.populate();
							}
						}
					}
				}
			}
		}

		this.performProcessing = true;
		long f = System.currentTimeMillis();
//		Log.debugMessage(pce.getPropertyName() + " -------- " + (f - d) + " ms ---------", Level.INFO);
	}

	/**
	 * 
	 */
	private void updateLinkToNetMapViewer() {
		if(this.mapFrame != null) {
			Collection items = this.iconedTreeUI.findNodes(
					this.root, 
					Collections.singletonList(TopologyTreeModel.TOPOLOGY_BRANCH), 
					false);
			for(Iterator it = items.iterator(); it.hasNext();) {
				PopulatableIconedNode pin = (PopulatableIconedNode )it.next();
				TopologyTreeModel model = (TopologyTreeModel)pin.getChildrenFactory();
				model.setNetMapViewer(this.mapFrame.getMapViewer());
				pin.populate();
			}
			if(this.mapView != null) {
				items = this.iconedTreeUI.findNodes(
						this.root, 
						Collections.singletonList(this.mapView.getMap()), 
						false);
				for(Iterator it = items.iterator(); it.hasNext();) {
					PopulatableIconedNode pin = (PopulatableIconedNode )it.next();
					MapTreeModel model = (MapTreeModel)pin.getChildrenFactory();
					model.setNetMapViewer(this.mapFrame.getMapViewer());
				}
			}
		}
	}

	public void updateTree(MapView mapView) {
		PopulatableIconedNode node = (PopulatableIconedNode) this.iconedTreeUI.findNode(
				this.root, 
				MapEditorTreeModel.MAP_EDITOR_TREE_ROOT, 
				false);
		if(node != null) {
			MapEditorTreeModel model = (MapEditorTreeModel) node.getChildrenFactory();
			model.setMapView(mapView);
			this.tree.updateUI();
		}
	}

	public void updateTopologyTree(NetMapViewer netMapViewer) {
		PopulatableIconedNode node = (PopulatableIconedNode) this.iconedTreeUI.findNode(
				this.root, 
				MapEditorTreeModel.MAP_EDITOR_TREE_ROOT, 
				false);
		if(node != null) {
			MapEditorTreeModel model = (MapEditorTreeModel) node.getChildrenFactory();
			model.updateTopologyTree(netMapViewer);
		}
	}

	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		// nothing
	}

	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		final TreePath path = event.getPath();
		final Object lastPathComponent = path.getLastPathComponent();
		if (lastPathComponent instanceof IconedNode) {
			IconedNode node = (IconedNode)lastPathComponent;
			if(node.getObject() instanceof SpatialLayer) {
				PopulatableIconedNode populatable = (PopulatableIconedNode)node;
				populatable.clearChildren();
			}
			
		}
	}
}
