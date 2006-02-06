/*-
 * $Id: CheckableRenderer.java,v 1.5 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.5 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
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

	public Component getTreeCellRendererComponent(final JTree tree,
			final Object value,
			final boolean selected1,
			final boolean expanded,
			final boolean leaf,
			final int row,
			final boolean hasFocus) {
		this.selected = selected1;
		final Item node = (Item) value;
		super.setText(node.getName());

		if (!selected1) {
			super.setForeground(tree.getForeground());
			super.setBackground(tree.getBackground());
		} else {
			super.setForeground(selectedForeground);
			super.setBackground(selectedBackground);
		}
		if (node instanceof CheckableNode) {
			super.setSelected(((CheckableNode) node).isChecked());
		}
		return this;
	}

	@Override
	public Dimension getPreferredSize() {
		final Dimension ps = super.getPreferredSize();
		return new Dimension(ps.width, preferredHeight);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (this.selected) {
			int x = 0;
			final Icon icon = ((BasicRadioButtonUI) instance.getUI()).getDefaultIcon();
			if (icon != null) {
				x += icon.getIconWidth() + getIconTextGap();
			}
			g.setColor(selectedBackground);
			final Insets i = getInsets();
			g.fillRect(i.left + x, 0, getWidth() - i.right - i.left - x, getHeight());
		}
		super.paintComponent(g);
	}
}
