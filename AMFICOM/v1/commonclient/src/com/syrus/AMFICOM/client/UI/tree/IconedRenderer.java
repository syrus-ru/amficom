/*-
 * $Id: IconedRenderer.java,v 1.6 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.6 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
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

	public Component getTreeCellRendererComponent(final JTree tree,
			final Object value,
			final boolean selected1,
			final boolean expanded,
			final boolean leaf,
			final int row,
			final boolean hasFocus) {
		this.selected = selected1;
		final Item node = (Item) value;
		this.setText(node.getName());
		if (node instanceof IconedNode) {
			this.setIcon(((IconedNode) node).getIcon());
		}

		if (!selected1) {
			this.setForeground(tree.getForeground());
			this.setBackground(tree.getBackground());
		} else {
			this.setForeground(UIManager.getColor("IconedRenderer.selectedForeground"));
			this.setBackground(UIManager.getColor("IconedRenderer.selectedBackground"));
		}
		return this;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (this.selected) {
			int x = 0;
			final Icon icon1 = this.getIcon();
			if (icon1 != null) {
				x += icon1.getIconWidth() + this.getIconTextGap();
			}
			g.setColor(this.getBackground());
			final Insets insets = this.getInsets();
			g.fillRect(insets.left + x, 0, this.getWidth() - insets.right - insets.left - x, this.getHeight());
		}
		super.paintComponent(g);
	}
}
