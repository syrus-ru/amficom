package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;

import javax.swing.tree.DefaultTreeModel;
import com.syrus.AMFICOM.Client.General.Filter.FilterTreeNode;
import com.syrus.AMFICOM.filter.*;

public class TreeModelClone extends DefaultTreeModel
{
	public TreeModelClone (FilterTreeNode tn)
	{
		super(tn);
	}

	public TreeModelClone myclone ()
	{
		FilterTreeNode new_root = new FilterTreeNode(LangModel.getString("labelRoot"), "root");
		new_root.state = ((FilterTreeNode)root).state;
		for(Enumeration en = root.children(); en.hasMoreElements();)
		{
			FilterTreeNode kisnode_old = (FilterTreeNode)en.nextElement();
			FilterTreeNode kisnode_new = new FilterTreeNode(kisnode_old.getName(),kisnode_old.id);
			kisnode_new.state = kisnode_old.state;
			new_root.add(kisnode_new);
			for(Enumeration enu = kisnode_old.children(); enu.hasMoreElements();)
			{
				FilterTreeNode portnode_old = (FilterTreeNode)enu.nextElement();
				FilterTreeNode portnode_new = new FilterTreeNode(portnode_old.getName(),portnode_old.id);
				portnode_new.state = portnode_old.state;
				kisnode_new.add(portnode_new);
				for(Enumeration e = portnode_old.children(); e.hasMoreElements();)
				{
					FilterTreeNode vol_tt_old = (FilterTreeNode)e.nextElement();
					FilterTreeNode vol_tt_new = new FilterTreeNode(vol_tt_old.getName(),vol_tt_old.id);
					vol_tt_new.state = vol_tt_old.state;
					portnode_new.add(vol_tt_new);
					if ( vol_tt_new.getName().equals(LangModel.getString("ORMones")) )
					{
						for(Enumeration enum = vol_tt_old.children(); enum.hasMoreElements();)
						{
							FilterTreeNode pathnode_old = (FilterTreeNode)enum.nextElement();
							FilterTreeNode pathnode_new = new FilterTreeNode(pathnode_old.getName(),pathnode_old.id);
							pathnode_new.state = pathnode_old.state;
							vol_tt_new.add(pathnode_new);
						}
					}
					if ( vol_tt_new.getName().equals(LangModel.getString("ORTestTypes")) )
					{
						for(Enumeration enum = vol_tt_old.children(); enum.hasMoreElements();)
						{
							FilterTreeNode ttnode_old = (FilterTreeNode)enum.nextElement();
							FilterTreeNode ttnode_new = new FilterTreeNode(ttnode_old.getName(),ttnode_old.id);
							ttnode_new.state = ttnode_old.state;
							vol_tt_new.add(ttnode_new);
						}
					}
				}
			}

		}
		TreeModelClone mtm = new TreeModelClone(new_root);
		return mtm;
	}

	public Hashtable getHash ()
	{
		Hashtable result = new Hashtable();

		FilterTreeNode node = (FilterTreeNode )this.getRoot();

		getTreeHash(node, result);

		return result;
	}

	private void getTreeHash(FilterTreeNode node, Hashtable volume)
	{
		FilterTreeNode parent = (FilterTreeNode )node.getParent();

		ArrayList childList = new ArrayList();
		for(Enumeration enum1 = node.children(); enum1.hasMoreElements();)
			childList.add(((FilterTreeNode )enum1.nextElement()).id);

		FilterTreeNodeHolder trans = new FilterTreeNodeHolder(
				node.id,
				node.state,
				node.name,
//				"root",
				(parent == null) ? "" : parent.id,
				(String[] )childList.toArray(new String[0]));

		volume.put(node.id, trans);

		for(Enumeration enum = node.children(); enum.hasMoreElements();)
		{
			FilterTreeNode curElem = (FilterTreeNode )enum.nextElement();

			getTreeHash(curElem, volume);
/*
			ArrayList childList = new ArrayList();
			for(Enumeration enum1 = curElem.children(); enum1.hasMoreElements();)
				childList.add(((FilterTreeNode )enum1.nextElement()).id);

			FilterTreeNodeHolder trans = new FilterTreeNodeHolder(
					curElem.id,
					curElem.state,
					curElem.name,
					node.id,
//					(parent == null) ? "root" : parent.id,
					(String[] )childList.toArray());

			volume.put(curElem.id, trans);
*/
		}
	}
}
