package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.util.*;
import javax.swing.*;

public class TestTypeTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "");

	public void setTree(ApplicationContext aContext)
	{
		this.aContext = aContext;
		Hashtable ht = Pool.getHash(TestType.typ);
		for(Enumeration en = ht.elements(); en.hasMoreElements();)
		{
			TestType tt = (TestType )en.nextElement();
			root.add(new FilterTreeNode(tt.getName(), tt.getId()));
		}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}

