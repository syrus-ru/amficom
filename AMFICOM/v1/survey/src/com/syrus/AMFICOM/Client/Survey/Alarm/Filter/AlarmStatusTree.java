package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import javax.swing.*;

public class AlarmStatusTree extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModelSurvey.String("labelRoot"), "root");
	FilterTreeNode a1 = new FilterTreeNode(LangModelSurvey.String("labelALARM_STATUS_GENERATED"), "GENERATED");
	FilterTreeNode a2 = new FilterTreeNode(LangModelSurvey.String("labelALARM_STATUS_ASSIGNED"), "ASSIGNED");
	FilterTreeNode a3 = new FilterTreeNode(LangModelSurvey.String("labelALARM_STATUS_FIXED"), "FIXED");

	public AlarmStatusTree()
	{
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