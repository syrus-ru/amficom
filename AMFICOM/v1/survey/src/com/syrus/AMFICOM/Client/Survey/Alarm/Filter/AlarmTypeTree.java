package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import java.util.*;
import javax.swing.*;

public class AlarmTypeTree extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModelSurvey.getString("Root"), "root");

	public AlarmTypeTree()
	{
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