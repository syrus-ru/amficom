package com.syrus.AMFICOM.Client.Configure.Map;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

public class MyMapSchemeRenderer extends DefaultTreeCellRenderer 
		implements TreeCellRenderer
{
	public MyMapSchemeRenderer()
	{
	}

	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value, 
			boolean selected,
			boolean expanded, 
			boolean leaf, 
			int row, 
			boolean hasFocus)
	{
		MyMapSchemeTreeNode mte = (MyMapSchemeTreeNode )value;
		Component renderer;

		if (mte.elementLabel != null)
		{
//			mte.elementLabel.setText(mte.getName());
			renderer = mte.elementLabel;
			tree.add(renderer);
		}
		else
		{
			this.setText(mte.getName());
			renderer = this;
		}
		if (!selected)
		{
			renderer.setForeground(tree.getForeground());
			renderer.setBackground(new Color(0, 0, 100));//tree.getBackground());
		}
		else //		if (isSelected) 
		{
			renderer.setForeground(new Color(0, 0, 255));// Color.white );UIManager.getColor("Tree.selectionForeground")
			renderer.setBackground( new Color(0, 0, 100) );
		}
		Container cont = renderer.getParent();
		return renderer;
	}
	
}