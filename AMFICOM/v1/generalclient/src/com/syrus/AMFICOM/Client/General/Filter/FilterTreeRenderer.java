package com.syrus.AMFICOM.Client.General.Filter;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import com.syrus.AMFICOM.Client.General.Filter.FilterTreeNode;

public class FilterTreeRenderer extends JLabel implements TreeCellRenderer
{
	public FilterTreeRenderer() { }

	public Component getTreeCellRendererComponent(JTree tree,Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		FilterTreeNode mte = (FilterTreeNode)value;
		this.setText(mte.getName());

		if (mte.state == 0)
		{
			this.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/unselect.gif").getScaledInstance(
					10,
					10,
					Image.SCALE_DEFAULT)));
		}
		else if (mte.state == 1)
		{
			this.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/selectnotall.gif").getScaledInstance(
					10,
					10,
					Image.SCALE_DEFAULT)));
		}
		else if (mte.state == 2)
		{
			this.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/selectall.gif").getScaledInstance(
					10,
					10,
					Image.SCALE_DEFAULT)));
		}
		return this;
	}
}