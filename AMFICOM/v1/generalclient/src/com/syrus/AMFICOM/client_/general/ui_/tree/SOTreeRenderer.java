/*
 * $Id: SOTreeRenderer.java,v 1.1 2005/03/14 13:30:48 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;
import java.awt.Component;

import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/14 13:30:48 $
 * @module generalclient_v1
 */

public class SOTreeRenderer extends JLabel implements TreeCellRenderer {
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	private boolean selected = false; 
	private static SOTreeRenderer instance;
	
	public static SOTreeRenderer getInstance() {
		if (instance == null) {
			instance = new SOTreeRenderer();
		}
		return instance;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		SONode node = (SONode )value;
		setText(node.getName());
		setIcon(node.getIcon());

		if (!selected) {
			setForeground(node.getColor());
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
