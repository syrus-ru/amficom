package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.util.*;
import javax.swing.*;

public class KISTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "");

	public KISTree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		this.aContext = aContext;
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
		//DataSet dSet = new DataSet(Pool.getHash(KIS.typ));
		Map dSet = Pool.getHash(KIS.typ);
		filter.filtrate(dSet);
		//for(Enumeration en = dSet.elements(); en.hasMoreElements();)
		for(Iterator it=dSet.keySet().iterator();it.hasNext();)
		{
			//KIS kis = (KIS )en.nextElement();
			KIS kis = (KIS)dSet.get(it.next());
			FilterTreeNode kisnode = new FilterTreeNode(kis.getName(), kis.getId());
			root.add(kisnode);
			for(Enumeration enu = kis.access_ports.elements(); enu.hasMoreElements();)
			{
				AccessPort ap = (AccessPort )enu.nextElement();
				FilterTreeNode portnode = new FilterTreeNode(ap.getName(), ap.getId());
				kisnode.add(portnode);
				FilterTreeNode vol_ = new FilterTreeNode(LangModelSchedule.getString("TestTypes"), "mone");
				FilterTreeNode tt_ = new FilterTreeNode(LangModelSchedule.getString("TestTypes"), "testtypes");
				portnode.add(vol_);
				portnode.add(tt_);
				DataSet daSet = new DataSet(Pool.getHash(MonitoredElement.typ));
				daSet = filter.filter(daSet);
				for(Enumeration enum = daSet.elements(); enum.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )enum.nextElement();
					if(me.access_port_id.equals(ap.getId()))
					{
						FilterTreeNode pathnode = new FilterTreeNode(me.getName(), me.getId());
						vol_.add(pathnode);
					}
				}
				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
				Vector vec = new Vector();
				for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
				{
					TestType tt = (TestType )enum.nextElement();
					if(apt.test_type_ids.contains(tt.getId()))
					{
						FilterTreeNode ttnode = new FilterTreeNode(tt.getName(), tt.getId());
						tt_.add(ttnode);
					}
				}
			}

		}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}

