package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class SiteBindingRenderer extends DefaultTreeCellRenderer 
{
	MapSiteNodeElement site;

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
			super.setText(((SchemeElement )uo).getName());
		}
		else
		if(uo instanceof SchemeCableLink)
		{
			super.setText(((SchemeCableLink )uo).getName());
		}

        return this;
    }
}