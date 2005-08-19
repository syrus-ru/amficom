/*
 * $Id: IconedTreeUI.java,v 1.6 2005/08/19 14:06:09 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
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
 * @version $Revision: 1.6 $, $Date: 2005/08/19 14:06:09 $
 * @module commonclient
 */

public class IconedTreeUI {
	protected LogicalTreeUI treeUI;
	JScrollPane scrollPane;
	IconedTreeToolBar toolBar;
	protected boolean linkObjects = false;
	
	public IconedTreeUI(Item rootItem) {
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
		if (this.toolBar == null)
			this.toolBar = new IconedTreeToolBar();
		return this.toolBar;
	}
	
	public Item findNode(Item item, Object object, boolean usePopulate) {
		if (item.getObject().equals(object))
			return item;
		if (usePopulate && item instanceof Populatable)
			((Populatable)item).populate();
		
		for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
			Item child = (Item)it.next();
			if (child.getObject().equals(object))
				return child;
		}
		for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
			Item child = (Item)it.next();
			Item found = findNode(child, object, usePopulate);
			if (found != null)
				return found;
		}
		return null;
	}
	
	public Collection findNodes(Item item, Collection objects, boolean usePopulate) {
		Collection<Item> items = new LinkedList<Item>();
		fillFoundNodes(item, objects, items, usePopulate);
		return items;
	}
	
	private void fillFoundNodes(Item item, Collection objects, Collection<Item> items, boolean usePopulate) {
		if(objects.contains(item.getObject()))
			items.add(item);
		if (usePopulate && item instanceof Populatable)
			((Populatable)item).populate();
		for(Iterator iter = item.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			fillFoundNodes(childNode, objects, items, usePopulate);
		}
	}
	
	public void updateRecursively(Item item) {
		if (item instanceof Populatable) {
			Populatable populatable = (Populatable)item;
			if (populatable.isPopulated())
				populatable.populate();
			for (Iterator it = item.getChildren().iterator(); it.hasNext();)
				updateRecursively((Item)it.next());
		}
	}
	
	public boolean isLinkObjects() {
		return this.linkObjects;
	}
	
	public class IconedTreeToolBar extends JToolBar {
		
		public IconedTreeToolBar() {
			super(SwingConstants.HORIZONTAL);
			final JButton refreshButton = new JButton();
			refreshButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_REFRESH));
			refreshButton.setToolTipText(LangModelGeneral.getString("Refresh"));
			refreshButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TreePath selectedPath = IconedTreeUI.this.treeUI.getTree().getSelectionModel().getSelectionPath();
					Item itemToRefresh = selectedPath != null 
							? (Item)selectedPath.getLastPathComponent()
							: (Item)IconedTreeUI.this.treeUI.getTreeModel().getRoot();
					updateRecursively(itemToRefresh);
				}
			});
			this.add(refreshButton);
			
			final JToggleButton	syncButton = new JToggleButton();
			syncButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_SYNCHRONIZE));
			syncButton.setToolTipText(LangModelGeneral.getString("Button.Synchronize"));
			syncButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			syncButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IconedTreeUI.this.linkObjects = ((JToggleButton)e.getSource()).isSelected();
				}
			});
			this.add(syncButton);
		}
	}
}
