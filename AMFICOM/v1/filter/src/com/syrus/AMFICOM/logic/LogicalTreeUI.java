/*
 * $Id: LogicalTreeUI.java,v 1.1 2005/03/10 15:17:48 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * @version $Revision: 1.1 $, $Date: 2005/03/10 15:17:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter_v1
 */
public class LogicalTreeUI implements SelectionListener, AddDeleteItems {

	private class ItemTreeCellRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(	JTree tree1,
														Object value,
														boolean selected1,
														boolean expanded,
														boolean leaf,
														int row,
														boolean hasFocus1) {
			Component treeCellRendererComponent = super.getTreeCellRendererComponent(tree1, value, selected1, expanded,
				leaf, row, hasFocus1);

			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
				Object userObject = treeNode.getUserObject();
				if (userObject instanceof Item) {
					Item item = (Item) userObject;
					((JLabel) treeCellRendererComponent).setText(item.getName());
				}
			}

			return treeCellRendererComponent;
		}
	}
	DefaultTreeModel				treeModel;
	private ItemListener			itemListener;

	private DefaultMutableTreeNode	root;
	private List					rootItems;

	private JTree					tree;
	private JPanel					panel;
	private static final Insets		nullInsets			= new Insets(0, 0, 0, 0);

	SelectionListener[]				selectionListeners	= new SelectionListener[0];

	public LogicalTreeUI(List rootItems) {
		this.rootItems = rootItems;
	}

	void addItem(	DefaultMutableTreeNode parent,
					Item item) {
		if (parent == null) {
			parent = this.root;
		}
		DefaultMutableTreeNode node = this.addObject(parent, item);
		if (item.getChildren() != null) {
			for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
				Item item1 = (Item) it.next();
				addItem(node, item1);
			}
		}
	}

	DefaultMutableTreeNode getItemNode(	DefaultMutableTreeNode parent,
										Item item) {

		DefaultMutableTreeNode node = null;
		if (parent == null) {
			parent = this.root;
		}
		for (int i = 0; i < parent.getChildCount() && node == null; i++) {
			DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) parent.getChildAt(i);
			Object userObject = node2.getUserObject();
			if (userObject.equals(item)) {
				node = node2;
				break;
			}
			node = this.getItemNode(node2, item);
		}

		return node;
	}

	private void addChangeListener(List items) {
		for (Iterator it = items.iterator(); it.hasNext();) {
			Item item = (Item) it.next();
			item.addChangeListener(this.itemListener);
			List children = item.getChildren();
			if (children != null)
				this.addChangeListener(children);
		}
	}

	private DefaultMutableTreeNode addObject(	DefaultMutableTreeNode parent,
												Object child) {
		return addObject(parent, child, false);
	}

	private DefaultMutableTreeNode addObject(	DefaultMutableTreeNode parent,
												Object child,
												boolean shouldBeVisible) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

		if (parent == null) {
			parent = this.root;
		}

		this.treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			this.tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	public JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new GridBagLayout());

			JButton expandButton = new JButton("v");
			expandButton.setToolTipText("expand all");
			expandButton.setMargin(nullInsets);

			expandButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					expandAll(true);
				}
			});

			JButton collapseButton = new JButton("^");
			collapseButton.setToolTipText("collapse all");
			collapseButton.setMargin(nullInsets);

			collapseButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					expandAll(false);
				}
			});

			Box box = new Box(BoxLayout.X_AXIS);
			box.add(Box.createGlue());
			box.add(expandButton);
			box.add(collapseButton);

			JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
														ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.getViewport().add(this.getTree());

			GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;

			this.panel.add(box, gbc);
			gbc.weighty = 1.0;
			gbc.gridheight = GridBagConstraints.RELATIVE;
			this.panel.add(scrollPane, gbc);
			expandButton.doClick();
		}

		return this.panel;
	}

	public void addSelectionListener(SelectionListener selectionListener) {
		SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length + 1];
		System.arraycopy(this.selectionListeners, 0, selectionListeners1, 1, this.selectionListeners.length);
		selectionListeners1[0] = selectionListener;
		this.selectionListeners = selectionListeners1;
	}

	public void removeSelectionListener(SelectionListener selectionListener) {
		int index = -1;
		for (int i = 0; i < this.selectionListeners.length; i++) {
			if (this.selectionListeners[i].equals(selectionListener)) {
				index = i;
				break;
			}
		}
		if (index >= -1) {
			SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length - 1];
			System.arraycopy(this.selectionListeners, 0, selectionListeners1, 0, index);
			System.arraycopy(this.selectionListeners, index + 1, selectionListeners1, index, selectionListeners1.length
					- index);
			this.selectionListeners = selectionListeners1;
		}
	}

	public JTree getTree() {
		if (this.tree == null) {
			this.root = new DefaultMutableTreeNode(".");
			this.treeModel = new DefaultTreeModel(this.root);
			this.tree = new JTree(this.treeModel);
			this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			this.tree.setRootVisible(true);
			for (Iterator it = this.rootItems.iterator(); it.hasNext();) {
				this.addItem(null, (Item) it.next());
			}

			this.tree.addTreeSelectionListener(new TreeSelectionListener() {

				public void valueChanged(TreeSelectionEvent e) {
					JTree jTree = (JTree) e.getSource();
					TreePath[] selectionPaths = jTree.getSelectionPaths();
					if (selectionPaths == null)
						return;
					Collection items = new ArrayList(selectionPaths.length);
					for (int j = 0; j < selectionPaths.length; j++) {
						DefaultMutableTreeNode itemNode = (DefaultMutableTreeNode) selectionPaths[j]
								.getLastPathComponent();
						Object userObject = itemNode.getUserObject();
						if (userObject instanceof Item) {
							Item item = (Item) userObject;
							items.add(item);
						}
					}
					for (int i = 0; i < LogicalTreeUI.this.selectionListeners.length; i++) {
						LogicalTreeUI.this.selectionListeners[i].selectedItems(items);
					}
				}
			});

			this.tree.setCellRenderer(new ItemTreeCellRenderer());
			this.itemListener = new ItemListener() {

				public void addChildPerformed(	Item source,
												Item childItem) {
					DefaultMutableTreeNode itemNode = getItemNode(null, childItem);
					if (itemNode != null) {
						MutableTreeNode parent = (MutableTreeNode) (itemNode.getParent());
						if (parent != null) {
							LogicalTreeUI.this.treeModel.removeNodeFromParent(itemNode);
							itemNode = getItemNode(null, source);
							addItem(itemNode, childItem);
						}
					}

				}

				public void addParentPerformed(	Item source,
												Item parent) {
					// nothing else
				}

				public void removeChildPerformed(	Item source,
													Item childItem) {
					DefaultMutableTreeNode itemNode = getItemNode(null, childItem);
					if (itemNode != null) {
						MutableTreeNode parent = (MutableTreeNode) (itemNode.getParent());
						if (parent != null) {
							LogicalTreeUI.this.treeModel.removeNodeFromParent(itemNode);
							addItem(null, childItem);
							return;
						}
					}

				}

				public void removeParentPerformed(	Item source,
													Item parent) {
					// nothing else
				}
			};

			this.addChangeListener(this.rootItems);
		}
		return this.tree;
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandAll(boolean expand) {
		// Traverse tree from root
		expandAll(new TreePath(this.root), expand);
	}

	public void selectedItems(Collection items) {
		TreePath[] treePaths = new TreePath[items.size()];
		this.tree.clearSelection();
		int i = 0;
		for (Iterator it = items.iterator(); it.hasNext(); i++) {
			Item item = (Item) it.next();
			List treePath = this.getTreePath(null, item);
			Object[] objects = new Object[treePath.size() + 1];
			int j = objects.length;
			for (Iterator iter = treePath.iterator(); iter.hasNext();) {
				Item item2 = (Item) iter.next();
				objects[--j] = this.getItemNode(null, item2);
			}
			objects[--j] = this.root;
			treePaths[i] = new TreePath(objects);
		}
		this.tree.setSelectionPaths(treePaths);
	}

	private List getTreePath(	List objects,
								Item item) {
		if (objects == null)
			objects = new LinkedList();
		objects.add(item);
		List parents = item.getParents();
		if (parents != null && !parents.isEmpty()) {
			getTreePath(objects, (Item) parents.get(0));
		}
		return objects;
	}

	private void expandAll(	TreePath parent,
							boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			this.tree.expandPath(parent);
		} else {
			this.tree.collapsePath(parent);
		}
	}

	public void addItem(Item item) {
		item.addChangeListener(this.itemListener);
		this.addItem(null, item);
	}

	public void deleteItem(Item item) {
		synchronized (this.treeModel) {
			DefaultMutableTreeNode itemNode = getItemNode(null, item);
			if (itemNode != null) {
				MutableTreeNode parent = (MutableTreeNode) (itemNode.getParent());
				if (parent != null) {
					List children = item.getChildren();
					if (children != null && !children.isEmpty()) {
						for (Iterator it = children.iterator(); it.hasNext();) {
							Item item2 = (Item) it.next();
							this.addItem(null, item2);
						}
					}
					this.treeModel.removeNodeFromParent(itemNode);
					item.removeChangeListener(this.itemListener);
				}
			}

		}
	}

}
