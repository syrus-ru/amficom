/*
 * $Id: ItemTreeModel.java,v 1.2 2005/03/21 13:04:06 bob Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @version $Revision: 1.2 $, $Date: 2005/03/21 13:04:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public class ItemTreeModel implements TreeModel, ItemListener {

	protected static class NameItemComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			Item item1 = (Item) o1;
			Item item2 = (Item) o2;
			String string1 = item1.getName();
			String string2 = item2.getName();
			return (string1 == null ? "" : string1).compareTo((string2 == null ? "" : string2));
		}
	}

	protected class SortList {

		boolean	sorted	= false;
		List	list	= null;
	}

	private List						listeners;
	private Item						root;
	private Map							parentSortedChildren	= new HashMap();

	private static NameItemComparator	nameItemComparator;

	public ItemTreeModel(Item root) {
		this.root = root;
	}

	protected static NameItemComparator getNameItemComparator() {
		if (nameItemComparator == null) nameItemComparator = new NameItemComparator();
		return nameItemComparator;
	}

	/**
	 * The only event raised by this model is TreeStructureChanged with the root
	 * as path, i.e. the whole tree has changed.
	 */
	protected void fireTreeStructureChanged(Item oldRoot) {
		TreeModelEvent e = new TreeModelEvent(this, new Object[] { oldRoot});
		for (Iterator it = this.listeners.iterator(); it.hasNext();) {
			TreeModelListener listener = (TreeModelListener) it.next();
			listener.treeStructureChanged(e);
		}
	}

	public synchronized void addTreeModelListener(TreeModelListener treeModelListener) {
		if (this.listeners == null) {
			this.listeners = new LinkedList();
		}
		this.listeners.add(treeModelListener);

	}

	public synchronized void removeTreeModelListener(TreeModelListener treeModelListener) {
		if (this.listeners != null) {
			this.listeners.remove(treeModelListener);
		}
	}

	protected synchronized List getChildren(Object parent) {
		SortList sortList = (SortList) this.parentSortedChildren.get(parent);
		if (sortList == null) {
			sortList = new SortList();
			sortList.list = new LinkedList();
			this.parentSortedChildren.put(parent, sortList);
		}
		if (!sortList.sorted) {
			sortList.list.clear();
			sortList.list.addAll(((Item) parent).getChildren());
			for (Iterator it = sortList.list.iterator(); it.hasNext();) {
				Item item = (Item) it.next();
				if (item.isService()) {
					it.remove();
				}
			}
			Collections.sort(sortList.list, getNameItemComparator());
			sortList.sorted = true;
		}
		return sortList.list;
	}

	public Object getChild(Object parent, int index) {
		return this.getChildren(parent).get(index);
	}

	public int getChildCount(Object parent) {
		return this.getChildren(parent).size();
	}

	public int getIndexOfChild(Object parent, Object child) {
		List children = ((Item) parent).getChildren();
		return children.indexOf(child);
	}

	public Object getRoot() {
		return this.root;
	}

	public boolean isLeaf(Object node) {
		List children = ((Item) node).getChildren();
		return children.isEmpty();
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);

	}

	/**
	 * Invoked this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate event.
	 * This is the preferred way to add children as it will create the
	 * appropriate event.
	 */
	protected void insertNodeInto(Item parent, Item child) {
		int childCount = this.getChildCount(parent);
		int[] newIndexs = new int[1];
		Object[] objects = new Object[] {child};
		for (int i = 0; i < childCount; i++) {
			if (this.getChild(parent, i).equals(child)) {
				newIndexs[0] = i;
				break;
			}
		}
		nodesWereInserted(parent, objects, newIndexs);
	}

	/**
	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the preferred
	 * way to remove a node as it handles the event creation for you.
	 */
	protected void removeNodeFromParent(Item parent, Item child) {
		int[] childIndex = new int[1];
		int childCount = this.getChildCount(parent);
		for (int i = 0; i < childCount; i++) {
			if (this.getChild(parent, i).equals(child)) {
				childIndex[0] = i;
				break;
			}
		}
		SortList sortList = (SortList) this.parentSortedChildren.get(parent);
		if (sortList != null) {
			sortList.sorted = false;
		}
		Object[] removedArray = new Object[] { child};
		nodesWereRemoved(parent, childIndex, removedArray);
	}

	/**
	 * Invoke this method after you've changed how node is to be represented in
	 * the tree.
	 */
	protected void nodeChanged(Item node) {
		if (this.listeners != null && node != null) {
			Item parent = node.getParent();

			if (parent != null) {
				int anIndex = parent.getChildren().indexOf(node);
				if (anIndex != -1) {
					int[] cIndexs = new int[1];

					cIndexs[0] = anIndex;
					nodesChanged(parent, cIndexs);
				}
			} else if (node == getRoot()) {
				nodesChanged(node, null);
			}
		}
	}

	/**
	 * Invoke this method after you've changed how the children identified by
	 * childIndicies are to be represented in the tree.
	 */
	protected void nodesChanged(Item node, int[] childIndices) {
		if (node != null) {
			if (childIndices != null) {
				int cCount = childIndices.length;

				if (cCount > 0) {
					Object[] cChildren = new Object[cCount];

					for (int counter = 0; counter < cCount; counter++)
						cChildren[counter] = node.getChildren().get(childIndices[counter]);
					fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
				}
			} else if (node == getRoot()) {
				fireTreeNodesChanged(this, getPathToRoot(node), null, null);
			}
		}
	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param source
	 *            the node being changed
	 * @param path
	 *            the path to the root node
	 * @param childIndices
	 *            the indices of the changed elements
	 * @param children
	 *            the changed elements
	 * @see EventListenerList
	 */
	protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;
		for (Iterator it = this.listeners.iterator(); it.hasNext();) {
			TreeModelListener listener = (TreeModelListener) it.next();
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
			listener.treeNodesChanged(e);
		}
	}

	/**
	 * Invoke this method after you've inserted some TreeNodes into node.
	 * childIndices should be the index of the new elements and must be sorted
	 * in ascending order.
	 */
	protected void nodesWereInserted(Item node, Object[] newChildren, int[] childIndices) {
		if (this.listeners != null && node != null && childIndices != null && childIndices.length > 0) {
			for (int i = 0; i < childIndices.length; i++) {
				System.out.println("nodesWereInserted | to node " + node.getName() + " #" + childIndices[i] + ", " + ((Item)newChildren[i]).getName());
			}
			fireTreeNodesInserted(this, getPathToRoot(node), childIndices, newChildren);
		}
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
		for (Iterator it = this.listeners.iterator(); it.hasNext();) {
			TreeModelListener listener = (TreeModelListener) it.next();
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
			listener.treeNodesInserted(e);
		}
	}

	/**
	 * Invoke this method after you've removed some TreeNodes from node.
	 * childIndices should be the index of the removed elements and must be
	 * sorted in ascending order. And removedChildren should be the array of the
	 * children objects that were removed.
	 */
	protected void nodesWereRemoved(Item node, int[] childIndices, Object[] removedChildren) {
		if (node != null && childIndices != null) {
			fireTreeNodesRemoved(this, getPathToRoot(node), childIndices, removedChildren);
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
		for (Iterator it = this.listeners.iterator(); it.hasNext();) {
			TreeModelListener listener = (TreeModelListener) it.next();
			if (e == null) e = new TreeModelEvent(source, path, childIndices, children);
			listener.treeNodesRemoved(e);
		}
	}

	protected Item[] getPathToRoot(Item aNode) {
		return getPathToRoot(aNode, 0);
	}

	protected Item[] getPathToRoot(Item aNode, int depth) {
		Item[] retNodes;
		// This method recurses, traversing towards the root in order
		// size the array. On the way back, it fills in the nodes,
		// starting from the root and working back to the original node.

		/*
		 * Check for null, in case someone passed in a null node, or they passed
		 * in an element that isn't rooted at root.
		 */
		if (aNode == null) {
			if (depth == 0) return null;
			retNodes = new Item[depth];
		} else {
			depth++;
			if (aNode == this.root)
				retNodes = new Item[depth];
			else {
				Item parent = aNode.getParent();
				retNodes = getPathToRoot(parent, depth);
			}
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	protected void addItem(Item parentItem, Item childItem) {
		Item parent = (parentItem == null ? this.root : parentItem);
		SortList sortList = (SortList) this.parentSortedChildren.get(parent);
		if (sortList != null) {
			sortList.sorted = false;
		}
		System.out.println("addItem | parentItem:" + parentItem.getName() + ", childItem:" + childItem.getName() );
		this.addObject(parent, childItem);
		for (Iterator it = childItem.getChildren().iterator(); it.hasNext();) {
			Item item1 = (Item) it.next();
			addItem(childItem, item1);
		}
	}

	Item getItemNode(Item parent, Item item) {

		if (parent == null) {
			parent = this.root;
		}
		// System.out.println("getItemNode | parent is '" + parent.getName() +
		// "', item is '" + item.getName() + "'");
		List children = parent.getChildren();
		Item node = null;
		for (Iterator it = children.iterator(); it.hasNext();) {
			Item item2 = (Item) it.next();
			if (item2.equals(item)) {
				node = item2;
				break;
			}
			node = this.getItemNode(item2, item);
			if (node != null) {
				break;
			}
		}
		return node;
	}

	private void addObject(Item parent1, final Item child) {
		if (parent1 == null) {
			if (this.root == null) {
				this.root = new ServiceItem();
			}
			parent1 = this.root;
		}
		final Item parent = parent1;
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				insertNodeInto(parent, child);
			}
		});
	}
	
	public void setParentPerformed(Item item, Item oldParent, Item newParent) {		
		if (oldParent!=null) {
			removeNodeFromParent(oldParent, item);
		}
		if (newParent != null) {
			addItem(newParent, item);
		} else {
			this.parentSortedChildren.remove(oldParent);
		}
	}

	public ItemListener getItemListener() {		
		return this;
	}
}
