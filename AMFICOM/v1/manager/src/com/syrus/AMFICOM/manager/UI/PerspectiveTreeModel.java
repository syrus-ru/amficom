/*-
* $Id: PerspectiveTreeModel.java,v 1.1 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PerspectiveTreeModel implements TreeModel {

	/** Root of the tree. */
    protected TreeNode root;
	
    /** Listeners */
	private List<TreeModelListener>	treeModelListeners;

	private final ManagerMainFrame	managerMainFrame;
	
	public PerspectiveTreeModel(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
		this.root = new DefaultMutableTreeNode("Administration");
	}
	
	public Object getRoot() {
		return this.root;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(	Object parent,
							int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path,
									Object newValue) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(	Object parent,
								Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addTreeModelListener(TreeModelListener listener) {
		if (this.treeModelListeners == null) {
			this.treeModelListeners = new LinkedList<TreeModelListener>();
		}

		if (!this.treeModelListeners.contains(listener)) {
			this.treeModelListeners.add(listener);
		}

	}

	public void removeTreeModelListener(TreeModelListener listener) {
		if (this.treeModelListeners == null) { return; }

		if (this.treeModelListeners.contains(listener)) {
			this.treeModelListeners.remove(listener);
		}
	}

	public void reload(final TreeNode node) {
		if (node != null) {
//			this.fireTreeStructureChanged(this, this.getPathToRoot(node), null, null);
		}
	}
	
	protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;
		for (TreeModelListener listener : this.treeModelListeners) {
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
			listener.treeStructureChanged(e);
		}
	}
}

