/*
 * $Id: IconedTreeUI.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.util.Iterator;

import javax.swing.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module generalclient_v1
 */

public class IconedTreeUI {
	protected LogicalTreeUI treeUI;
	JScrollPane scrollPane;
	
	public IconedTreeUI(Item rootItem) {
		this.treeUI = new LogicalTreeUI(rootItem);
		this.treeUI.setRenderer(IconedNode.class, IconedRenderer.getInstance());
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
		}
		return this.scrollPane;
	}
	
	public Item findNode(Item item, Object object, boolean usePopulate) {
		if (item.getObject().equals(object))
			return item;
		for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
			Item child = (Item)it.next();
			if (child.getObject().equals(object))
				return child;
		}
		for (Iterator it = item.getChildren().iterator(); it.hasNext();) {
			Item child = (Item)it.next();
			if (usePopulate && child instanceof Populatable)
				((Populatable)child).populate();
			Item found = findNode(child, object, usePopulate);
			if (found != null)
				return found;
		}
		return null;
	}
}
