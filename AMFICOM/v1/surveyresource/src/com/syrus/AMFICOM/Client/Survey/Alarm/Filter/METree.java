package com.syrus.AMFICOM.Client.Survey.Alarm.Filter;

import java.util.*;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.DomainCondition;

public class METree	extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModel.getString("labelRoot"), "root");

	public void setTree(ApplicationContext aContext)
	{
		try {
			Identifier domainId = new Identifier(aContext.getSessionInterface().getDomainId());
			Domain domain = (Domain)AdministrationStorableObjectPool.
					getStorableObject(domainId, true);
			StorableObjectCondition condition = new DomainCondition(domain,
					ObjectEntities.KIS_ENTITY_CODE);
			List kiss = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
					condition, true);

			for (Iterator it = kiss.iterator(); it.hasNext(); ) {
				KIS kis = (KIS)it.next();
				FilterTreeNode kisnode = new FilterTreeNode(kis.getName(),
						kis.getId().getIdentifierString());
				root.add(kisnode);

				List mes = kis.retrieveMonitoredElements();

				for (Iterator it2 = mes.iterator(); it2.hasNext(); ) {
					MonitoredElement me = (MonitoredElement)it.next();
					kisnode.add(new FilterTreeNode(me.getName(),
							me.getId().getIdentifierString()));
				}
			}
			TreeModelClone myModel = new TreeModelClone(root);
			tree = new JTree(myModel);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}
}