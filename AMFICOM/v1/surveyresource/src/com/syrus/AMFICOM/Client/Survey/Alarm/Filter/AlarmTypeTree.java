package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import java.util.*;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Alarm.AlarmType;

public class AlarmTypeTree extends FilterTree
{
	private FilterTreeNode root = new FilterTreeNode(LangModelSurvey.getString("Root"), "root");

	public AlarmTypeTree()
	{
		// empty constructor
	}

	public void setTree(ApplicationContext aContext)
	{
		Hashtable ht = Pool.getHash(AlarmType.typ);
		if(ht != null)
			for(Enumeration en = ht.elements(); en.hasMoreElements();)
			{
				AlarmType at = (AlarmType )en.nextElement();
				root.add(new FilterTreeNode(at.getName(), at.getId()));
			}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}

