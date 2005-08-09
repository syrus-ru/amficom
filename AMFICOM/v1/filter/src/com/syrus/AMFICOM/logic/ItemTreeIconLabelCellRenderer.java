/*-
 * $Id: ItemTreeIconLabelCellRenderer.java,v 1.5 2005/08/09 21:10:10 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.logic;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * @version $Revision: 1.5 $, $Date: 2005/08/09 21:10:10 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public class ItemTreeIconLabelCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3618699716540380468L;

	@Override
	public Component getTreeCellRendererComponent(final JTree tree1,
			final Object value,
			final boolean selected1,
			final boolean expanded,
			final boolean leaf,
			final int row,
			final boolean hasFocus1) {
		final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree1, value, selected1, expanded, leaf, row, hasFocus1);
		if (value instanceof Item) {
			final Item item = (Item) value;
			label.setText(item.getName());
			if (item instanceof IconPopulatableItem) {
				final Icon icon = ((IconPopulatableItem) item).getIcon();
				if (icon != null) {
					label.setIcon(icon);
				}
			}
		}
		return label;
	}

}
