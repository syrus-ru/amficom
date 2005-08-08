/*-
 * $Id: ItemTreeIconLabelCellRenderer.java,v 1.4 2005/08/08 11:37:22 arseniy Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:37:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module filter
 */
public class ItemTreeIconLabelCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3618699716540380468L;

	public Component getTreeCellRendererComponent(	JTree tree1,
													Object value,
													boolean selected1,
													boolean expanded,
													boolean leaf,
													int row,
													boolean hasFocus1) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree1, value, selected1, expanded, leaf, row,
			hasFocus1);
		if (value instanceof Item) {
			Item item = (Item) value;
			label.setText(item.getName());
			if (item instanceof IconPopulatableItem) {
				Icon icon = ((IconPopulatableItem) item).getIcon();
				if (icon != null)
					label.setIcon(icon);
			}
		}
		return label;
	}

}
