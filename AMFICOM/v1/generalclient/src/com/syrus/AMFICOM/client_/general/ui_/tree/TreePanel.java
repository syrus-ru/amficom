/*
 * $Id: TreePanel.java,v 1.1 2005/03/05 11:33:03 stas Exp $
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
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/05 11:33:03 $
 * @module generalclient_v1
 */

public class TreePanel extends JPanel implements OperationListener,
		DragGestureListener {

	DragSource dragSource = null;
	boolean isDragEnabled = false;
	boolean send_event = false;

	Dispatcher dispatcher;
	TreeDataModel otm;
	StorableObjectTreeNode root;
	JTree tree;
	

	public TreePanel(Dispatcher disp, TreeDataModel otm) {
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
		tree = new JTree(new Hashtable());
		tree.setRootVisible(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent e) {
				tree_treeWillExpand(e);
			}
			public void treeWillCollapse(TreeExpansionEvent e) {
				// do nothing
			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				tree_valueChanged(e);
			}
		});

		this.setLayout(new BorderLayout());
		this.add(tree, BorderLayout.CENTER);
		
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, this);
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	}

	public void setModel(TreeDataModel otm) {
		this.otm = otm;
		root = otm.getRoot();
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		otm.nodeBeforeExpanded(root);

		List children = otm.getChildNodes(root);
		for (Iterator it = children.iterator(); it.hasNext();) {
			root.add((DefaultMutableTreeNode)it.next());
		}
		DefaultTreeModel tm = new DefaultTreeModel(root);
		tm.setAsksAllowsChildren(true);
		tree.setModel(tm);
		tree.setCellRenderer(StorableObjectTreeRenderer.getInstance());
		tree.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseClicked(e);
					tree.repaint();
				}
			}
			public void mouseEntered(MouseEvent e) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseEntered(e);
					tree.repaint();
				}
			}
			public void mouseExited(MouseEvent e) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseExited(e);
					tree.repaint();
				}
			}
			public void mousePressed(MouseEvent e) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mousePressed(e);
					tree.repaint();
				}
			}
			public void mouseReleased(MouseEvent e) {
				TreePath path = tree.getSelectionPath();
				if (path != null) {
					StorableObjectTreeNode node =	(StorableObjectTreeNode)path.getLastPathComponent();
					node.mouseReleased(e);
					tree.repaint();
				}
			}
		});
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public JTree getTree() {
		return tree;
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
				tree.getSelectionModel().clearSelection();
			}
		} 
	}

	public void tree_valueChanged(TreeSelectionEvent e) {
		/*StorableObjectTreeNode node = (StorableObjectTreeNode)e.getPath().getLastPathComponent();
		if (node == null)
			return;

		List res = new LinkedList();
		Class cl = null;
		ObjectResourceController controller = null;
		ObjectResourceCatalogActionModel orcam = null;
		int n = -1;
		Object selectedObject = node.getObject();

		if (selectedObject instanceof String) {
			if (node.getAllowsChildren()) {
				this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				node.removeAllChildren();
				otm.nodeBeforeExpanded(node);
				List vec = otm.getChildNodes(node);
				if (vec.size() == 0)
					node.setAllowsChildren(false);

				for (Iterator it = vec.iterator(); it.hasNext();) {
					StorableObjectTreeNode tn = (StorableObjectTreeNode)it.next();
					node.add(tn);
					Object obj = tn.getObject();
					if (!(obj instanceof String))
							res.add(obj);
				}
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
				cl = otm.getNodeChildClass(node);
				controller = otm.getNodeChildController(node);
			} 
		}
		else {
			if (node.isRoot())
				return;

			StorableObjectTreeNode parent = (StorableObjectTreeNode)node.getParent();

			for (Enumeration enumeration = parent.children(); enumeration.hasMoreElements();) {
				Object oo = ((StorableObjectTreeNode)enumeration.nextElement()).getObject();
				if (!(oo instanceof String))
					res.add(oo);
			}
			cl = otm.getNodeChildClass(parent);
			controller = otm.getNodeChildController(parent);
			n = res.indexOf(selectedObject);
		}

		TreeDataSelectionEvent event = new TreeDataSelectionEvent(this, res, cl, n,
				selectedObject, controller);
		event.setParam(orcam);

		send_event = true;
		dispatcher.notify(event);
		send_event = false;*/
	}

	void tree_treeWillExpand(TreeExpansionEvent e) {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		StorableObjectTreeNode node = (StorableObjectTreeNode) e.getPath().getLastPathComponent();
		otm.nodeBeforeExpanded(node);
		List vec = otm.getChildNodes(node);
		if (vec.size() == 0) {
			node.setAllowsChildren(false);
		}
		for (Iterator it = vec.iterator(); it.hasNext();) {
			node.add((DefaultMutableTreeNode)it.next());
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		tree.setSelectionPath(new TreePath(node.getPath()));
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
			otm.nodeBeforeExpanded(tn);
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
		TreePath tp = tree.getClosestPathForLocation(origin.x, origin.y);
		DefaultMutableTreeNode ortn = (DefaultMutableTreeNode) tp.getLastPathComponent();
		if (isDragEnabled) {
			Object obj = ortn.getUserObject();
			if (obj instanceof DragGestureListener)
				((DragGestureListener)obj).dragGestureRecognized(event);
		}
	}
}
