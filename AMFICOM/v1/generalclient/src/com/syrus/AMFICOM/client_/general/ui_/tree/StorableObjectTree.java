/*
 * $Id: StorableObjectTree.java,v 1.1 2005/03/05 15:20:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/05 15:20:38 $
 * @module generalclient_v1
 */

public class StorableObjectTree extends JTree implements OperationListener,
		DragGestureListener {
	private static final long serialVersionUID = 3976731458805248816L;
	DragSource dragSource = null;
	boolean send_event = false;
	
	/** 
	 * Variable to force drag-n-drop prohibit.
	 * If true, then DragGestureListener implementing objects will dnd and others will not   
	 */
	boolean isDragDropEnabled = true;

	Dispatcher dispatcher;
	TreeDataModel otm;
	StorableObjectTreeNode root;

	public StorableObjectTree(Dispatcher disp, TreeDataModel otm) {
		super(new Object[0]);
		this.dispatcher = disp;
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setModel(otm);
	}

	private void jbInit() throws Exception {
		setRootVisible(true);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				
		addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) {
				tree_treeWillExpand(e);
			}
			public void treeWillCollapse(TreeExpansionEvent e) {
				// do nothing
			}
		});
		addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});
	
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	}

	public void setModel(TreeDataModel otm) {
		this.otm = otm;
		root = otm.getRoot();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		List children = otm.getChildNodes(root);
		for (Iterator it = children.iterator(); it.hasNext();) {
			root.add((DefaultMutableTreeNode)it.next());
		}
		DefaultTreeModel tm = new DefaultTreeModel(root);
		tm.setAsksAllowsChildren(true);
		setModel(tm);
		setCellRenderer(StorableObjectTreeRenderer.getInstance());
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				TreePath path = getClosestPathForLocation(e.getX(), e.getY());
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseClicked(e);
					repaint();
				}
			}
			public void mouseEntered(MouseEvent e) {
				TreePath path = getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseEntered(e);
					repaint();
				}
			}
			public void mouseExited(MouseEvent e) {
				TreePath path = getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseExited(e);
					repaint();
				}
			}
			public void mousePressed(MouseEvent e) {
				TreePath path = getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mousePressed(e);
					repaint();
				}
			}
			public void mouseReleased(MouseEvent e) {
				TreePath path = getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseReleased(e);
					repaint();
				}
			}
		});
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public boolean setNodeSelection(DefaultMutableTreeNode orte, Object o) {
		boolean selected = false;
		return selected;
	}

	public void operationPerformed(OperationEvent ev) {
		if (ev.getActionCommand().equals(TreeListSelectionEvent.typ)) {
			TreeListSelectionEvent select_event = (TreeListSelectionEvent) ev;
			if (select_event.SELECT) {
			// TODO
			} 
			else if (select_event.REFRESH) {
			// TODO
			} 
			else if (select_event.DESELECT) {
				getSelectionModel().clearSelection();
			}
		} 
	}

	public void tree_valueChanged(TreeSelectionEvent e) {
		StorableObjectTreeNode node = (StorableObjectTreeNode)e.getPath().getLastPathComponent();
		if (node == null)
			return;

		List res = new LinkedList();
		Class cl = null;
		ObjectResourceController controller = null;
		int n = -1;
		Object selectedObject = node.getObject();

		if (selectedObject instanceof String) {
			if (node.getAllowsChildren()) {
				cl = otm.getNodeChildClass(node);
				controller = otm.getNodeChildController(node);
				
				for (Enumeration enumeration = node.children(); enumeration.hasMoreElements();) {
					Object childObj = ((StorableObjectTreeNode)enumeration.nextElement()).getObject();
					if (!(childObj instanceof String))
						res.add(childObj);
				}
			} 
		}
		else {
			if (node.isRoot())
				return;
			StorableObjectTreeNode parentNode = (StorableObjectTreeNode)node.getParent();
			cl = otm.getNodeChildClass(parentNode);
			controller = otm.getNodeChildController(parentNode);
			n = res.indexOf(selectedObject);
			
			for (Enumeration enumeration = parentNode.children(); enumeration.hasMoreElements();) {
				Object childObj = ((StorableObjectTreeNode)enumeration.nextElement()).getObject();
				if (!(childObj instanceof String))
					res.add(childObj);
			}
		}

		TreeDataSelectionEvent event = new TreeDataSelectionEvent(this, res, cl, n,
				selectedObject, controller);

		send_event = true;
		dispatcher.notify(event);
		send_event = false;
	}

	void tree_treeWillExpand(TreeExpansionEvent e) {
		StorableObjectTreeNode node = (StorableObjectTreeNode)e.getPath().getLastPathComponent();
		if (!node.isExpanded()) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			node.removeAllChildren();
			List vec = otm.getChildNodes(node);
			for (Iterator it = vec.iterator(); it.hasNext();) {
				node.add((DefaultMutableTreeNode) it.next());
			}
			setSelectionPath(new TreePath(node.getPath()));
			node.setExpanded(true);
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	DefaultMutableTreeNode recure_tree_exp(StorableObjectTreeNode tn, Object o) {
		DefaultMutableTreeNode oooo = null;
		if (!tn.getAllowsChildren())
			return null;

		StorableObjectTreeNode fc = (StorableObjectTreeNode) tn.getFirstChild();

		Object obj = fc.getObject();
		if (obj instanceof String && ((String) obj).length() == 0) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			tn.removeAllChildren();
			List vec = otm.getChildNodes(tn);
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			if (vec.size() == 0)
				tn.setAllowsChildren(false);

			for (Iterator it = vec.iterator(); it.hasNext();) {
				StorableObjectTreeNode tnn = (StorableObjectTreeNode)it.next();
				tn.add(tnn);

				if (!tnn.getObject().equals(o)) {
					oooo = recure_tree_exp(tnn, o);
					if (oooo != null) {
						for (; it.hasNext();) {
							StorableObjectTreeNode temp = ((StorableObjectTreeNode)it.next());
							tn.add(temp);
						}
						break;
					}
				} 
				else {
					oooo = tnn;
					for (; it.hasNext();) {
						StorableObjectTreeNode temp = ((StorableObjectTreeNode)it.next());
						tn.add(temp);
					}
					break;
				}
			}
		} 
		else {
			for (Enumeration en = tn.children(); en.hasMoreElements();) {
				StorableObjectTreeNode tnn = (StorableObjectTreeNode) en.nextElement();
				if (!tnn.getObject().equals(o)) {
					oooo = recure_tree_exp(tnn, o);
					if (oooo != null) {
						break;
					}
				} 
				else {
					oooo = tnn;
					break;
				}
			}
		}
		return oooo;
	}

	public void dragGestureRecognized(DragGestureEvent event) {
		Point origin = event.getDragOrigin();
		TreePath tp = getClosestPathForLocation(origin.x, origin.y);
		StorableObjectTreeNode node = (StorableObjectTreeNode) tp.getLastPathComponent();
		if (this.isDragDropEnabled) {
			Object obj = node.getObject();
			if (obj instanceof DragGestureListener)
				((DragGestureListener)obj).dragGestureRecognized(event);
		}
	}
}
