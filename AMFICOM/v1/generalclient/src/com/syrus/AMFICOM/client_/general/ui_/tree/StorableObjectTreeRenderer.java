/*
 * $Id: StorableObjectTreeRenderer.java,v 1.1 2005/03/05 11:33:03 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/05 11:33:03 $
 * @module generalclient_v1
 */

public class StorableObjectTreeRenderer extends JLabel implements TreeCellRenderer {
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	private boolean selected = false; 
	private static StorableObjectTreeRenderer instance;
	
	public static StorableObjectTreeRenderer getInstance() {
		if (instance == null) {
			instance = new StorableObjectTreeRenderer();
		}
		return instance;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		StorableObjectTreeNode node = (StorableObjectTreeNode)value; 
		setText(node.getName());
		setIcon(node.getIcon());

		if (!selected) {
			setForeground(tree.getForeground());
		}
		else {
			setForeground(selectedForeground);
		}
		return this;
	}
	
	protected void paintComponent (Graphics g) {
		if (selected) {
			int x = 0;
			Icon icon = instance.getIcon();
			if (icon != null) {
				x += icon.getIconWidth() + getIconTextGap(); 
			}
			g.setColor(selectedBackground);
			g.fillRect(x - 1, 0, getWidth() - x + 2, getHeight());
		}
		super.paintComponent(g);
	}
}
