package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.*;

public class AlarmTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.String("labelRoot"), "root");
	FilterTreeNode a1 = new FilterTreeNode(LangModelSchedule.String("labelAlarmTest"), "alarm");
	FilterTreeNode a2 = new FilterTreeNode(LangModelSchedule.String("labelNoAlarmTest"), "noalarm");

	public AlarmTree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		this.aContext = aContext;
		root.add(a1);
		root.add(a2);
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}