package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.util.*;
import javax.swing.*;

public class MoneTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.String("labelRoot"), "");

	public MoneTree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		this.aContext = aContext;
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
		DataSet dSet = new DataSet(Pool.getHash(MonitoredElement.typ));
		dSet = filter.filter(dSet);
		for(Enumeration en = dSet.elements(); en.hasMoreElements();)
		{
			MonitoredElement path = (MonitoredElement )en.nextElement();
			root.add(new FilterTreeNode(path.getName(), path.getId()));
		}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}