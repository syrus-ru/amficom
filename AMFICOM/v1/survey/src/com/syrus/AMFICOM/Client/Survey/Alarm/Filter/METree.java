package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import java.util.*;
import javax.swing.*;

public class METree extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModel.getString("labelRoot"), "root");

	public METree()
	{
	}

	public void setTree(ApplicationContext aContext)
	{
		Hashtable ht = Pool.getHash(KIS.typ);
		if(ht != null)
		for(Enumeration en = ht.elements(); en.hasMoreElements();)
		{
			KIS kis = (KIS )en.nextElement();
			FilterTreeNode kisnode = new FilterTreeNode(kis.getName(), kis.getId());
			root.add(kisnode);

			for(Enumeration enu = kis.access_ports.elements(); enu.hasMoreElements();)
			{
				AccessPort ap = (AccessPort )enu.nextElement();

				Hashtable ht2 = Pool.getHash(MonitoredElement.typ);
				if(ht2 != null)
				for(Enumeration en2 = ht2.elements(); en2.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en2.nextElement();
					if(me.access_port_id.equals(ap.getId()))
					{
						kisnode.add(new FilterTreeNode(me.getName(), me.getId()));
					}
				}
			}
		}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
	}
}