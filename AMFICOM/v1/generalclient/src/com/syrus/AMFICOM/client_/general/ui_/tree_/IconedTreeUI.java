/*
 * $Id: IconedTreeUI.java,v 1.2 2005/04/18 08:54:35 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.util.Iterator;

import javax.swing.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 08:54:35 $
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
		return treeUI;
	}

	public JTree getTree() {
		return this.treeUI.getTree();
	}
	
	public JComponent getPanel() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getTree());
		}
		return scrollPane;
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
