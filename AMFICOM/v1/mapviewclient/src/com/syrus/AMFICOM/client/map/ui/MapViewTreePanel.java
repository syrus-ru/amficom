package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

public final class MapViewTreePanel extends JPanel 
		implements PropertyChangeListener, TreeSelectionListener {

	LogicalTreeUI treeUI;
	JTree tree;
	ItemTreeModel treeModel;
	MapViewTreeModel model;
	Item root;

	ApplicationContext aContext;

	public boolean performProcessing = true;

	private JScrollPane scroll = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();

	private MapView mapView = null;
	
	TreeCellRenderer treeRenderer;

	public MapViewTreePanel() {
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public MapViewTreePanel(ApplicationContext aContext) {
		this();
		setContext(aContext);
	}

	private void jbInit() {
		this.setLayout(this.borderLayout1);

		this.scroll.setWheelScrollingEnabled(true);
		this.scroll.setAutoscrolls(true);

		this.add(this.scroll, BorderLayout.CENTER);

		this.model = MapViewTreeModel.getInstance();
		this.treeRenderer = new MapViewTreeCellRenderer(this.model);

		this.root = new IconedNode("root", LangModelGeneral.getString("root"));
		this.treeUI = new LogicalTreeUI(this.root, false);
		this.tree = this.treeUI.getTree();
		this.treeModel = (ItemTreeModel )this.tree.getModel();
		this.treeModel.setAllwaysSort(false);

		this.scroll.getViewport().add(this.tree);
		this.tree.addTreeSelectionListener(this);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.tree.setCellRenderer(this.treeRenderer);
	}

	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				Dispatcher disp = this.aContext.getDispatcher();
				disp.removePropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
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
		disp.addPropertyChangeListener(MapEvent.MAP_NAVIGATE, this);
		disp.addPropertyChangeListener(MapEvent.MAP_SELECTED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_CHANGED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_VIEW_CHANGED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
		disp.addPropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);

		updateTree(null);
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(this.performProcessing) {
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
			if(	pce.getPropertyName().equals(MapEvent.MAP_VIEW_CHANGED)) {
				MapView mapView = (MapView )pce.getSource();
				updateTree(mapView);
			}
			if(pce.getPropertyName().equals(MapEvent.MAP_NAVIGATE)) {
				MapNavigateEvent mne = (MapNavigateEvent )pce;
				if(mne.isMapElementSelected()) {
					MapElement mapElement = (MapElement )mne.getNewValue();
					Item node = this.model.findNode(this.root, mapElement);
					if(node != null) {
						TreePath path = new TreePath(this.treeModel.getPathToRoot(node));
						this.tree.getSelectionModel().addSelectionPath(path);
						this.tree.scrollPathToVisible(path);
					}
				}
				else if (mne.isMapElementDeselected()) {
					MapElement mapElement = (MapElement )mne.getNewValue();
					Item node = this.model.findNode(this.root, mapElement);
					if(node != null) {
						TreePath path = new TreePath(this.treeModel.getPathToRoot(node));
						this.tree.getSelectionModel().removeSelectionPath(path);
					}
				}
			}
		}
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

	public void valueChanged(TreeSelectionEvent e) {
		Dispatcher dispatcher = this.aContext.getDispatcher();
		if(dispatcher != null) {
			this.performProcessing = false;
			TreePath paths[] = e.getPaths();
			for (int i = 0; i < paths.length; i++) 
			{
				Item node = (Item )paths[i].getLastPathComponent();
				if(node.getObject() instanceof MapElement) {
					MapElement mapElement = (MapElement )node.getObject();
					if(e.isAddedPath(paths[i]))
						dispatcher.firePropertyChange(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					else
						dispatcher.firePropertyChange(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
				}
				else if(node.getObject() instanceof Map) {
					Map map = (Map )node.getObject();
					if(e.isAddedPath(paths[i]))
						dispatcher.firePropertyChange(new MapEvent(map, MapEvent.MAP_SELECTED));
				}
				else if(node.getObject() instanceof MapView) {
					MapView mapView = (MapView )node.getObject();
					if(e.isAddedPath(paths[i]))
						dispatcher.firePropertyChange(new MapEvent(mapView, MapEvent.MAP_VIEW_SELECTED));
				}
			}
			dispatcher.firePropertyChange(new MapEvent(this, MapEvent.SELECTION_CHANGED));
			dispatcher.firePropertyChange(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.performProcessing = true;
		}
	}
	
}


