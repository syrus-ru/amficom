package com.syrus.AMFICOM.Client.Schedule.Filter;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class AlarmTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "root");
	FilterTreeNode a1 = new FilterTreeNode(LangModelSchedule.getString("AlarmTest"), "alarm");
	FilterTreeNode a2 = new FilterTreeNode(LangModelSchedule.getString("NoAlarmTest"), "noalarm");

	public AlarmTree(){
		// empty constuctor
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

