package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class SiteBindingRenderer extends DefaultTreeCellRenderer 
{
	SiteNode site;

    public SiteBindingRenderer()//MapSiteNodeElement site) 
	{
//        site = site;
    }

    public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) 
	{
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
		if(uo instanceof SchemeElement)
		{
			super.setText(((SchemeElement )uo).name());
		}
		else
		if(uo instanceof SchemeCableLink)
		{
			super.setText(((SchemeCableLink )uo).name());
		}

        return this;
    }
}
