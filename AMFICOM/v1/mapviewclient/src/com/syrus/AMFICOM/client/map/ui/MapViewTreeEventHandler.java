/**
 * $Id: MapViewTreeEventHandler.java,v 1.2 2005/08/19 15:43:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
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
import com.syrus.util.Log;

public class MapViewTreeEventHandler implements TreeSelectionListener, PropertyChangeListener {

	private boolean performProcessing = true;
	private ApplicationContext aContext = null;
	private final JTree tree;

	private MapView mapView = null;
	private final MapViewTreeModel model;
	private final Item root;

	public MapViewTreeEventHandler(JTree tree, ApplicationContext context, MapViewTreeModel model, Item root) {
		this.tree = tree;
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

			if(	mapEventType.equals(MapEvent.MAP_VIEW_CLOSED)) {
				updateTree(null);
			}
			if(	mapEventType.equals(MapEvent.MAP_VIEW_SELECTED)) {
				MapView mapView = (MapView )pce.getNewValue();
				if(this.model == null 
						|| this.mapView == null 
						|| !this.mapView.equals(mapView)) {
					updateTree(mapView);
				}
			}
			if(	mapEventType.equals(MapEvent.MAP_CHANGED)) {
				updateTree(this.mapView);
			}
			if(	mapEventType.equals(MapEvent.MAP_SELECTED)) {
				updateTree(this.mapView);
			}
			if(	mapEventType.equals(MapEvent.LIBRARY_SET_CHANGED)) {
				updateTree(this.mapView);
			}
			if(	mapEventType.equals(MapEvent.MAP_VIEW_CHANGED)) {
				MapView mapView = (MapView )pce.getNewValue();
				updateTree(mapView);
			}
			if(mapEventType.equals(MapEvent.SELECTION_CHANGED)) {
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
		}

		this.performProcessing = true;
		long f = System.currentTimeMillis();
		Log.debugMessage("MapViewTreePanel::propertyChange(" + pce.getPropertyName() + ") -------- " + (f - d) + " ms ---------", Level.INFO);
	}

	public void updateTree(MapView mapView) {
		if(this.mapView != null) {
			if(mapView == null) {
				List children = new LinkedList(this.root.getChildren());
				for(Iterator iter = children.iterator(); iter.hasNext();) {
					Item item = (Item )iter.next();
					item.setParent(null);
				}
			}
			else if(mapView.equals(this.mapView)) {
				for(Iterator iter = this.root.getChildren().iterator(); iter.hasNext();) {
					Item item = (Item )iter.next();
					this.model.populate(item);
				}
			}
			else {
				List children = new LinkedList(this.root.getChildren());
				for(Iterator iter = children.iterator(); iter.hasNext();) {
					Item item = (Item )iter.next();
					item.setParent(null);
				}
				Item item = new PopulatableIconedNode(
						this.model,
						mapView,
						MapViewTreeModel.mapViewIcon,
						true);
				this.model.populate(item);
				this.root.addChild(item);
			}
		}
		else {
			if(mapView == null) {
				// empty
			}
			else {
				Item item = new PopulatableIconedNode(
						this.model,
						mapView,
						MapViewTreeModel.mapViewIcon,
						true);
				this.model.populate(item);
				this.root.addChild(item);
			}
		}
		this.mapView = mapView;
		this.tree.updateUI();
	}
}
