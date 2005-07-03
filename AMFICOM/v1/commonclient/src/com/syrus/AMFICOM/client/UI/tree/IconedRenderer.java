/*-
 * $Id: IconedRenderer.java,v 1.3 2005/06/17 13:54:16 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/06/17 13:54:16 $
 * @module generalclient_v1
 */

public class IconedRenderer extends JLabel implements TreeCellRenderer {
	private static final long serialVersionUID = 3834031342177432887L;
	
	private static IconedRenderer instance;

	private boolean selected = false; 
	
	private IconedRenderer() {
		// singleton
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
			this.setIcon(((IconedNode)node).getIcon());

		if (!selected) {
			this.setForeground(tree.getForeground());
			this.setBackground(tree.getBackground());
		} else {
			this.setForeground(UIManager.getColor("IconedRenderer.selectedForeground"));
			this.setBackground(UIManager.getColor("IconedRenderer.selectedBackground"));
		}
		return this;
	}
	
	protected void paintComponent (Graphics g) {
		if (this.selected) {
			int x = 0;
			Icon icon1 = getIcon();
			if (icon1 != null) {
				x += icon1.getIconWidth() + this.getIconTextGap(); 
			}
			g.setColor(this.getBackground());
			Insets insets = this.getInsets();
			g.fillRect(insets.left + x, 0, this.getWidth() - insets.right - insets.left - x, this.getHeight());
		}
		super.paintComponent(g);
	}
}
