package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.*;

public class StatusTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "ROOT");
	FilterTreeNode a1 = new FilterTreeNode(LangModelSchedule.getString("Done"), "COMPLETED");
	FilterTreeNode a2 = new FilterTreeNode(LangModelSchedule.getString("Doing"), "PROCESSING");
	FilterTreeNode a3 = new FilterTreeNode(LangModelSchedule.getString("ReadyToDo"), "SCHEDULED");

	public StatusTree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		this.aContext = aContext;
		root.add(a1);
		root.add(a2);
		root.add(a3);
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}