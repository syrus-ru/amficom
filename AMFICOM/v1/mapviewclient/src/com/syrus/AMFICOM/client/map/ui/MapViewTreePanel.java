package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.tree.StorableObjectTreeNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public final class MapViewTreePanel extends JPanel 
		implements OperationListener, TreeSelectionListener {
	JTree tree = null;
	MapViewTreeModel model = null;

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

		this.model = new MapViewTreeModel(null);
		this.treeRenderer = new MapViewTreeCellRenderer(this.model);
		this.tree = new JTree(this.model);
		this.scroll.getViewport().add(this.tree);
		this.tree.addTreeSelectionListener(this);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.tree.setCellRenderer(this.treeRenderer);

		this.tree.setRootVisible(true);
	}

	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				Dispatcher disp = this.aContext.getDispatcher();
				disp.unregister(this, MapEvent.MAP_NAVIGATE);
				disp.unregister(this, MapEvent.MAP_CHANGED);
				disp.unregister(this, MapEvent.MAP_VIEW_CHANGED);
				disp.unregister(this, MapEvent.MAP_VIEW_SELECTED);
				disp.unregister(this, MapEvent.MAP_VIEW_CLOSED);
			}
		this.aContext = aContext;
		if(aContext == null)
			return;

		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, MapEvent.MAP_NAVIGATE);
		disp.register(this, MapEvent.MAP_CHANGED);
		disp.register(this, MapEvent.MAP_VIEW_CHANGED);
		disp.register(this, MapEvent.MAP_VIEW_SELECTED);
		disp.register(this, MapEvent.MAP_VIEW_CLOSED);

		updateTree(null);
	}

	public void operationPerformed(OperationEvent operationEvent) {
		if(this.performProcessing) {
			if(	operationEvent.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED)) {
				updateTree(null);
			}
			if(	operationEvent.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED)) {
				MapView mapView = (MapView )operationEvent.getSource();
				if(this.model == null 
						|| this.model.mapView == null 
						|| !this.model.mapView.equals(mapView)) {
					updateTree(mapView);
				}
			}
			if(	operationEvent.getActionCommand().equals(MapEvent.MAP_CHANGED)) {
				updateTree(this.mapView);
			}
			if(	operationEvent.getActionCommand().equals(MapEvent.MAP_VIEW_CHANGED)) {
				MapView mapView = (MapView )operationEvent.getSource();
				updateTree(mapView);
			}
			if(operationEvent.getActionCommand().equals(MapEvent.MAP_NAVIGATE)) {
				MapNavigateEvent mne = (MapNavigateEvent )operationEvent;
				if(mne.isMapElementSelected()) {
					MapElement mapElement = (MapElement )mne.getSource();
					StorableObjectTreeNode node = this.model.findNode(mapElement);
					if(node != null) {
						TreePath path = new TreePath(node.getPath());
						this.tree.getSelectionModel().addSelectionPath(path);
						this.tree.scrollPathToVisible(path);
					}
				}
				else if (mne.isMapElementDeselected()) {
					MapElement mapElement = (MapElement )mne.getSource();
					StorableObjectTreeNode node = this.model.findNode(mapElement);
					if(node != null) {
						TreePath path = new TreePath(node.getPath());
						this.tree.getSelectionModel().removeSelectionPath(path);
					}
				}
			}
		}
	}
	
	public void updateTree(MapView mapView) {
		this.mapView = mapView;
		this.model.setMapView(mapView);
		this.tree.updateUI();
	}

	public void valueChanged(TreeSelectionEvent e) {
		Dispatcher dispatcher = this.aContext.getDispatcher();
		if(dispatcher != null) {
			this.performProcessing = false;
			TreePath paths[] = e.getPaths();
			for (int i = 0; i < paths.length; i++) 
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode )paths[i].getLastPathComponent();
				if(node.getUserObject() instanceof MapElement) {
					MapElement mapElement = (MapElement )node.getUserObject();
					if(e.isAddedPath(paths[i]))
						dispatcher.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					else
						dispatcher.notify(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_DESELECTED_EVENT));
				}
			}
			dispatcher.notify(new MapEvent(this, MapEvent.SELECTION_CHANGED));
			dispatcher.notify(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.performProcessing = true;
		}
	}
}

class MapViewTreeCellRenderer extends JLabel implements TreeCellRenderer {
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	private boolean selected = false; 

	MapViewTreeModel model;
	
	public MapViewTreeCellRenderer(MapViewTreeModel model) {
		this.model = model;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		StorableObjectTreeNode node = (StorableObjectTreeNode )value; 
//		String name = node.getName();
		String name = this.model.getNodeName(node);
		if(name == null || name.length() == 0)
			name = LangModelMap.getString("noname");
		setText(name);
		setIcon(node.getIcon());

		if (!selected) {
			setForeground(tree.getForeground());
		}
		else {
			setForeground(selectedForeground);
		}
		return this;
	}
	
	protected void paintComponent (Graphics g) {
		if (selected) {
			int x = 0;
			Icon icon = getIcon();
			if (icon != null) {
				x += icon.getIconWidth() + getIconTextGap(); 
			}
			g.setColor(selectedBackground);
			g.fillRect(x - 1, 0, getWidth() - x + 2, getHeight());
		}
		super.paintComponent(g);
	}
}