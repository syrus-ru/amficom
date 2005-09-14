/**
 * $Id: MapViewTreeEventHandler.java,v 1.11 2005/09/14 10:41:04 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

public class MapViewTreeEventHandler implements TreeSelectionListener, PropertyChangeListener, TreeWillExpandListener {

	private boolean performProcessing = true;
	private ApplicationContext aContext = null;
	private final JTree tree;

	private MapView mapView = null;
	private final MapViewTreeModel model;
	private final Item root;
	private final IconedTreeUI iconedTreeUI;
	private PopulatableIconedNode topologyNode;
	private MapFrame mapFrame;

	public MapViewTreeEventHandler(IconedTreeUI iconedTreeUI, ApplicationContext context, MapViewTreeModel model, Item root) {
		this.iconedTreeUI = iconedTreeUI;
		this.tree = iconedTreeUI.getTree();
		this.model = model;
		this.root = root;
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
				if(userObject instanceof MapElement
						|| userObject instanceof SchemeElement
						|| userObject instanceof SchemeCableLink
						|| userObject instanceof SchemePath) {
					Object mapElement = userObject;
					if(e.isAddedPath(paths[i]))
						toSelect.add(mapElement);
					else
						toDeSelect.add(mapElement);
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
				updateTree(null);
			}
			else if(mapEventType.equals(MapEvent.MAP_VIEW_SELECTED)) {
				MapView mapView = (MapView )pce.getNewValue();
				if(this.model == null 
						|| this.mapView == null 
						|| !this.mapView.equals(mapView)) {
					updateTree(mapView);
				}
			}
			else if(mapEventType.equals(MapEvent.MAP_CHANGED)) {
				updateTree(this.mapView);
			}
			else if(mapEventType.equals(MapEvent.MAP_SELECTED)) {
				updateTree(this.mapView);
			}
			else if(mapEventType.equals(MapEvent.LIBRARY_SET_CHANGED)) {
				updateTree(this.mapView);
			}
			else if(mapEventType.equals(MapEvent.MAP_VIEW_CHANGED)) {
				MapView mapView = (MapView )pce.getNewValue();
				updateTree(mapView);
			}
			else if(mapEventType.equals(MapEvent.SELECTION_CHANGED)) {
				Collection selection = (Collection )pce.getNewValue();
				Collection items = this.model.findNodes(this.root, selection);
				this.tree.getSelectionModel().clearSelection();
				ItemTreeModel treeModel = (ItemTreeModel )this.tree.getModel();
				for(Iterator iter = items.iterator(); iter.hasNext();) {
					Item node = (Item )iter.next();
					TreePath path = new TreePath(treeModel.getPathToRoot(node));
					this.tree.getSelectionModel().addSelectionPath(path);
					this.tree.scrollPathToVisible(path);
				}
			}
			else if(mapEventType.equals(MapEvent.TOPOLOGY_CHANGED)) {
				this.topologyNode.setParent(null);
				TopologyTreeModel topologyTreeModel = new TopologyTreeModel();
				this.topologyNode = topologyTreeModel.getRoot();
				if(this.mapFrame != null) {
					topologyTreeModel.setNetMapViewer(this.mapFrame.getMapViewer());
				}
				this.root.addChild(this.topologyNode);
			}
			else if(mapEventType.equals(MapEvent.MAP_FRAME_SHOWN)) {
				this.mapFrame = (MapFrame) mapEvent.getNewValue();
				Collection items = this.iconedTreeUI.findNodes(this.root, Collections.singletonList(TopologyTreeModel.TOPOLOGY_BRANCH), false);
				for(Iterator it = items.iterator(); it.hasNext();) {
					PopulatableIconedNode pin = (PopulatableIconedNode )it.next();
					TopologyTreeModel model = (TopologyTreeModel)pin.getChildrenFactory();
					model.setNetMapViewer(this.mapFrame.getMapViewer());
				}
			}
			else if(mapEventType.equals(MapEvent.MAP_REPAINTED)) {
				if(this.iconedTreeUI.isLinkObjects()) {
					Collection items = this.iconedTreeUI.findNodes(this.root, Collections.singletonList(TopologyTreeModel.TOPOLOGY_BRANCH), false);
					for(Iterator it = items.iterator(); it.hasNext();) {
						PopulatableIconedNode pin = (PopulatableIconedNode )it.next();
						if(pin.isPopulated()) {
							pin.populate();
						}
					}
				}
			}
		}

		this.performProcessing = true;
		long f = System.currentTimeMillis();
//		Log.debugMessage("MapViewTreePanel::propertyChange(" + pce.getPropertyName() + ") -------- " + (f - d) + " ms ---------", Level.INFO);
	}

	public void updateTree(MapView mapView) {
		Item mapRoot = this.iconedTreeUI.findNode(this.root, MapViewTreeModel.MAP_VIEW_TREE_ROOT, false);
		if(this.mapView != null) {
			if(mapView == null) {
				List children = new LinkedList(mapRoot.getChildren());
				for(Iterator iter = children.iterator(); iter.hasNext();) {
					Item item = (Item )iter.next();
					item.setParent(null);
				}
			}
			else if(mapView.equals(this.mapView)) {
				for(Iterator iter = mapRoot.getChildren().iterator(); iter.hasNext();) {
					Item item = (Item )iter.next();
					this.model.populate(item);
				}
			}
			else {
				List children = new LinkedList(mapRoot.getChildren());
				for(Iterator iter = children.iterator(); iter.hasNext();) {
					Item item = (Item )iter.next();
					item.setParent(null);
				}
				createNewTree(mapView, mapRoot);
			}
		}
		else {
			if(mapView == null) {
				// empty
			}
			else {
				createNewTree(mapView, mapRoot);
			}
		}
		this.mapView = mapView;
		this.tree.updateUI();
	}

	/**
	 * @param mapView
	 * @param mapRoot TODO
	 */
	private void createNewTree(MapView mapView, Item mapRoot) {
		Item item = MapViewTreeModel.createSingleMapViewRoot(mapView);
		this.model.populate(item);
		mapRoot.addChild(item);

		if(this.topologyNode == null) {
			TopologyTreeModel topologyTreeModel = new TopologyTreeModel();
			this.topologyNode = topologyTreeModel.getRoot();
		}
		mapRoot.addChild(this.topologyNode);
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
