package com.syrus.AMFICOM.Client.Schedule.Filter;

import java.util.*;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.DomainCondition;

public class MoneTree extends FilterTree
{
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "");

	public void setTree(ApplicationContext aContext)
	{
		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.ME_ENTITY_CODE);
			List mes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (Iterator it = mes.iterator(); it.hasNext(); ) {
				MonitoredElement me = (MonitoredElement)it.next();
				root.add(new FilterTreeNode(me.getName(), me.getId().getIdentifierString()));
			}
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
		TreeModelClone myModel = new TreeModelClone(root);
		this.tree = new JTree(myModel);
	}
}
