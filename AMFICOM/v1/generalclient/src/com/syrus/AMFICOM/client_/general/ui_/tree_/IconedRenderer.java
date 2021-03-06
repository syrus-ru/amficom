/*-
 * $Id: IconedRenderer.java,v 1.2 2005/05/18 14:01:21 bass Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/05/18 14:01:21 $
 * @module generalclient_v1
 */

public class IconedRenderer extends JLabel implements TreeCellRenderer {
	private static IconedRenderer instance;
	/**
	 * @todo fill following fields from UIDefaults
	 */
	private Color selectedBackground = Color.BLUE;
	private Color selectedForeground = Color.WHITE;
	private boolean selected = false;
	
	private IconedRenderer() {
		// empty
	}
	
	public static IconedRenderer getInstance() {
		if (instance == null) {
			instance = new IconedRenderer();
		}
		return instance;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		Item node = (Item)value;
		setText(node.getName());
		if (node instanceof IconedNode)
			setIcon(((IconedNode)node).getIcon());

		if (!selected) {
			setForeground(tree.getForeground());
			setBackground(tree.getBackground());
		}
		else {
			setForeground(selectedForeground);
			setBackground(selectedBackground);
		}
		return this;
	}
	
	protected void paintComponent (Graphics g) {
		if (this.selected) {
			int x = 0;
			Icon icon1 = getIcon();
			if (icon1 != null) {
				x += icon1.getIconWidth() + getIconTextGap();
			}
			g.setColor(selectedBackground);
			Insets i = getInsets();
			g.fillRect(i.left + x, 0, getWidth() - i.right - i.left - x, getHeight());
		}
		super.paintComponent(g);
	}
}
