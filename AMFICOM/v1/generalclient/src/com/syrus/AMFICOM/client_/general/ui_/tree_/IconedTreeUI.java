/*
 * $Id: IconedTreeUI.java,v 1.1 2005/03/30 13:27:20 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import javax.swing.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/30 13:27:20 $
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
}
