/*
 * $Id: IconedTreeUI.java,v 1.8 2005/09/20 12:39:36 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.Populatable;

/**
 * @author $Author: bob $
 * @version $Revision: 1.8 $, $Date: 2005/09/20 12:39:36 $
 * @module commonclient
 */

public class IconedTreeUI {
	protected LogicalTreeUI treeUI;
	JScrollPane scrollPane;
	private JToolBar toolBar;
	protected boolean linkObjects = false;
	
	public IconedTreeUI(final Item rootItem) {
		this.treeUI = new LogicalTreeUI(rootItem);
		this.treeUI.setRenderer(IconedNode.class, IconedRenderer.getInstance());
		this.treeUI.setRenderer(PopulatableIconedNode.class, IconedRenderer.getInstance());
		this.treeUI.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);		
		
		if (rootItem instanceof Populatable) {
			((Populatable)rootItem).populate();
		}
		this.treeUI.getTree().expandPath(new TreePath(rootItem));
	}
	
	public LogicalTreeUI getTreeUI() {
		return this.treeUI;
	}

	public JTree getTree() {
		return this.treeUI.getTree();
	}
	
	public JComponent getPanel() {
		if (this.scrollPane == null) {
			this.scrollPane = new JScrollPane(getTree());
			this.scrollPane.setAutoscrolls(true);
			this.scrollPane.setWheelScrollingEnabled(true);
		}
		return this.scrollPane;
	}
	
	public JToolBar getToolBar() {
		if (this.toolBar == null) {
			this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
			final JButton refreshButton = new JButton();
			refreshButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_REFRESH));
			refreshButton.setToolTipText(LangModelGeneral.getString("Refresh"));
			refreshButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					final JTree tree = IconedTreeUI.this.treeUI.getTree();
					final TreePath selectedPath = 
						tree.getSelectionModel().getSelectionPath();
					final Item itemToRefresh = selectedPath != null 
							? (Item)selectedPath.getLastPathComponent()
							: (Item)IconedTreeUI.this.treeUI.getTreeModel().getRoot();
					updateRecursively(itemToRefresh);
				}
			});
			this.toolBar.add(refreshButton);
			
			final JToggleButton	syncButton = new JToggleButton();
			syncButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_SYNCHRONIZE));
			syncButton.setToolTipText(LangModelGeneral.getString("Button.Synchronize"));
			syncButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			syncButton.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					IconedTreeUI.this.linkObjects = ((JToggleButton)e.getSource()).isSelected();
				}
			});
			this.toolBar.add(syncButton);
		}
		return this.toolBar;
	}
	
	public Item findNode(Item item, Object object, boolean usePopulate) {
		if (item.getObject().equals(object)) {
			return item;
		}
		if (usePopulate && item instanceof Populatable) {
			((Populatable)item).populate();
		}
		
		for (final Item child : item.getChildren()) {
			if (child.getObject().equals(object)) {
				return child;
			}
		}
		for (final Item child : item.getChildren()) {
			final Item found = findNode(child, object, usePopulate);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
	
	public Collection findNodes(final Item item, 
	                            final Collection objects, 
	                            final boolean usePopulate) {
		final Collection<Item> items = new LinkedList<Item>();
		this.fillFoundNodes(item, objects, items, usePopulate);
		return items;
	}
	
	private void fillFoundNodes(final Item item, 
	                            final Collection objects, 
	                            final Collection<Item> items, 
	                            final boolean usePopulate) {
		if(objects.contains(item.getObject())) {
			items.add(item);
		}
		if (usePopulate && item instanceof Populatable) {
			((Populatable)item).populate();
		}
		for(final Item childNode : item.getChildren()) {
			this.fillFoundNodes(childNode, objects, items, usePopulate);
		}
	}
	
	public void updateRecursively(Item item) {
		if (item instanceof Populatable) {
			Populatable populatable = (Populatable)item;
			if (populatable.isPopulated()) {
				populatable.repopulate();
			}
			for(final Item item2 : item.getChildren()) {
				this.updateRecursively(item2);
			}
		}
	}
	
	public boolean isLinkObjects() {
		return this.linkObjects;
	}
	
}
