package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.EventSource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JTree;

public class AlarmSourceTree extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModel.getString("labelRoot"), "root");

	public AlarmSourceTree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		Hashtable ht = Pool.getHash(EventSource.typ);
		if(ht != null)
			for(Enumeration en = ht.elements(); en.hasMoreElements();)
			{
				EventSource src = (EventSource )en.nextElement();
				FilterTreeNode srcnode = new FilterTreeNode(src.getName(), src.getId());
				root.add(srcnode);
			}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}