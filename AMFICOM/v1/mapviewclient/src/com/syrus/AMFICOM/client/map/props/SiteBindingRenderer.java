package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class SiteBindingRenderer extends DefaultTreeCellRenderer {
	SiteNode site;

	public SiteBindingRenderer() {
		// empty
	}

	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(
				tree,
				value,
				sel,
				expanded,
				leaf,
				row,
				hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode )value;
		Object uo = node.getUserObject();
		if(uo instanceof SchemeElement) {
			super.setText(((SchemeElement )uo).getName());
		}
		else
			if(uo instanceof SchemeCableLink) {
				super.setText(((SchemeCableLink )uo).getName());
			}

		return this;
	}
}
