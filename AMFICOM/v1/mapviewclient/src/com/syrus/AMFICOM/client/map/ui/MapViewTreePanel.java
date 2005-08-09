package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

public final class MapViewTreePanel extends JPanel 
		implements PropertyChangeListener {

	IconedTreeUI treeUI;
	JTree tree;
	ItemTreeModel treeModel;
	MapViewTreeModel model;
	Item root;

	ApplicationContext aContext;

	private JScrollPane scroll = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();

	private MapView mapView = null;
	public boolean performProcessing = true;
	
	public MapViewTreePanel(ApplicationContext aContext) {
		setContext(aContext);
		jbInit();
		updateTree(null);
	}

	private void jbInit() {
		this.setLayout(this.borderLayout1);

		this.scroll.setWheelScrollingEnabled(true);
		this.scroll.setAutoscrolls(true);

		this.add(this.scroll, BorderLayout.CENTER);

		this.model = MapViewTreeModel.getInstance();

		this.root = new IconedNode("root", LangModelGeneral.getString("root"));
		this.treeUI = new IconedTreeUI(this.root);
		this.tree = this.treeUI.getTree();
		this.treeModel = (ItemTreeModel )this.tree.getModel();
		this.treeModel.setAllwaysSort(false);

		this.scroll.getViewport().add(this.tree);
		this.tree.addTreeSelectionListener(new MapViewTreeSelectionListener(this));
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
	}

	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				Dispatcher disp = this.aContext.getDispatcher();
				disp.removePropertyChangeListener(MapEvent.LIBRARY_SET_CHANGED, this);
				disp.removePropertyChangeListener(MapEvent.SELECTION_CHANGED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_SELECTED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_CHANGED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_VIEW_CHANGED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
			}
		this.aContext = aContext;
		if(aContext == null)
			return;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.addPropertyChangeListener(MapEvent.LIBRARY_SET_CHANGED, this);
		disp.addPropertyChangeListener(MapEvent.SELECTION_CHANGED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_SELECTED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_CHANGED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_VIEW_CHANGED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
	}

	public void propertyChange(PropertyChangeEvent pce) {
		long d = System.currentTimeMillis();
		if(!this.performProcessing) {
			return;
		}
		this.performProcessing = false;

		if(	pce.getPropertyName().equals(MapEvent.MAP_VIEW_CLOSED)) {
			updateTree(null);
		}
		if(	pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED)) {
			MapView mapView = (MapView )pce.getSource();
			if(this.model == null 
					|| this.mapView == null 
					|| !this.mapView.equals(mapView)) {
				updateTree(mapView);
			}
		}
		if(	pce.getPropertyName().equals(MapEvent.MAP_CHANGED)) {
			updateTree(this.mapView);
		}
		if(	pce.getPropertyName().equals(MapEvent.MAP_SELECTED)) {
			updateTree(this.mapView);
		}
		if(	pce.getPropertyName().equals(MapEvent.LIBRARY_SET_CHANGED)) {
			updateTree(this.mapView);
		}
		if(	pce.getPropertyName().equals(MapEvent.MAP_VIEW_CHANGED)) {
			MapView mapView = (MapView )pce.getSource();
			updateTree(mapView);
		}
		if(pce.getPropertyName().equals(MapEvent.SELECTION_CHANGED)) {
			Collection selection = (Collection )pce.getNewValue();
			Collection items = this.model.findNodes(this.root, selection);
			this.tree.getSelectionModel().clearSelection();
			for(Iterator iter = items.iterator(); iter.hasNext();) {
				Item node = (Item )iter.next();
				TreePath path = new TreePath(this.treeModel.getPathToRoot(node));
				this.tree.getSelectionModel().addSelectionPath(path);
				this.tree.scrollPathToVisible(path);
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


