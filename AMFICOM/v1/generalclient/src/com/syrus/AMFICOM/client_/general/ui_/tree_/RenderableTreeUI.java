/*
 * $Id: RenderableTreeUI.java,v 1.1 2005/03/28 11:40:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.awt.Component;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.syrus.AMFICOM.logic.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/28 11:40:28 $
 * @module generalclient_v1
 */

public class RenderableTreeUI extends LogicalTreeUI { 
	
	private class TreeCellRendererRedirector extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(JTree tree1, Object value,
				boolean selected1, boolean expanded, boolean leaf, int row,
				boolean hasFocus1) {
			if (value instanceof Renderable) {
				return ((Renderable)value).getRenderer().getTreeCellRendererComponent(tree1, value, selected1, expanded, leaf, row, hasFocus1);
			}
			
			Component treeCellRendererComponent = super.getTreeCellRendererComponent(tree1, value, selected1, expanded,
				leaf, row, hasFocus1);

			if (value instanceof Item) {
				Item item = (Item) value;
				((JLabel) treeCellRendererComponent).setText(item.getName());
			}
			return treeCellRendererComponent;
		}
	}

//	public class SOTreeEditor extends DefaultTreeCellEditor
	
	public RenderableTreeUI(Item rootItem) {
		super(rootItem);
	}

	public JTree getTree() {
		JTree tree = super.getTree();
		tree.setCellRenderer(new TreeCellRendererRedirector());
		return tree;
	}
	
//	public void setRootItem(Item rootItem) {
//		if (rootItem instanceof Populatable)
//			((Populatable)rootItem).populate();
//		super.setRootItem(rootItem);
//	}
}