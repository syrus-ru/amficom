/*-
* $Id: GraphTreeModel.java,v 1.8 2005/09/08 14:35:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import gnu.trove.TObjectIntHashMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphModel;

import com.syrus.AMFICOM.general.ErrorMessages;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/08 14:35:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class GraphTreeModel implements TreeModel {

	private List<TreeModelListener>	treeModelListeners;

//	private Map<TreeNode, Integer>	treeNodeIndexMap;
	private TObjectIntHashMap treeNodeIndexMap;
	private Map<TreeNode, TreeNode[]>	treeNodePathMap;

	protected GraphModel			model;

	protected DefaultGraphCell		root;
	
	private boolean direct;

	public GraphTreeModel(GraphModel model, boolean direct) {
		this.model = model;
		this.root = (DefaultGraphCell) this.model.getRootAt(0);
		this.treeNodeIndexMap = new TObjectIntHashMap();
		this.treeNodePathMap = new HashMap<TreeNode, TreeNode[]>();
		this.direct = direct;
		this.clearCache();		
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

	@Override
	public String toString() {
		return this.model.toString();
	}

	public void clearCache() {
		this.treeNodeIndexMap.clear();
		this.treeNodePathMap.clear();
//		this.treeNodePathMap.put(this.root, new TreeNode[] {this.root});
	}

	public Object getChild(Object parent, int index) {
//		System.out.println("GraphTreeModel.getChild() | parent:" + parent + ", index:" + index);
//		if (index == 0) {
//			try {
//				throw new Exception();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		final DefaultGraphCell cell = (DefaultGraphCell) parent;
		final DefaultPort port = (DefaultPort) cell.getChildAt(0);

		int count = 0;

		DefaultPort rootPort = (DefaultPort) this.root.getChildAt(0);

		for (Object object : port.getEdges()) {
			Edge edge = (Edge) object;
			DefaultPort target = (DefaultPort) (this.direct ? edge.getTarget() : edge.getSource());
			if (target == port) {
				continue;
			}

			int targetEdgeSourceCount = 0;
			boolean sourceRootFound = false;
			for (Object oEdge : target.getEdges()) {
				Edge edge2 = (Edge) oEdge;
				targetEdgeSourceCount += ((this.direct ? edge2.getTarget() : edge2.getSource()) == target) ? 1 : 0;
				if (!sourceRootFound) {
					sourceRootFound = (this.direct ? edge2.getSource() : edge2.getTarget()) == rootPort;
				}
			}

			targetEdgeSourceCount = targetEdgeSourceCount == 0 && sourceRootFound ? 1 : targetEdgeSourceCount;
			if (cell != this.root) {
				targetEdgeSourceCount = 1;
			}

			if (count == index && targetEdgeSourceCount == 1) {
				DefaultGraphCell treeNode = (DefaultGraphCell) target.getParent();
				if (this.treeNodeIndexMap.get(treeNode) == 0) {
					this.treeNodeIndexMap.put(treeNode, index);
					this.cacheTreePath(treeNode);
				}
				return treeNode;
			}

			count += (target != null && target != port && targetEdgeSourceCount == 1) ? 1 : 0;

			// int sourceCount = 0;
			// boolean foundNotRootPort = false;
			// for(Object oEdge : target.getEdges()) {
			// Edge edge2 = (Edge)oEdge;
			// Object target2 = edge2.getTarget();
			// if (target2 == target && !foundNotRootPort) {
			// foundNotRootPort = edge2.getSource() != rootPort;
			// }
			// sourceCount += (target2 == target) ? 1 : 0;
			// }
			//			
			// // + (foundNotRootPort ? 1 : 0)
			// if (count == index && (sourceCount == 1 + (foundNotRootPort ? 1 :
			// 0))) {
			// return target.getParent();
			// }
			// count += (sourceCount == 1 + (foundNotRootPort ? 1 : 0)) ? 1 : 0;
		}
		return null;
	}

	public int getChildCount(Object parent) {
		final DefaultGraphCell cell = (DefaultGraphCell) parent;
		final DefaultPort port = (DefaultPort) cell.getChildAt(0);

		DefaultPort rootPort = (DefaultPort) this.root.getChildAt(0);
//		System.out.println();
//		System.out.println("GraphTreeModel.getChildCount() | parent:" +
//		parent);

		this.cacheTreePath(cell);

		
		int count = 0;
		for (Object object : port.getEdges()) {
			Edge edge = (Edge) object;
			DefaultPort target = (DefaultPort) (this.direct ? edge.getTarget() : edge.getSource());
			if (target == port || target == null) {
				continue;
			}

//			System.out.println("GraphTreeModel.getChildCount() | target:" + target);
			int targetEdgeSourceCount = 0;
			boolean sourceRootFound = false;
			for (Object oEdge : target.getEdges()) {
				Edge edge2 = (Edge) oEdge;
				targetEdgeSourceCount += ((this.direct ? edge2.getTarget() : edge2.getSource()) == target) ? 1 : 0;
				if (!sourceRootFound) {
					sourceRootFound = (this.direct ? edge2.getSource() : edge2.getTarget()) == rootPort;
					
				}
			}

			targetEdgeSourceCount = targetEdgeSourceCount == 0 && sourceRootFound ? 1 : targetEdgeSourceCount;
			if (cell != this.root) {
				targetEdgeSourceCount = 1;
			}

			if (targetEdgeSourceCount == 1) {
				DefaultGraphCell treeNode = (DefaultGraphCell) target.getParent();
				if (this.treeNodeIndexMap.get(treeNode) == 0) {
					this.treeNodeIndexMap.put(treeNode, count);
					this.cacheTreePath(treeNode);
				}
			}
			
			count += (target != null && targetEdgeSourceCount == 1) ? 1 : 0;

			// int sourceCount = 0;
			// boolean foundNotRootPort = false;
			// for(Object oEdge : target.getEdges()) {
			// Edge edge2 = (Edge)oEdge;
			// Object target2 = edge2.getTarget();
			// // System.out.println("GraphTreeModel.getChildCount() | parent:"
			// + parent + "\n\ttarget: " + target + ", target2:" + target2);
			// // sourceCount++;
			// if (target2 == target && !foundNotRootPort) {
			// foundNotRootPort = edge2.getSource() != rootPort;
			// System.err.println("GraphTreeModel.getChildCount() | parent:" +
			// parent + "\ntarget: " + target + ", nonROOT:" +
			// edge2.getSource());
			// }
			// sourceCount += (target2 == target) ? 1 : 0;
			// }
			//			
			// System.out.println("GraphTreeModel.getChildCount() | parent:" +
			// parent + "\ntarget: " + target + ", sourceCount: " +
			// sourceCount);
			//			
			// //
			// count += (sourceCount == 1 + (foundNotRootPort ? 1 : 0)) ? 1 : 0;
		}
		 System.out.println("GraphTreeModel.getChildCount() | " + parent + ", count: " + count);
		return count;
	}

	public int getIndexOfChild(Object parent, Object child) {
		// TODO rebuild
		int childCount = this.getChildCount(parent);
//		System.out.println("GraphTreeModel.getIndexOfChild() | parent: " +
//			parent + "\n\t\tchild: "+ child + ", childCount: " + childCount);

		for (int i = 0; i < childCount; i++) {
//			System.out.println("GraphTreeModel.getIndexOfChild() | i:" + i);
			if (child == this.getChild(parent, i)) return i;
		}
		return -1;
	}
	
	private void cacheTreePath(DefaultGraphCell cell) {
		if (this.treeNodePathMap.get(cell) == null) {
			this.treeNodePathMap.put(cell, this.getPathToRoot(cell));
		}
	}

	public int _getIndexOfChild(Object parent, Object child) {
		final DefaultGraphCell cell = (DefaultGraphCell) parent;
		final DefaultPort port = (DefaultPort) cell.getChildAt(0);

		DefaultPort rootPort = (DefaultPort) this.root.getChildAt(0);
//		System.out.println("GraphTreeModel.getIndexOfChild()");
		int index = 0;
		for (Object object : port.getEdges()) {
			Edge edge = (Edge) object;
			DefaultPort target = (DefaultPort) edge.getTarget();

			// if (target == child) {
			// return index;
			// }
			//			
			// int targetEdgeSourceCount = 0;
			// for(Object oEdge : target.getEdges()) {
			// Edge edge2 = (Edge)oEdge;
			// targetEdgeSourceCount += (edge2.getTarget() == target &&
			// edge2.getSource() != rootPort)? 1 : 0;
			// }
			//			
			// index += (target != null && target != port &&
			// targetEdgeSourceCount == 1) ? 1 : 0;

			int targetEdgeSourceCount = 0;
			boolean sourceRootFound = false;
			for (Object oEdge : target.getEdges()) {
				Edge edge2 = (Edge) oEdge;
				targetEdgeSourceCount += (edge2.getTarget() == target) ? 1 : 0;
				if (!sourceRootFound) {
					sourceRootFound = edge2.getSource() == rootPort;
				}
			}

			targetEdgeSourceCount = targetEdgeSourceCount == 0 && sourceRootFound ? 1 : targetEdgeSourceCount;

			if (cell != this.root) {
				targetEdgeSourceCount = 1;
			}

			if (target == child) { return index; }

			index += (target != null && target != port && targetEdgeSourceCount == 1) ? 1 : 0;
		}
		return -1;
	}

	public Object getRoot() {
		return this.root;
	}
	
	/**
     * Sets the root to <code>root</code>. A null <code>root</code> implies
     * the tree is to display nothing, and is legal.
     */
    public void setRoot(DefaultGraphCell root) {
    	this.clearCache();
        Object oldRoot = this.root;
	this.root = root;
        if (root == null && oldRoot != null) {
            this.fireTreeStructureChanged(this, null);
        }
        else {
            this.nodeStructureChanged(root);
        }
    }
    
    /**
     * Invoke this method if you've totally changed the children of
     * node and its childrens children...  This will post a
     * treeStructureChanged event.
     */
   public void nodeStructureChanged(TreeNode node) {	   
	   System.out.println("GraphTreeModel.nodeStructureChanged() | node " + node);
       if(node != null) {
    	   System.out.println("GraphTreeModel.nodeStructureChanged()");
          fireTreeStructureChanged(this, this.getPathToRoot(node), null, null);
       }
   }
    
    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {   
    	System.out.println("GraphTreeModel.fireTreeStructureChanged() | " + path);
    	TreeModelEvent e = null;
        for (TreeModelListener listener : this.treeModelListeners) {
			// Lazily create the event:
			if (e == null) e = new TreeModelEvent(source, path);
			listener.treeStructureChanged(e);
		}
    }

	public boolean isLeaf(Object node) {
		return this.getChildCount(node) == 0;
	}

	/**
	 * Invoke this method after you've changed how node is to be represented in
	 * the tree.
	 */
	public void nodeChanged(TreeNode node) {
		if (this.treeModelListeners != null && node != null) {
			DefaultGraphCell cell = (DefaultGraphCell) node;

			TreeNode[] pathToRoot = this.getPathToRoot(node);
			if (pathToRoot != null && pathToRoot.length > 1) {
				DefaultGraphCell parent = (DefaultGraphCell) pathToRoot[pathToRoot.length - 2];
				int anIndex = this.getIndexOfChild(parent, cell);
				if (anIndex != -1) {
					int[] cIndexs = new int[1];

					cIndexs[0] = anIndex;
					this.nodesChanged(parent, cIndexs);
				}
			} else if (node == this.getRoot()) {
				this.nodesChanged(node, null);
			}
		}
	}

	/**
	 * Invoke this method after you've changed how the children identified by
	 * childIndicies are to be represented in the tree.
	 */
	public void nodesChanged(TreeNode node, int[] childIndices) {
		if (node != null) {
			if (childIndices != null) {
				int cCount = childIndices.length;

				if (cCount > 0) {
					Object[] cChildren = new Object[cCount];

					for (int counter = 0; counter < cCount; counter++) {
						cChildren[counter] = this.getChild(node, childIndices[counter]);
					}
					this.fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
				}
			} else if (node == getRoot()) {
				this.fireTreeNodesChanged(this, getPathToRoot(node), null, null);
			}
		}
	}

	protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (TreeModelListener listener : this.treeModelListeners) {
			// Lazily create the event:
			if (e == null) e = new TreeModelEvent(source, path, childIndices, children);
			listener.treeNodesChanged(e);
		}
	}

	/**
	 * Invoked this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate event.
	 * This is the preferred way to add children as it will create the
	 * appropriate event.
	 */
	public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent) {
		int index = this.getIndexOfChild(parent, newChild);
//		System.out.println("GraphTreeModel.insertNodeInto() | parent:" + parent + "\n\t index of '" + newChild + "':"
//				+ index);
		this.fireTreeNodesInserted(this, getPathToRoot(parent), new int[] {index},
			new Object[] {newChild});
	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param source
	 *            the node where new elements are being inserted
	 * @param path
	 *            the path to the root node
	 * @param childIndices
	 *            the indices of the new elements
	 * @param children
	 *            the new elements
	 * @see EventListenerList
	 */
	protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;
		for (TreeModelListener listener : this.treeModelListeners) {
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
			listener.treeNodesInserted(e);
		}
	}

	/**
	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the preferred
	 * way to remove a node as it handles the event creation for you.
	 */
	public void removeNodeFromParent(MutableTreeNode node) {
		int index = this.treeNodeIndexMap.get(node);		
		if (index == 0) {
			System.err.println("GraphTreeModel.removeNodeFromParent() | cache is null, return");
			return;
		}
		
		TreeNode[] nodes = this.treeNodePathMap.get(node);	
		
		assert nodes != null : ErrorMessages.NON_NULL_EXPECTED;
		
		if (nodes.length <= 1) {
			throw new IllegalArgumentException("node does not have a parent.");
		}

		this.nodesWereRemoved(nodes[nodes.length - 2], 
				new int[] {index}, 
				new Object[] {node});
	}

	/**
	 * Invoke this method after you've removed some TreeNodes from node.
	 * childIndices should be the index of the removed elements and must be
	 * sorted in ascending order. And removedChildren should be the array of the
	 * children objects that were removed.
	 */
	public void nodesWereRemoved(TreeNode node, int[] childIndices, Object[] removedChildren) {
		if (node != null && childIndices != null) {
			System.out.println("GraphTreeModel.nodesWereRemoved() | " + removedChildren[0] + " from " + node 
					+ " at " + childIndices[0]);			
//			if (this.treeNodePathMap.get(node) == null) {
//				System.err.println("GraphTreeModel.nodesWereRemoved() | node path for '" + node + "' is null");
//			}
			this.fireTreeNodesRemoved(this, this.treeNodePathMap.get(node), childIndices, removedChildren);
		}
	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param source
	 *            the node where elements are being removed
	 * @param path
	 *            the path to the root node
	 * @param childIndices
	 *            the indices of the removed elements
	 * @param children
	 *            the removed elements
	 * @see EventListenerList
	 */
	protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;	
		for (TreeModelListener listener : this.treeModelListeners) {
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
			listener.treeNodesRemoved(e);
		}
	}

	protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;
		for (TreeModelListener listener : this.treeModelListeners) {
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
				System.out.println("GraphTreeModel.fireTreeStructureChanged() | " + e);
			}
			listener.treeStructureChanged(e);
		}
	}

	public TreeNode[] getPathToRoot(TreeNode aNode) {
//		System.out.println("GraphTreeModel.getPathToRoot() | " + aNode + '[' + aNode.getClass().getName() + ']');
		TreeNode[] nodes = this.treeNodePathMap.get(aNode);
		if (nodes != null) {
			return nodes;
		}
		nodes = this.getPathToRoot(aNode, 0);
		this.treeNodePathMap.put(aNode, nodes);
		return nodes;
	}

	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[] retNodes = null;
		if (aNode instanceof Edge) { return null; }

		DefaultGraphCell cell = (DefaultGraphCell) aNode;
		// This method recurses, traversing towards the root in order
		// size the array. On the way back, it fills in the nodes,
		// starting from the root and working back to the original node.

		/*
		 * Check for null, in case someone passed in a null node, or they passed
		 * in an element that isn't rooted at root.
		 */
		if (cell == null) {
			if (depth == 0) { return null; }
			retNodes = new TreeNode[depth];
		} else {
			depth++;
			if (cell == this.root) {
				retNodes = new TreeNode[depth];
			} else {
				DefaultPort port = (DefaultPort) cell.getChildAt(0);

				DefaultPort rootPort = (DefaultPort) this.root.getChildAt(0);

				Set edges = port.getEdges();

				int nonRootSource = 0;

				for (Object object : edges) {
					Edge edge = (Edge) object;
					Object source = this.direct ? edge.getSource() : edge.getTarget();
					Object target = this.direct ? edge.getTarget() : edge.getSource();

					if (target == port && source != rootPort) {
						nonRootSource++;
					}
				}

				for (Object object : edges) {
					Edge edge = (Edge) object;
					Object source = this.direct ? edge.getSource() : edge.getTarget();
					Object target = this.direct ? edge.getTarget() : edge.getSource();

					if (target == port && (source != rootPort || nonRootSource == 0)) {
						retNodes = getPathToRoot(((DefaultPort) source).getParent(), depth);
					}
				}

			}
			if (retNodes != null) {
				retNodes[retNodes.length - depth] = aNode;
			}
		}
		return retNodes;
	}

	public void reload(TreeNode node) {
		if (node != null) {
			this.clearCache();
			this.fireTreeStructureChanged(this, this.getPathToRoot(node), null, null);
		}
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	
	public final boolean isDirect() {
		return this.direct;
	}
}
