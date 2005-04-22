/**
 * $Id: MapViewTreeCellRenderer.java,v 1.1 2005/04/22 11:36:52 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client_.general.ui_.tree_.IconedNode;
import com.syrus.AMFICOM.logic.Item;

class MapViewTreeCellRenderer extends JLabel implements TreeCellRenderer {
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	private boolean selected = false; 

	MapViewTreeModel model;
	
	public MapViewTreeCellRenderer(MapViewTreeModel model) {
		this.model = model;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		Item node = (Item )value; 
//		String name = node.getName();
		String name = this.model.getNodeName(node);
		if(name == null || name.length() == 0)
			name = LangModelMap.getString("noname");
		setText(name);
		if(node instanceof IconedNode) {
			setIcon(((IconedNode )node).getIcon());
		}

		if (!selected) {
			setForeground(tree.getForeground());
		}
		else {
			setForeground(selectedForeground);
		}
		return this;
	}
	
	protected void paintComponent (Graphics g) {
		if (this.selected) {
			int x = 0;
			Icon icon = getIcon();
			if (icon != null) {
				x += icon.getIconWidth() + getIconTextGap(); 
			}
			g.setColor(selectedBackground);
			g.fillRect(x - 1, 0, getWidth() - x + 2, getHeight());
		}
		super.paintComponent(g);
	}
}