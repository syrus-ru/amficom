package com.syrus.AMFICOM.Client.General.Filter;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public abstract class FilterTree
{
	public JTree tree = new JTree();

	public FilterTree() {}

	public JTree getTree(ApplicationContext aC)
	{
		setTree(aC);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		return tree;
	}

	public abstract void setTree(ApplicationContext aC);
}
