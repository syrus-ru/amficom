/*
 * $Id: SOTextRenderer.java,v 1.1 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;
import java.awt.Color;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOTextRenderer extends JLabel implements TreeCellRenderer {
	private static final long serialVersionUID = 3689072858814887473L;
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	private boolean selected = false; 
	private static SOTextRenderer instance;
	
	private SOTextRenderer() {
		// empty
	}
	
	public static SOTextRenderer getInstance() {
		if (instance == null) {
			instance = new SOTextRenderer();
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
			Insets i = getInsets();
			g.fillRect(i.left + x, 0, getWidth() - i.right - i.left - x, getHeight());
		}
		super.paintComponent(g);
	}
}

