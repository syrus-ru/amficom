package com.syrus.AMFICOM.Client.General.Filter;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class FilterTreeNode extends DefaultMutableTreeNode
{
	public int state = 0;
	public String name;
	public String id;

	public FilterTreeNode(String name, String id)
	{
		super(name);
		this.name = name;
		this.id = id;
	}

	public String getName()
	{
			return name;
	}

	public String getId()
	{
			return id;
	}

	public void setChildrenState(int st)
	{
		for(Enumeration enum = this.children(); enum.hasMoreElements();)
		{
			FilterTreeNode down_mte = (FilterTreeNode)enum.nextElement();
			if (st == 0)
			{
				down_mte.state = 2;
			}
			else if (st == 1 || st == 2)
			{
				down_mte.state = 0;
			}
			down_mte.setChildrenState(st);
		}
	}

	public int[] CheckChildrenState(FilterTreeNode mte, int[] count)
	{
		for(Enumeration enum = this.children(); enum.hasMoreElements();)
		{
			FilterTreeNode down_mte = (FilterTreeNode)enum.nextElement();
			if (down_mte.state == 0)
			{
				count[0]++;
			}
			else if (down_mte.state == 2)
			{
				count[1]++;
			}
			count[2]++;
			int[] count_down = {0,0,0};
			count_down = down_mte.CheckChildrenState(down_mte, count_down);
			count[0] += count_down[0];
			count[1] += count_down[1];
			count[2] += count_down[2];
		}
		return count;
	}

	public void MyGetParent(FilterTreeNode mte)
	{
		FilterTreeNode up_mte = (FilterTreeNode)mte.getParent();
		int[] count = {0,0,0};
		count = up_mte.CheckChildrenState(up_mte,count);
		if (count[0] == count[2])
		{
			up_mte.state = 0;
		}
		else if (count[1] == count[2])
		{
			up_mte.state = 2;
		}
		else
		{
			up_mte.state = 1;
		}
		if (!up_mte.equals(up_mte.getRoot()))
		{
			up_mte.MyGetParent(up_mte);
		}
	}
}
