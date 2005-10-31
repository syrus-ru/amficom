/*-
 * $Id: ItemTreeModel.java,v 1.24 2005/10/31 12:30:03 bass Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/10/31 12:30:03 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public final class ItemTreeModel implements TreeModel, ItemListener {

	protected static class NameItemComparator implements Comparator<Item> {

		public int compare(final Item item1, 
		                   final Item item2) {
			final String string1 = item1.getName();
			final String string2 = item2.getName();
//			Log.debugMessage(item1 + " to " + item2 
//					+ " as '" + string1 + "' to '" + string2 + '\'',
//				Log.DEBUGLEVEL10);
			return (string1 == null ? "" : string1).compareTo((string2 == null ? "" : string2));
		}
	}

	protected class SortList {

		boolean sorted;
		List<Item> list;

		public SortList() {
			this.list = new ArrayList<Item>();
			this.sorted = false;
		}
	}

	private List<TreeModelListener> listeners;
	private Item root;
	private Map<Item, SortList> parentSortedChildren = new HashMap<Item, SortList>();

	private boolean allwaysSort = true;

	private static NameItemComparator nameItemComparator;

	public ItemTreeModel(final Item root) {
		this.root = root;
		this.root.addChangeListener(this.getItemListener());
	}

	protected static NameItemComparator getNameItemComparator() {
		if (nameItemComparator == null) {
			nameItemComparator = new NameItemComparator();
		}
		return nameItemComparator;
	}

	/**
	 * The only event raised by this model is TreeStructureChanged with the root
	 * as path, i.e. the whole tree has changed.
	 */
	protected void fireTreeStructureChanged(final Item oldRoot) {
		final TreeModelEvent e = new TreeModelEvent(this, new Object[] { oldRoot});
		for (final TreeModelListener listener : this.listeners) {
			listener.treeStructureChanged(e);
		}
	}

	public synchronized void addTreeModelListener(final TreeModelListener treeModelListener) {
		if (this.listeners == null) {
			this.listeners = new LinkedList<TreeModelListener>();
		}
		this.listeners.add(treeModelListener);

	}

	public synchronized void removeTreeModelListener(final TreeModelListener treeModelListener) {
		if (this.listeners != null) {
			this.listeners.remove(treeModelListener);
		}
	}

	protected synchronized List getChildren(final Item parent) {
		SortList sortList = this.parentSortedChildren.get(parent);
		if (sortList == null) {
			sortList = new SortList();		
			this.parentSortedChildren.put(parent, sortList);
		}
		if (!sortList.sorted) {
			final List<Item> children = parent.getChildren();
			Log.debugMessage(parent + " > " + sortList.list, Log.DEBUGLEVEL10);
			if (sortList.list.isEmpty() && !children.isEmpty()) {
				sortList.list.addAll(children);
				for (final Iterator<Item> it = sortList.list.iterator(); it.hasNext();) {
					final Item item = it.next();
					Log.debugMessage("parent " + parent + ", child " + item, Log.DEBUGLEVEL10);
					if (item.isService()) {
						it.remove();
					}
				}
			}
			if (this.allwaysSort) {
				Collections.sort(sortList.list, getNameItemComparator());
			}
			sortList.sorted = true;
		}
		return sortList.list;
	}

	public Object getChild(final Object parent, final int index) {
		final Object object = this.getChildren((Item) parent).get(index);
//		Log.debugMessage("parent:" + parent + ", index: " + index + ", child:" + object, Log.DEBUGLEVEL10);
		return object;
	}

	public int getChildCount(final Object parent) {
		final int count = this.getChildren((Item) parent).size();
//		Log.debugMessage("parent:" + parent + ", count: " + count, Log.DEBUGLEVEL10);
		return count;
	}

	public int getIndexOfChild(final Object parent, final Object child) {
		final List<Item> children = ((Item) parent).getChildren();
		return children.indexOf(child);
	}

	public Object getRoot() {
		return this.root;
	}

	public boolean isLeaf(final Object node) {
		return !((Item) node).canHaveChildren();
	}

	//TODO: remove this method, if it's useless	
	public void valueForPathChanged(final TreePath path, final Object newValue) {
//		System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);
	}

	/**
	 * Invoked this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate event.
	 * This is the preferred way to add children as it will create the
	 * appropriate event.
	 */
	protected void insertNodeInto(final Item parent, final Item child) {
		final int childCount = this.getChildCount(parent);
		final int[] newIndexs = new int[1];
		final Object[] objects = new Object[] { child };
		for (int i = 0; i < childCount; i++) {
			if (this.getChild(parent, i).equals(child)) {
				newIndexs[0] = i;
				break;
			}
		}
		Log.debugMessage("insert " + child.getName() + " to " + parent.getName() + " [ " + newIndexs[0] + " ] ", Log.DEBUGLEVEL10);
		nodesWereInserted(parent, objects, newIndexs);
	}

	/**
	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the preferred way
	 * to remove a node as it handles the event creation for you.
	 */
	protected void removeNodeFromParent(final Item parent, final Item child) {
		final SortList sortList = this.parentSortedChildren.get(parent);
		final int[] childIndex;
		if (sortList != null) {
			Log.debugMessage("sortList:" + sortList.list, Log.DEBUGLEVEL10);
			final int index = sortList.list.indexOf(child);
			sortList.list.remove(index);
			childIndex = new int[] {index};
			Log.debugMessage("sortList:" + sortList.list, Log.DEBUGLEVEL10);
		} else {
			Log.debugMessage("sortList == null", Log.DEBUGLEVEL10);
			final int childCount = this.getChildCount(parent);
			childIndex = new int[1];
			for (int i = 0; i < childCount; i++) {
				if (this.getChild(parent, i).equals(child)) {
					childIndex[0] = i;
					break;
				}
			}
		}
		Object[] removedArray = new Object[] { child };
		Log.debugMessage("delete " + child.getName() + " from " + parent.getName() + " [ " + childIndex[0] + " ] ", Log.DEBUGLEVEL10);
		this.nodesWereRemoved(parent, childIndex, removedArray);
	}

	/**
	 * Invoke this method after you've changed how node is to be represented in
	 * the tree.
	 */
	protected void nodeChanged(final Item node) {
		if (this.listeners != null && node != null) {
			Item parent = node.getParent();

			if (parent != null) {
				int anIndex = parent.getChildren().indexOf(node);
				if (anIndex != -1) {
					final int[] cIndexs = new int[1];

					cIndexs[0] = anIndex;
					nodesChanged(parent, cIndexs);
				}
			} else if (node == this.getRoot()) {
				nodesChanged(node, null);
			}
		}
	}

	/**
	 * Invoke this method after you've changed how the children identified by
	 * childIndicies are to be represented in the tree.
	 */
	protected void nodesChanged(final Item node, final int[] childIndices) {
		if (node != null) {
			if (childIndices != null) {
				int cCount = childIndices.length;

				if (cCount > 0) {
					final Object[] cChildren = new Object[cCount];

					for (int counter = 0; counter < cCount; counter++) {
						cChildren[counter] = node.getChildren().get(childIndices[counter]);
					}
					fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
				}
			} else if (node == this.getRoot()) {
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
	 */
	protected void fireTreeNodesChanged(final Object source, final Object[] path, final int[] childIndices, final Object[] children) {
		TreeModelEvent e = null;
		for (final TreeModelListener listener : this.listeners) {
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
	protected void nodesWereInserted(final Item node, final Object[] newChildren, final int[] childIndices) {
		if (this.listeners != null && node != null && childIndices != null && childIndices.length > 0) {
			this.fireTreeNodesInserted(this, getPathToRoot(node), childIndices, newChildren);
		}
	}
	
	protected void sortChildren(final Item parent) {
		Log.debugMessage("parent:" + parent, Log.DEBUGLEVEL10);
		if (this.listeners != null && parent != null) {

			final List<Item> children2 = parent.getChildren();
			for (final Item item : children2) {
				this.sortChildren(item);
			}

			final int childCount = this.getChildCount(parent);
			final int[] childIndices = new int[childCount];
			final Object[] children = new Object[childCount];
			for (int i = 0; i < childCount; i++) {
				childIndices[i] = i;
				children[i] = this.getChild(parent, i);
			}

			this.fireTreeNodesRemoved(this, getPathToRoot(parent), childIndices, children);

			final SortList sortList = this.parentSortedChildren.get(parent);
			if (sortList != null) {
				sortList.list.clear();
				sortList.list.addAll(parent.getChildren());
				sortList.sorted = false;
			}

			for (int i = 0; i < childCount; i++) {
				childIndices[i] = i;
				children[i] = this.getChild(parent, i);
			}

			this.fireTreeNodesInserted(this, getPathToRoot(parent), childIndices, children);
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
	 */
	protected void fireTreeNodesInserted(final Object source, final Object[] path, final int[] childIndices, final Object[] children) {
		TreeModelEvent e = null;
		for (final TreeModelListener listener : this.listeners) {
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
	protected void nodesWereRemoved(final Item node, final int[] childIndices, final Object[] removedChildren) {
//		Log.debugMessage("node:" + node + ", childIndices[0]:" + childIndices[0], Log.DEBUGLEVEL10);
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
	 */
	protected void fireTreeNodesRemoved(final Object source, final Object[] path, final int[] childIndices, final Object[] children) {
//		Log.debugMessage(Log.DEBUGLEVEL10);
		TreeModelEvent e = null;
		for (final TreeModelListener listener : this.listeners) {
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
//			Log.debugMessage("event " + e + " > listener " + listener, Log.DEBUGLEVEL10);
			listener.treeNodesRemoved(e);
		}
	}

	public Item[] getPathToRoot(final Item aNode) {
		return this.getPathToRoot(aNode, 0);
	}

	protected Item[] getPathToRoot(final Item aNode, int depth) {
		Item[] retNodes;
		// This method recurses, traversing towards the root in order
		// size the array. On the way back, it fills in the nodes,
		// starting from the root and working back to the original node.

		/*
		 * Check for null, in case someone passed in a null node, or they passed
		 * in an element that isn't rooted at root.
		 */
		if (aNode == null) {
			retNodes = new Item[depth];
		} else {
			depth++;
			if (aNode == this.root) {
				retNodes = new Item[depth];
			}
			else {
				Item parent = aNode.getParent();
				retNodes = this.getPathToRoot(parent, depth);
			}
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	protected void addItem(final Item parentItem, final Item childItem) {
		final Item parent = (parentItem == null) ? this.root : parentItem;
		final SortList sortList = this.parentSortedChildren.get(parent);
		if (sortList != null) {
			sortList.sorted = false;
			if (!sortList.list.contains(childItem)) {
				sortList.list.add(childItem);
			}
		}
		this.addObject(parent, childItem);
		for (final Item item1 : childItem.getChildren()) {
			this.addItem(childItem, item1);
		}
	}

	Item getItemNode(Item parent, final Item item) {
		if (parent == null) {
			parent = this.root;
		}
		Log.debugMessage("parent is '" + parent.getName()
				+ "', item is '" + item.getName() + "'", Log.DEBUGLEVEL10);
		final List<Item> children = parent.getChildren();
		Item node = null;
		for (final Item item2 : children) {
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
//		SwingUtilities.invokeLater(new Runnable() {
//
//			public void run() {
				insertNodeInto(parent, child);
//			}
//		});
	}

	public void setParentPerformed(final Item item, 
	                               final Item oldParent, 
	                               final Item newParent) {
		Log.debugMessage("item:" 
				+ item + ", oldParent:" 
				+ oldParent + ", newParent:" 
				+ newParent,
			Log.DEBUGLEVEL10);
		if (oldParent != null) {
			this.removeNodeFromParent(oldParent, item);
		}
		if (newParent != null) {
			if (newParent instanceof AbstractItem) {
				AbstractItem abstractItem = (AbstractItem) newParent;
				abstractItem.print(0);
			}
			this.addItem(newParent, item);
		}
//		else {
//			Log.debugMessage("remove " + oldParent + " from parentSortedChildren",
//				Log.DEBUGLEVEL10);
//			this.parentSortedChildren.remove(oldParent);
//		}
	}	

	public void setObjectNameChanged(final Item item, final String oldName, final String newName) {
		this.nodeChanged(item);
	}

	public ItemListener getItemListener() {
		return this;
	}

	/**
	 * @return Returns the allwaysSort.
	 */
	public boolean isAllwaysSort() {
		return this.allwaysSort;
	}

	/**
	 * @param allwaysSort
	 *            The allwaysSort to set.
	 */
	public void setAllwaysSort(final boolean allwaysSort) {
		this.allwaysSort = allwaysSort;
		if (allwaysSort) {
			this.sortChildren(this.root);
		}
	}

}
