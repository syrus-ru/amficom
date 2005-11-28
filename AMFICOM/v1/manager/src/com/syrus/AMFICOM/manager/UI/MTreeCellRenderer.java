/*-
* $Id: MTreeCellRenderer.java,v 1.1 2005/11/28 14:47:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/28 14:47:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class MTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private Font defaultFont;
	private Font actionNodeFont;
	
	@Override
	public final Component getTreeCellRendererComponent(final JTree tree,
			final Object value,
			final boolean sel,
			final boolean expanded,
			final boolean leaf,
			final int row,
			final boolean hasFocus) {
		
		final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, 
			value, 
			sel, 
			expanded, 
			leaf, 
			row,
			hasFocus);
		
		if (this.defaultFont == null) {
			this.defaultFont = label.getFont();
		}
		
		if (value instanceof ActionMutableTreeNode) {
			final ActionMutableTreeNode treeNode = (ActionMutableTreeNode) value;
			final AbstractAction abstractAction = treeNode.getAbstractAction();
			label.setIcon((Icon) abstractAction.getValue(Action.SMALL_ICON));
			if (this.actionNodeFont == null) {
				final int italic = this.defaultFont.isItalic() ? Font.PLAIN : Font.ITALIC;
				final int bold = this.defaultFont.isBold() ? Font.PLAIN : Font.BOLD;
				this.actionNodeFont = this.defaultFont.deriveFont(italic | bold);
			}
			label.setFont(this.actionNodeFont);
		} else {
			label.setFont(this.defaultFont);
		}
		
		return label;
	}
	
}

