package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;
import java.awt.dnd.*;
import java.util.*;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * 
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/14 13:30:48 $
 * @module mapviewclient_v1
 */

public class Tree extends JTree implements OperationListener, DragGestureListener {
	Dispatcher dispatcher;
	boolean isDragDropEnabled = true;
	DragSource dragSource = null;
	
	public Tree(Dispatcher dispatcher,  SONode root) {
		super(new Object[0]);
		setModel(root);
		
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
		this.dispatcher.register(this, TreeListSelectionEvent.typ);
		try {
			jbInit();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		this.dispatcher = dispatcher;
	}
	
	public void setModel(SONode root) {
		root.setExpanded(true);
		root.getTreeDataModel().updateChildNodes(root);
		DefaultTreeModel tm = new DefaultTreeModel(root);
		tm.setAsksAllowsChildren(true);
		setModel(tm);
	}
	
	private void jbInit() throws Exception {
		setRootVisible(true);
		setCellRenderer(SOTreeRenderer.getInstance());
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
						
		addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) {
				thisTreeWillExpand(e);
			}
			public void treeWillCollapse(TreeExpansionEvent e) {
				// do nothing
			}
		});
		addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				thisValueChanged(e);
			}
		});
	
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	}

	void thisTreeWillExpand(TreeExpansionEvent e) {
		SONode node = (SONode)e.getPath().getLastPathComponent();
		if(node.isExpanded())
			return;
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		node.setExpanded(true);
		node.getTreeDataModel().updateChildNodes(node);
		setSelectionPath(new TreePath(node.getPath()));
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void thisValueChanged(TreeSelectionEvent e) {
		SONode node = (SONode) e.getPath().getLastPathComponent();
		if (node == null)
			return;

		ObjectResourceController controller = node.getNodeController();
		Object selectedObject = node.getUserObject();
		if (selectedObject instanceof String)
			selectedObject = null; 

		TreeDataSelectionEvent event = new TreeDataSelectionEvent(this, null, null, -1,
				selectedObject, controller);

		dispatcher.notify(event);
	}

	public void dragGestureRecognized(DragGestureEvent event) {
		Point origin = event.getDragOrigin();
		TreePath tp = getClosestPathForLocation(origin.x, origin.y);
		if (tp != null) {
			SONode node = (SONode) tp.getLastPathComponent();
			if (this.isDragDropEnabled) {
				Object obj = node.getUserObject();
				if (obj instanceof DragGestureListener)
					((DragGestureListener) obj).dragGestureRecognized(event);
			}
		}
	}
	
	SONode getNodeForObject(SONode startNode,	Object obj, boolean forceExpansion) {
		// check startNode object
		if (startNode.getUserObject().equals(obj))
			return startNode;
		// if forceExpansion expand the tree
		if (forceExpansion && !startNode.isExpanded()) {
			startNode.setExpanded(true);
			startNode.getTreeDataModel().updateChildNodes(startNode);
		}
		// check startNode's children
		for (Enumeration en = startNode.children(); en.hasMoreElements();) {
			SONode child = (SONode) en.nextElement();
			if (child.getUserObject().equals(obj))
				return child;
		}
		// search in startNode's children
		for (Enumeration en = startNode.children(); en.hasMoreElements();) {
			SONode child = (SONode) en.nextElement();
			SONode node = getNodeForObject(child, obj, forceExpansion);
			if (node != null)
				return node;
		}
		return null;
	}

	/**
	 * @param e
	 * @see com.syrus.AMFICOM.Client.General.Event.OperationListener#operationPerformed(com.syrus.AMFICOM.Client.General.Event.OperationEvent)
	 */
	public void operationPerformed(OperationEvent e) {
		if (e.getActionCommand().equals(TreeListSelectionEvent.typ)) {
			TreeListSelectionEvent select_event = (TreeListSelectionEvent)e;
			if (select_event.SELECT) {
				Object o = e.getSource();
				SONode node = null;
				TreePath tp = getSelectionPath();
				if (tp != null) {
					SONode startNode = (SONode)tp.getParentPath().getLastPathComponent();
					node = getNodeForObject(startNode, o, true);
				}
				if (node == null)
					node = getNodeForObject((SONode)getModel().getRoot(), o, false);
				if (node == null)
					node = getNodeForObject((SONode)getModel().getRoot(), o, true);
				if (node != null)
					setSelectionPath(new TreePath(node.getPath()));
			}
			else if (select_event.REFRESH) {
				Object o = e.getSource();
				if (o == null || o.equals("")) {
					TreePath tp = getSelectionPath();
					if (tp != null) {
						SONode node = tp.getParentPath() == null ? 
								(SONode) tp.getLastPathComponent() : 
								(SONode) tp.getParentPath().getLastPathComponent();
						node.getTreeDataModel().updateChildNodes(node);
						updateUI();
					}
				} 
				else {
					SONode node = getNodeForObject((SONode)getModel().getRoot(), o, false);
					if (node != null && node.getParent() != null) {
						SONode parentNode = (SONode)node.getParent();
						parentNode.getTreeDataModel().updateChildNodes(parentNode);
						updateUI();
					}
				}
			} 
			else if (select_event.DESELECT) {
				getSelectionModel().clearSelection();
			}
		}
	}
}
