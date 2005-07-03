package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class AlarmStatusTree extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModelSurvey.getString("Root"), "root");
	FilterTreeNode a1 = new FilterTreeNode(LangModelSurvey.getString("New"), "GENERATED");
	FilterTreeNode a2 = new FilterTreeNode(LangModelSurvey.getString("Assigned"), "ASSIGNED");
	FilterTreeNode a3 = new FilterTreeNode(LangModelSurvey.getString("Fixed"), "FIXED");

	public AlarmStatusTree(){
		// empty constuctor
	}

	public void setTree(ApplicationContext aContext)
	{
		root.add(a1);
		root.add(a2);
		root.add(a3);
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}

