package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

public class ObjectResourceTreeRenderer extends DefaultTreeCellRenderer
		implements TreeCellRenderer
{
	public ObjectResourceTreeRenderer()
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
		try
		{
			ObjectResourceTreeNode ortn = (ObjectResourceTreeNode )value;
			Component renderer;

			renderer = ortn.getComponent();
			if (renderer != null)
			{
				Vector vec = new Vector();
				Component[] comps = tree.getComponents();
				for(int i = 0; i < comps.length; i++)
					vec.add(comps[i]);
				if(!vec.contains(renderer))
					tree.add(renderer);
			}
			else
			{
				this.setText(ortn.getName());
				this.setIcon(null);
				renderer = this;
			}
			if (!selected)
			{
				renderer.setForeground(tree.getForeground());
	//			renderer.setBackground(tree.getBackground());
				//this.setText("<html><P bgcolor=\"white\">" + ortn.getName() +"</P>");
			}
			else
			{
				renderer.setForeground(new Color(0, 0, 255));
	//			renderer.setBackground(new Color(0, 0, 255));
				//this.setText("<html><P bgcolor=\"black\">" + ortn.getName() +"</P>");
			}
	//		Container cont = renderer.getParent();
			return renderer;
		}
		catch(Exception ex)
		{
			return super.getTreeCellRendererComponent(
					tree,
					value,
					selected,
					expanded,
					leaf,
					row,
					hasFocus);
		}
		
	}

}