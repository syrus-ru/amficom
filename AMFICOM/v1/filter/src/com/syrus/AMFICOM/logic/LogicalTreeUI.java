/*
 * $Id: LogicalTreeUI.java,v 1.5 2005/03/16 12:58:18 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
 * @version $Revision: 1.5 $, $Date: 2005/03/16 12:58:18 $
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

	private static Icon getStringIcon(	String s,
										int angle) {
		int w = 16;
		int h = 16;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setBackground(UIManager.getColor("Button.background"));
		g2d.clearRect(0, 0, w, h);
		Font font = UIManager.getFont("Button.font");
		g2d.setFont(font);
		g2d.setColor(UIManager.getColor("Button.foreground"));		
		g2d.drawString(s, w / 4, (h / 2 + fm.getHeight()) / 2);
		AffineTransform tx = new AffineTransform();	
	    tx.rotate(angle * Math.PI / 180.0, img.getWidth()/2, img.getHeight()/2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		Icon icon = new ImageIcon(img);
		return icon;
	}

	DefaultTreeModel				treeModel;
	private ItemListener			itemListener;

	private DefaultMutableTreeNode	root;
	private Collection				rootItems;

	private JTree					tree;
	private JPanel					panel;
	private static final Insets		nullInsets			= new Insets(0, 0, 0, 0);

	SelectionListener[]				selectionListeners	= new SelectionListener[0];

	public LogicalTreeUI() {
		// empty
	}

	public LogicalTreeUI(List rootItems) {
		this.addItems(rootItems);
	}

	void addItem2Node(	DefaultMutableTreeNode parent,
						Item item) {
		if (parent == null) {
			if (this.root == null) {
				this.root = new DefaultMutableTreeNode(".");
			}
			parent = this.root;
		}
		DefaultMutableTreeNode node = this.addObject(parent, item);
		if (item.getChildren() != null) {
			for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
				Item item1 = (Item) it.next();
				addItem2Node(node, item1);
			}
		}
	}

	public void addItem(Item parentItem,
						Item childItem) {
		DefaultMutableTreeNode itemNode = getItemNode(null, parentItem);
		addItem2Node(itemNode, childItem);
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

	private void addChangeListener(Collection items) {
		ItemListener itemListener1 = this.getItemListener();
		for (Iterator it = items.iterator(); it.hasNext();) {
			Item item = (Item) it.next();
			item.addChangeListener(itemListener1);
			List children = item.getChildren();
			if (children != null)
				this.addChangeListener(children);
		}
	}

	private DefaultMutableTreeNode addObject(	DefaultMutableTreeNode parent,
												Object child) {
		return addObject(parent, child, false);
	}

	private DefaultMutableTreeNode addObject(	DefaultMutableTreeNode parent1,
												Object child,
												boolean shouldBeVisible) {
		final DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

		if (parent1 == null) {
			if (this.root == null) {
				this.root = new DefaultMutableTreeNode(".");
			}
			parent1 = this.root;
		}
		final DefaultMutableTreeNode parent = parent1;
		if (this.treeModel == null) {
			this.treeModel = new DefaultTreeModel(this.root);
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				LogicalTreeUI.this.treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
			}
		});

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			final JTree tree1 = this.getTree();
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {

					tree1.scrollPathToVisible(new TreePath(childNode.getPath()));
				}
			});
		}
		return childNode;
	}

	public JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new GridBagLayout());

			JButton expandButton = new JButton(getStringIcon("v", 0));
			expandButton.setToolTipText("expand all");
			expandButton.setMargin(nullInsets);

			expandButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					expandAll(true);
				}
			});

			JButton collapseButton = new JButton(getStringIcon("v", 180));
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
			if (this.treeModel == null) {
				if (this.root == null) {
					this.root = new DefaultMutableTreeNode(".");
				}
				this.treeModel = new DefaultTreeModel(this.root);
			}
			this.tree = new JTree(this.treeModel);
			this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			this.tree.setRootVisible(true);
			this.tree.setDragEnabled(true);

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
		}
		return this.tree;
	}

	public void addItems(Collection rootItems) {
		this.rootItems = rootItems;
		for (Iterator it = this.rootItems.iterator(); it.hasNext();) {
			this.addItem2Node(null, (Item) it.next());
		}
		this.addChangeListener(this.rootItems);
	}

	public void removeAll(Item item) {
		DefaultMutableTreeNode itemNode = item == null ? this.root : this.getItemNode(null, item);
		if (itemNode != null) {
			for (int i = 0; i < itemNode.getChildCount(); i++) {
				this.treeModel.removeNodeFromParent((MutableTreeNode) itemNode.getChildAt(i));
			}
		}
	}

	private ItemListener getItemListener() {
		if (this.itemListener == null) {
			this.itemListener = new ItemListener() {

				public void addChildPerformed(	final Item source,
												final Item childItem) {
					SwingUtilities.invokeLater(new Runnable() {

						public void run() {

							DefaultMutableTreeNode itemNode = getItemNode(null, childItem);
							if (itemNode != null) {
								MutableTreeNode parent = (MutableTreeNode) (itemNode.getParent());
								if (parent != null) {
									LogicalTreeUI.this.treeModel.removeNodeFromParent(itemNode);
									addItem(source, childItem);
								}
							}
						}
					});
				}

				public void addParentPerformed(	Item source,
												Item parent) {
					// nothing else
				}

				public void removeChildPerformed(	final Item source,
													final Item childItem) {
					SwingUtilities.invokeLater(new Runnable() {

						public void run() {

							final DefaultMutableTreeNode itemNode = getItemNode(null, childItem);
							if (itemNode != null) {
								MutableTreeNode parent = (MutableTreeNode) (itemNode.getParent());
								if (parent != null) {
									LogicalTreeUI.this.treeModel.removeNodeFromParent(itemNode);
									addItem2Node(null, childItem);
								}
							}
						}
					});

				}

				public void removeParentPerformed(	Item source,
													Item parent) {
					// nothing else
				}
			};
		}
		return this.itemListener;
	}

	/**
	 * @param expand
	 *            is true, expands all nodes in the tree, Otherwise, collapses
	 *            all nodes in the tree.
	 */
	public void expandAll(boolean expand) {
		// Traverse tree from root
		if (this.root != null)
			expandAll(new TreePath(this.root), expand);
	}

	public void selectedItems(final Collection items) {
		final TreePath[] treePaths = new TreePath[items.size()];
		this.tree.clearSelection();
		int i = 0;
		for (Iterator it = items.iterator(); it.hasNext(); i++) {
			Item item = (Item) it.next();
			List treePath = this.getTreePath(null, item);
			Object[] objects = new Object[treePath.size() + 1];
			int j = objects.length;
			for (Iterator iter = treePath.iterator(); iter.hasNext();) {
				Item item2 = (Item) iter.next();
				objects[--j] = getItemNode(null, item2);
			}
			objects[--j] = this.root;
			treePaths[i] = new TreePath(objects);
		}
		final JTree tree1 = this.tree;
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				tree1.setSelectionPaths(treePaths);
			}
		});
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
		this.addItem2Node(null, item);
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
							this.addItem2Node(null, item2);
						}
					}
					this.treeModel.removeNodeFromParent(itemNode);
					item.removeChangeListener(this.itemListener);
				}
			}

		}
	}

}
