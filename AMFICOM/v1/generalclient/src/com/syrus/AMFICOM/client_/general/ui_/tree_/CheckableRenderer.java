/*-
 * $Id: CheckableRenderer.java,v 1.1 2005/03/30 13:27:20 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/30 13:27:20 $
 * @module generalclient_v1
 */

public class CheckableRenderer extends JCheckBox implements TreeCellRenderer {
	private static final long serialVersionUID = 3258125877622683441L;
	/**
	 * @todo fill following fields from UIDefaults 
	 */
	public static int preferredHeight = 18;
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	
	private boolean selected = false; 
	private static CheckableRenderer instance;
	
	private CheckableRenderer() {
		// empty
	}
	
	public static CheckableRenderer getInstance() {
		if (instance == null) {
			instance = new CheckableRenderer();
			instance.setOpaque(false);
			instance.setFocusable(false);
		}
		return instance;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		Item node = (Item)value;
		setText(node.getName());
		
		if (!selected) {
			setForeground(tree.getForeground());
			setBackground(tree.getBackground());
		}
		else {
			setForeground(selectedForeground);
			setBackground(selectedBackground);
		}
		if (node instanceof CheckableNode) {
			setSelected(((CheckableNode)node).isChecked());
		}
		return this;
	}
	
	public Dimension getPreferredSize() {
		Dimension ps = super.getPreferredSize();
		return new Dimension(ps.width, preferredHeight);
	}

	protected void paintComponent(Graphics g) {
		if (selected) {
			int x = 0;
			Icon icon = ((BasicRadioButtonUI)instance.getUI()).getDefaultIcon();
			if (icon != null) {
				x += icon.getIconWidth() + getIconTextGap(); 
			}
			g.setColor(selectedBackground);
			Insets i = getInsets();
			g.fillRect(i.left + x, 0 , getWidth() - i.right - i.left - x, getHeight());
		}
		super.paintComponent(g);
	}
}
