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
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "");

	public MoneTree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		this.aContext = aContext;
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
		//DataSet dSet = new DataSet(Pool.getHash(MonitoredElement.typ));
		Map dSet = Pool.getHash(MonitoredElement.typ);
		filter.filtrate(dSet);
		//for(Enumeration en = dSet.elements(); en.hasMoreElements();)
		for(Iterator it=dSet.keySet().iterator();it.hasNext();)
		{
			MonitoredElement path = (MonitoredElement )dSet.get(it.next());
			this.root.add(new FilterTreeNode(path.getName(), path.getId()));
		}
		TreeModelClone myModel = new TreeModelClone(this.root);
		this.tree = new JTree(myModel);
	}
}

