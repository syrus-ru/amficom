/*
 * $Id: LogicalTreeUI.java,v 1.7 2005/03/21 13:04:06 bob Exp $
 *
 * Copyright ? 2005 Syrus Systems.
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
import java.util.Collections;
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
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * @version $Revision: 1.7 $, $Date: 2005/03/21 13:04:06 $
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

			if (value instanceof Item) {
				Item item = (Item) value;
				((JLabel) treeCellRendererComponent).setText(item.getName());
			}

			return treeCellRendererComponent;
		}
	}
	private static final Insets	nullInsets	= new Insets(0, 0, 0, 0);

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
		tx.rotate(angle * Math.PI / 180.0, img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		Icon icon = new ImageIcon(img);
		return icon;
	}
	private JPanel		panel;
	// private ItemListener itemListener;

	// private DefaultMutableTreeNode root;
	private Item		rootItem;

	private JTree		tree;

	SelectionListener[]	selectionListeners	= new SelectionListener[0];

	ItemTreeModel		treeModel;

	public LogicalTreeUI(Item rootItem) {
		this.setRootItem(rootItem);
	}

	public void addItem(Item item) {
		this.addItem(null, item);
	}

	public void addItem(Item parentItem,
						Item childItem) {
		if (this.treeModel == null) {
			if (this.rootItem == null) {
				this.rootItem = new ServiceItem("/");
			}
			this.treeModel = new ItemTreeModel(this.rootItem);
		}
		childItem.addChangeListener(this.treeModel.getItemListener());
		this.treeModel.addItem(parentItem == null ? this.rootItem : parentItem, childItem);
	}

	public void addSelectionListener(SelectionListener selectionListener) {
		SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length + 1];
		System.arraycopy(this.selectionListeners, 0, selectionListeners1, 1, this.selectionListeners.length);
		selectionListeners1[0] = selectionListener;
		this.selectionListeners = selectionListeners1;
	}

	public void deleteItem(Item item) {
		/* TODO */

		// synchronized (this.treeModel) {
		// DefaultMutableTreeNode itemNode = getItemNode(null, item);
		// if (itemNode != null) {
		// MutableTreeNode parent = (MutableTreeNode) (itemNode
		// .getParent());
		// if (parent != null) {
		// List children = item.getChildren();
		// if (children != null && !children.isEmpty()) {
		// for (Iterator it = children.iterator(); it.hasNext();) {
		// Item item2 = (Item) it.next();
		// this.addItem2Node(null, item2);
		// }
		// }
		// this.treeModel.removeNodeFromParent(itemNode);
		// item.removeChangeListener(this.itemListener);
		// }
		// }
		//
		// }
	}

	/**
	 * @param expand
	 *            is true, expands all nodes in the tree, Otherwise, collapses
	 *            all nodes in the tree.
	 */
	public void expandAll(boolean expand) {
		// Traverse tree from root
		if (this.rootItem != null)
			expandAll(new TreePath(this.rootItem), null, expand);
	}

	public void expandAll(Item item) {
		// Traverse tree from root
		if (this.rootItem != null)
			expandAll(new TreePath(this.rootItem), item, true);
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

	public JTree getTree() {
		if (this.tree == null) {
			if (this.treeModel == null) {
				if (this.rootItem == null) {
					this.rootItem = new ServiceItem("/");
				}
				this.treeModel = new ItemTreeModel(this.rootItem);
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
						Item itemNode = (Item) selectionPaths[j].getLastPathComponent();
						// Object userObject = itemNode.getUserObject();
						// if (userObject instanceof Item) {
						// Item item = (Item) userObject;
						items.add(itemNode);
						// }
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

	public void selectedItems(final Collection items) {
		final TreePath[] treePaths = new TreePath[items.size()];
		this.tree.clearSelection();
		int i = 0;
		for (Iterator it = items.iterator(); it.hasNext(); i++) {
			Item item = (Item) it.next();
			List treePath = this.getTreePath(null, item);
			Object[] objects = new Object[treePath.size()];
			int j = objects.length;
			for (Iterator iter = treePath.iterator(); iter.hasNext();) {
				Item item2 = (Item) iter.next();
				objects[--j] = getItemNode(null, item2);
			}
			treePaths[i] = new TreePath(objects);
		}
		final JTree tree1 = this.tree;
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				tree1.setSelectionPaths(treePaths);
			}
		});
	}

	public void setRootItem(Item rootItem) {
		this.rootItem = rootItem;
		if (this.treeModel == null) {
			this.treeModel = new ItemTreeModel(this.rootItem);
		}
		List children = this.rootItem.getChildren();
		if (children != null) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				Item item = (Item) it.next();
				this.addItem(item);
			}
		}
		this.addChangeListener(Collections.singletonList(this.rootItem));
	}

	private void addChangeListener(Collection items) {
		ItemListener itemListener1 = this.treeModel.getItemListener();
		for (Iterator it = items.iterator(); it.hasNext();) {
			Item item = (Item) it.next();
			item.addChangeListener(itemListener1);
			System.out.println("addChangeListener | " + item.getName());
			List children = item.getChildren();
			this.addChangeListener(children);
		}
	}

	private Item addObject(	Item parent,
							Item child) {
		return addObject(parent, child, false);
	}

	private Item addObject(	Item parent1,
							final Item child,
							boolean shouldBeVisible) {
		if (parent1 == null) {
			parent1 = this.rootItem;
		}
		final Item parent = parent1;
		if (this.treeModel == null) {
			this.treeModel = new ItemTreeModel(this.rootItem);
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				LogicalTreeUI.this.treeModel.insertNodeInto(parent, child);
			}
		});

		// Make sure the user can see the lovely new node.
		// if (shouldBeVisible) {
		// final JTree tree1 = this.getTree();
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// public void run() {
		//
		// tree1
		// .scrollPathToVisible(new TreePath(childNode
		// .getPath()));
		// }
		// });
		// }
		return child;
	}

	private void expandAll(	TreePath parent,
							Item item,
							boolean expand) {
		// Traverse children
		Item node = (Item) parent.getLastPathComponent();
		if (item != null && node.equals(item))
			return;
		List children = node.getChildren();
		for (Iterator it = children.iterator(); it.hasNext();) {
			Item n = (Item) it.next();
			TreePath path = parent.pathByAddingChild(n);
			expandAll(path, item, expand);
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			this.tree.expandPath(parent);
		} else {
			this.tree.collapsePath(parent);
		}
	}

	private List getTreePath(	List objects,
								Item item) {
		if (objects == null) {
			objects = new LinkedList();
		}
		// System.out.println("getTreePath | item " + (item == null ? "'null'" :
		// item.getName()));
		objects.add(item);
		Item parent = item.getParent();
		if (parent != null) {
			getTreePath(objects, parent);
		}
		return objects;
	}

	Item getItemNode(	Item parent,
						Item item) {

		Item node = null;
		if (parent == null) {
			parent = this.rootItem;
		}
		if (parent.equals(item))
			return parent;
		List children = parent.getChildren();
		if (children != null) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				Item item2 = (Item) it.next();
				if (item2.equals(item)) {
					node = item2;
					break;
				}
				node = this.getItemNode(item2, item);
				if (node != null)
					break;
			}
		}

		return node;
	}

}
