/*
 * $Id: SOCheckboxRenderer.java,v 1.2 2005/03/21 09:49:19 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/21 09:49:19 $
 * @module generalclient_v1
 */

public class SOCheckboxRenderer extends JCheckBox implements TreeCellRenderer {
	private static final long serialVersionUID = 3258125877622683441L;
	/**
	 * @todo fill following fields from UIDefaults 
	 */
	public static int preferredHeight = 18;
	public static Color selectedBackground = Color.BLUE;
	public static Color selectedForeground = Color.WHITE;
	
	private boolean selected = false; 
	private static SOCheckboxRenderer instance;
	
	private SOCheckboxRenderer() {
		// empty
	}
	
	public static SOCheckboxRenderer getInstance() {
		if (instance == null) {
			instance = new SOCheckboxRenderer();
			instance.setOpaque(false);
			instance.setFocusable(false);
		}
		return instance;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		this.selected = selected;
		SONode node = (SONode)value;
		setText(node.getName());
		/**
		 * setIcon(node.getIcon());
		 * - ignored here as it replaces default checkbox icon
		 */
		
		if (!selected) {
			setForeground(node.getColor());
			setBackground(tree.getBackground());
		}
		else {
			setForeground(selectedForeground);
			setBackground(selectedBackground);
		}
		if (node instanceof SOCheckableNode) {
			setSelected(((SOCheckableNode)node).isChecked());
		}
		return this;
	}
	
	public Dimension getPreferredSize() {
		Dimension ps = super.getPreferredSize();
		return new Dimension(ps.width, SOCheckboxRenderer.preferredHeight);
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

