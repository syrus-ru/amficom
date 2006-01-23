/*
 * $Id: LogicalTreeUI.java,v 1.28 2006/01/23 13:32:09 bob Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.general.Identifiable;

/**
 * @version $Revision: 1.28 $, $Date: 2006/01/23 13:32:09 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public class LogicalTreeUI implements SelectionListener, AddDeleteItems, Serializable {

	private static final long serialVersionUID = 3258688823297849655L;

	protected static UIDefaults	renderers;

	protected static UIDefaults	editors;

	public static final String TRANSFERABLE_OBJECTS = "IconedTreeUI.object";

	public class ItemTreeCellRenderer implements TreeCellRenderer {

		private static final long serialVersionUID = 3762536732536615220L;

		public Component getTreeCellRendererComponent(final JTree tree1,
				final Object value,
				final boolean selected,
				final boolean expanded,
				final boolean leaf,
				final int row,
				final boolean hasFocus) {
			Class clazz = value.getClass();
			TreeCellRenderer cellRenderer = (TreeCellRenderer) renderers.get(clazz);
			if (cellRenderer != null) {
				return cellRenderer.getTreeCellRendererComponent(tree1, value, selected, expanded, leaf, row, hasFocus);
			}
			clazz = clazz.getSuperclass();
			cellRenderer = (TreeCellRenderer) renderers.get(clazz);
			if (cellRenderer == null) {
				cellRenderer = (TreeCellRenderer) renderers.get(Item.class);
			}
			return cellRenderer.getTreeCellRendererComponent(tree1, value, selected, expanded, leaf, row, hasFocus);
		}
	}

	private class ItemTreeLabelCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 3618132364363184434L;

		@Override
		public Component getTreeCellRendererComponent(final JTree tree1,
				final Object value,
				final boolean selected1,
				final boolean expanded,
				final boolean leaf,
				final int row,
				final boolean hasFocus1) {
			final Component treeCellRendererComponent = super.getTreeCellRendererComponent(tree1,
					value,
					selected1,
					expanded,
					leaf,
					row,
					hasFocus1);

			if (value instanceof Item) {
				final Item item = (Item) value;
				((JLabel) treeCellRendererComponent).setText(item.getName());
			}

			return treeCellRendererComponent;
		}
	}

	public class ItemCheckBoxTreeCellRenderer extends JCheckBox implements TreeCellRenderer {

		private static final long serialVersionUID = 3256721779866415415L;

		public Component getTreeCellRendererComponent(final JTree tree1,
				final Object value,
				final boolean selected,
				final boolean expanded,
				final boolean leaf,
				final int row,
				final boolean hasFocus) {
			final Item item = (Item) value;
			super.setText(item.getName());
			this.setForeground(tree1.getForeground());
			this.setBackground(tree1.getBackground());
			this.setSelected(selected);
			return this;
		}
	}

	public class ItemTreeCellEditor implements TreeCellEditor {

		TreeCellEditor cellEditor;
		JTree tree1;

		public ItemTreeCellEditor(final JTree tree) {
			this.tree1 = tree;
		}

		public Component getTreeCellEditorComponent(final JTree tree2,
				final Object value,
				final boolean isSelected,
				final boolean expanded,
				final boolean leaf,
				final int row) {
			if (this.cellEditor != null) {
				return this.cellEditor.getTreeCellEditorComponent(tree2, value, isSelected, expanded, leaf, row);
			}
			return null;
		}

		public void cancelCellEditing() {
			if (this.cellEditor != null) {
				this.cellEditor.cancelCellEditing();
			}
			this.clearEditor();
		}

		public boolean stopCellEditing() {
			boolean b = false;
			if (this.cellEditor != null) {
				b = this.cellEditor.stopCellEditing();
			}
			this.clearEditor();
			return b;
		}

		public Object getCellEditorValue() {
			if (this.cellEditor != null) {
				return this.cellEditor.getCellEditorValue();
			}
			return null;
		}

		public boolean isCellEditable(final EventObject anEvent) {
			if (!this.tree1.isEditable()) {
				return false;
			}
			if (anEvent != null) {
				if (anEvent.getSource() instanceof JTree) {
					if (anEvent instanceof MouseEvent) {
						final TreePath path = this.tree1.getPathForLocation(((MouseEvent) anEvent).getX(), ((MouseEvent) anEvent).getY());
						final Object value = path.getLastPathComponent();
						if (value != null) {
							Class clazz = value.getClass();
							this.cellEditor = (TreeCellEditor) editors.get(clazz);
							while (this.cellEditor == null && !clazz.equals(Object.class)) {
								final Class[] interfaces = clazz.getInterfaces();
								for (int i = 0; i < interfaces.length; i++) {
									this.cellEditor = (TreeCellEditor) editors.get(interfaces[i]);
									if (this.cellEditor != null) {
										break;
									}
								}
								if (this.cellEditor == null) {
									clazz = clazz.getSuperclass();
									this.cellEditor = (TreeCellEditor) editors.get(clazz);
								}
							}
						}
					}
				}
			}
			if (this.cellEditor != null) {
				return this.cellEditor.isCellEditable(anEvent);
			}
			return false;
		}

		public boolean shouldSelectCell(final EventObject anEvent) {
			if (this.cellEditor != null) {
				return this.cellEditor.shouldSelectCell(anEvent);
			}
			return false;
		}

		public void addCellEditorListener(final CellEditorListener l) {
			if (this.cellEditor != null) {
				this.cellEditor.addCellEditorListener(l);
			}
		}

		public void removeCellEditorListener(final CellEditorListener l) {
			if (this.cellEditor != null) {
				this.cellEditor.removeCellEditorListener(l);
			}
		}

		public void clearEditor() {
			this.cellEditor = null;
		}
	}

	public class ItemTransferHandler extends TransferHandler {
		private static final long serialVersionUID = -6467895288656251393L;

		DataFlavor flavor;

		public ItemTransferHandler() {
			this.flavor = new DataFlavor(ArrayList.class, TRANSFERABLE_OBJECTS);
		}

		@Override
		protected Transferable createTransferable(final JComponent c) {
			if (c instanceof JTree) {
				final JTree tree1 = (JTree) c;

				final TreePath[] values = tree1.getSelectionPaths();
				if (values == null || values.length == 0) {
					return null;
				}

				final ArrayList<Object> alist = new ArrayList<Object>(values.length);
				for (int i = 0; i < values.length; i++) {
					final Item item = (Item) values[i].getLastPathComponent();
					final Object o = item.getObject();
					alist.add(o instanceof Identifiable ? ((Identifiable)o).getId() : o);
				}
				return new ItemTransferable(alist);
			}
			return null;
		}

		@Override
		public int getSourceActions(final JComponent c) {
			return MOVE;
		}

		public class ItemTransferable implements Transferable {
			private List data;

			public ItemTransferable(final List alist) {
				this.data = alist;
			}

			public Object getTransferData(DataFlavor flavor1) throws UnsupportedFlavorException {
				if (!isDataFlavorSupported(flavor1)) {
					throw new UnsupportedFlavorException(flavor1);
				}
				return this.data;
			}

			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { ItemTransferHandler.this.flavor };
			}

			public boolean isDataFlavorSupported(final DataFlavor flavor1) {
				if (ItemTransferHandler.this.flavor.equals(flavor1)) {
					return true;
				}
				return false;
			}
		}
	}


	private static final Insets	NULL_INSETS	= new Insets(0, 0, 0, 0);

	public static Icon getStringIcon(	final String s,
	                                 	final int angle) {
		final int w = 16;
		final int h = 16;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g2d = (Graphics2D) img.getGraphics();
		final FontMetrics fm = g2d.getFontMetrics();
		final Font font = UIManager.getFont("Button.font");
		g2d.setFont(font);
		g2d.setColor(UIManager.getColor("Button.foreground"));
		g2d.drawString(s, w / 4, (h / 2 + fm.getHeight()) / 2);
		
		final AffineTransform tx = new AffineTransform();
		tx.rotate(angle * Math.PI / 180.0, img.getWidth() / 2, img.getHeight() / 2);
		
		final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		
		return new ImageIcon(img);
	}

	private JPanel		panel;
	// private ItemListener itemListener;

	// private DefaultMutableTreeNode root;
	private Item		rootItem;

	JTree		tree;

	SelectionListener[]	selectionListeners	= new SelectionListener[0];

	ItemTreeModel		treeModel;

	public LogicalTreeUI(final boolean useCheckedRenderer) {
		if (renderers == null) {
			renderers = new UIDefaults();
			
			renderers.put(Item.class, new UIDefaults.LazyValue() {

				public Object createValue(final UIDefaults arg0) {
					if (useCheckedRenderer) {
						return new ItemCheckBoxTreeCellRenderer();
					}
					return new ItemTreeLabelCellRenderer();
				}
			});			

		}
		if (editors == null) {
			editors = new UIDefaults();
		}
	}
	
	public LogicalTreeUI(final Item rootItem, final boolean useCheckedRenderer) {
		this(useCheckedRenderer);
		this.setRootItem(rootItem);		
	}
	
	public LogicalTreeUI(final Item rootItem) {
		this(rootItem, true);
	}

	public void addItem(Item item) {
		this.addItem(null, item);
	}
	
	public void setRenderer(final Class clazz, final TreeCellRenderer treeCellRenderer) {
		renderers.put(clazz, treeCellRenderer);
	}

	public void setEditor(final Class clazz, final TreeCellEditor treeCellEditor) {
		editors.put(clazz, treeCellEditor);
	}

	public void addItem(final Item parentItem, final Item childItem) {
		if (this.treeModel == null) {
			if (this.rootItem == null) {
				this.rootItem = new ServiceItem("/");
				this.treeModel = new ItemTreeModel(this.rootItem);
				this.rootItem.addChangeListener(this.treeModel.getItemListener());
			} else
				this.treeModel = new ItemTreeModel(this.rootItem);
		}
		// childItem.addChangeListener(this.treeModel.getItemListener());
		this.treeModel.addItem(parentItem == null ? this.rootItem : parentItem, childItem);
	}

	public void addSelectionListener(final SelectionListener selectionListener) {
		final SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length + 1];
		System.arraycopy(this.selectionListeners, 0, selectionListeners1, 1, this.selectionListeners.length);
		selectionListeners1[0] = selectionListener;
		this.selectionListeners = selectionListeners1;
	}

	public void deleteItem(final Item item) {
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
	public void expandAll(final boolean expand) {
		// Traverse tree from root
		if (this.rootItem != null) {
			final TreePath path = new TreePath(this.rootItem);
			this.expandAll(path, null, expand);
			this.expandAll(path, null, expand);
		}
	}

	public void expandAll(final Item item) {
		// Traverse tree from root
		if (this.rootItem != null) {
			final TreePath path = new TreePath(this.rootItem);
			this.expandAll(path, item, true);
			this.expandAll(path, item, true);
		}
	}

	public JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new GridBagLayout());

			final JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.getViewport().add(this.getTree());

			final JButton expandButton = new JButton(getStringIcon("v", 0));
			expandButton.setToolTipText("expand all");
			expandButton.setMargin(NULL_INSETS);

			expandButton.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent e) {
					expandAll(true);
				}
			});

			final JButton collapseButton = new JButton(getStringIcon("v", 180));
			collapseButton.setToolTipText("collapse all");
			collapseButton.setMargin(NULL_INSETS);

			collapseButton.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent e) {
					expandAll(false);
				}
			});

			final JToggleButton toggleButton = new JToggleButton("sort", this.treeModel.isAllwaysSort());

			toggleButton.addActionListener(new ActionListener() {

				public void actionPerformed(final ActionEvent e) {
					final JToggleButton button = (JToggleButton) e.getSource();
					LogicalTreeUI.this.treeModel.setAllwaysSort(button.isSelected());
				}
			});

			toggleButton.setToolTipText("always sort");
			toggleButton.setMargin(NULL_INSETS);

			final Box box = new Box(BoxLayout.X_AXIS);
			// box.add(Box.createGlue());
			box.add(toggleButton);
			box.add(Box.createGlue());
			box.add(expandButton);
			box.add(collapseButton);

			final GridBagConstraints gbc = new GridBagConstraints();

			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;

			this.panel.add(box, gbc);
			gbc.weighty = 1.0;
			gbc.gridheight = GridBagConstraints.RELATIVE;
			this.panel.add(scrollPane, gbc);
			collapseButton.doClick();
		}

		return this.panel;
	}

	public ItemTreeModel getTreeModel() {
		return this.treeModel;
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

				public void valueChanged(final TreeSelectionEvent e) {
					final JTree jTree = (JTree) e.getSource();
					final TreePath[] selectionPaths = jTree.getSelectionPaths();
					if (selectionPaths == null) {
						return;
					}
					final Collection<Item> items = new ArrayList<Item>(selectionPaths.length);
					for (int j = 0; j < selectionPaths.length; j++) {
						final Item itemNode = (Item) selectionPaths[j].getLastPathComponent();
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

			this.tree.addTreeWillExpandListener(new TreeWillExpandListener() {

				public void treeWillCollapse(final TreeExpansionEvent event) {
					// TODO Auto-generated method stub
				}

				public void treeWillExpand(final TreeExpansionEvent event) {
					final TreePath path = event.getPath();
					final Object lastPathComponent = path.getLastPathComponent();
					if (lastPathComponent instanceof Populatable) {
						final Populatable populatableItem = (Populatable) lastPathComponent;
						if (!populatableItem.isPopulated()) {
							final Cursor previousCursor = LogicalTreeUI.this.tree.getCursor();  
							LogicalTreeUI.this.tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							populatableItem.populate();
							LogicalTreeUI.this.tree.setCursor(previousCursor);
						}
					}
				}
			});

			this.tree.setCellRenderer(new ItemTreeCellRenderer());
			this.tree.setCellEditor(new ItemTreeCellEditor(this.tree));
			this.tree.setTransferHandler(new ItemTransferHandler());
		}
		return this.tree;
	}

	public void removeSelectionListener(final SelectionListener selectionListener) {
		int index = -1;
		for (int i = 0; i < this.selectionListeners.length; i++) {
			if (this.selectionListeners[i].equals(selectionListener)) {
				index = i;
				break;
			}
		}
		if (index >= -1) {
			final SelectionListener[] selectionListeners1 = new SelectionListener[this.selectionListeners.length - 1];
			System.arraycopy(this.selectionListeners, 0, selectionListeners1, 0, index);
			System.arraycopy(this.selectionListeners, index + 1, selectionListeners1, index, selectionListeners1.length - index);
			this.selectionListeners = selectionListeners1;
		}
	}

	public void selectedItems(final Collection<Item> items) {
		final TreePath[] treePaths = new TreePath[items.size()];
		this.tree.clearSelection();
		int i = 0;
		for (final Iterator<Item> it = items.iterator(); it.hasNext(); i++) {
			final Item item = it.next();
			final List<Item> treePath = this.getTreePath(null, item);
			final Object[] objects = new Object[treePath.size()];
			int j = objects.length;
			for (final Item item2 : treePath) {
				objects[--j] = getItemNode(null, item2);
			}
			treePaths[i] = new TreePath(objects);
		}
		this.tree.setSelectionPaths(treePaths);
	}

	public void setRootItem(final Item rootItem) {
		this.rootItem = rootItem;
		if (this.treeModel == null) {
			this.treeModel = new ItemTreeModel(this.rootItem);
		}
		final List<Item> children = this.rootItem.getChildren();
		if (children != null) {
			for (final Item item : children) {
				this.addItem(item);
			}
		}
	}

	private Item addObject(final Item parent, final Item child) {
		return addObject(parent, child, false);
	}

	private Item addObject(Item parent1, final Item child, boolean shouldBeVisible) {
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

	private void expandAll(final TreePath parent, final Item item, final boolean expand) {
		// Traverse children
		final Item node = (Item) parent.getLastPathComponent();
		if (item != null && node.equals(item)) {
			return;
		}
		if (node instanceof Populatable) {
			final Populatable populatableNode = ((Populatable) (node));
			if (!populatableNode.isPopulated()) {
				populatableNode.populate();
			}
		}
		final List<Item> children = node.getChildren();
		for (final Item n : children) {
			final TreePath path = parent.pathByAddingChild(n);
			this.expandAll(path, item, expand);
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			this.tree.expandPath(parent);
		} else {
			this.tree.collapsePath(parent);
		}
	}

	private List<Item> getTreePath(List<Item> objects, final Item item) {
		if (objects == null) {
			objects = new LinkedList<Item>();
		}
		// System.out.println("getTreePath | item " + (item == null ? "'null'" :
		// item.getName()));
		objects.add(item);
		final Item parent = item.getParent();
		if (parent != null) {
			this.getTreePath(objects, parent);
		}
		return objects;
	}

	Item getItemNode(Item parent, final Item item) {

		Item node = null;
		if (parent == null) {
			parent = this.rootItem;
		}
		if (parent.equals(item)) {
			return parent;
		}
		final List<Item> children = parent.getChildren();
		if (children != null) {
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
		}

		return node;
	}

}
