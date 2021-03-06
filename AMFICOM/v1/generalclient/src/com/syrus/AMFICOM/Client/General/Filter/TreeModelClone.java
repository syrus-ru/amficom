package com.syrus.AMFICOM.Client.General.Filter;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

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
			FilterTreeNode kisnode_new = new FilterTreeNode(kisnode_old.getName(),kisnode_old.getId());
			kisnode_new.state = kisnode_old.state;
			new_root.add(kisnode_new);
			for(Enumeration enu = kisnode_old.children(); enu.hasMoreElements();)
			{
				FilterTreeNode portnode_old = (FilterTreeNode)enu.nextElement();
				FilterTreeNode portnode_new = new FilterTreeNode(portnode_old.getName(),portnode_old.getId());
				portnode_new.state = portnode_old.state;
				kisnode_new.add(portnode_new);
				for(Enumeration e = portnode_old.children(); e.hasMoreElements();)
				{
					FilterTreeNode vol_tt_old = (FilterTreeNode)e.nextElement();
					FilterTreeNode vol_tt_new = new FilterTreeNode(vol_tt_old.getName(),vol_tt_old.getId());
					vol_tt_new.state = vol_tt_old.state;
					portnode_new.add(vol_tt_new);
					if ( vol_tt_new.getName().equals(LangModel.getString("ORMones")) )
					{
						for(Enumeration enumeration = vol_tt_old.children(); enumeration.hasMoreElements();)
						{
							FilterTreeNode pathnode_old = (FilterTreeNode)enumeration.nextElement();
							FilterTreeNode pathnode_new = new FilterTreeNode(pathnode_old.getName(),pathnode_old.getId());
							pathnode_new.state = pathnode_old.state;
							vol_tt_new.add(pathnode_new);
						}
					}
					if ( vol_tt_new.getName().equals(LangModel.getString("ORTestTypes")) )
					{
						for(Enumeration enumeration = vol_tt_old.children(); enumeration.hasMoreElements();)
						{
							FilterTreeNode ttnode_old = (FilterTreeNode)enumeration.nextElement();
							FilterTreeNode ttnode_new = new FilterTreeNode(ttnode_old.getName(),ttnode_old.getId());
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

	public Map getHash ()
	{
		Map result = new HashMap();

		FilterTreeNode node = (FilterTreeNode )this.getRoot();

		getTreeHash(node, result);

		return result;
	}

	private void getTreeHash(FilterTreeNode node, Map volume)
	{
		FilterTreeNode parent = (FilterTreeNode )node.getParent();

		List childList = new ArrayList();
		for(Enumeration enum1 = node.children(); enum1.hasMoreElements();)
			childList.add(((FilterTreeNode )enum1.nextElement()).getId());

		FilterTreeNodeHolder trans = new FilterTreeNodeHolder(
				node.getId(),
				node.state,
				node.name,
//				"root",
				(parent == null) ? "" : parent.getId(),
				(String[] )childList.toArray(new String[0]));

		volume.put(node.getId(), trans);

		for(Enumeration enumeration = node.children(); enumeration.hasMoreElements();)
		{
			FilterTreeNode curElem = (FilterTreeNode )enumeration.nextElement();

			getTreeHash(curElem, volume);
/*
			ArrayList childList = new ArrayList();
			for(Enumeration enum1 = curElem.children(); enum1.hasMoreElements();)
				childList.add(((FilterTreeNode )enum1.nextElement()).getId());

			FilterTreeNodeHolder trans = new FilterTreeNodeHolder(
					curElem.getId(),
					curElem.state,
					curElem.name,
					node.getId(),
//					(parent == null) ? "root" : parent.getId(),
					(String[] )childList.toArray());

			volume.put(curElem.getId(), trans);
*/
		}
	}
}
