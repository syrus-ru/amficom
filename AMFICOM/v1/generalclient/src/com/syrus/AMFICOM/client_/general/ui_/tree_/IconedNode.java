/*
 * $Id: IconedNode.java,v 1.1 2005/03/28 11:40:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.ChildrenFactory;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/28 11:40:28 $
 * @module generalclient_v1
 */

public class IconedNode extends PopulatableNode implements Renderable {
	private static TreeCellRenderer renderer;
	private Icon icon;
	
	private class TextRenderer extends JLabel implements TreeCellRenderer {
		private static final long serialVersionUID = 1L;
		/**
		 * @todo fill following fields from UIDefaults 
		 */
		private Color selectedBackground = Color.BLUE;
		private Color selectedForeground = Color.WHITE;
		private boolean selected = false; 
		
		private TextRenderer() {
			// empty
		}
		
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			this.selected = selected;
			IconedNode node = (IconedNode)value;
			setText(node.getName());
			setIcon(node.getIcon());

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
	
	public IconedNode(ChildrenFactory factory, Object object) {
		this(factory, object, object instanceof Namable ? ((Namable)object).getName() : object.toString());
	}
	
	public IconedNode(ChildrenFactory factory, Object object, String name, boolean allowsChildren) {
		this(factory, object, name, null, allowsChildren);
	}
	
	public IconedNode(ChildrenFactory factory, Object object, String name) {
		this(factory, object, name, null, true);
	}
	
	public IconedNode(ChildrenFactory factory, Object object, String name, Icon icon) {
		this(factory, object, name, icon, true);
	}
	
	public IconedNode(ChildrenFactory factory, Object object, String name, Icon icon, boolean allowsChildren) {
		super(factory, object, name, allowsChildren);
		this.icon = icon;
	}
	
	public Icon getIcon() {
		return icon;
	}

	public TreeCellRenderer getRenderer() {
		if (renderer == null)
			renderer = new TextRenderer(); 
		return renderer;
	}
}
