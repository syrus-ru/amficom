package com.syrus.AMFICOM.Client.Schedule.Filter;

import java.util.*;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.DomainCondition;

public class TestTypeTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "");

	public void setTree(ApplicationContext aContext)
	{
		try {
			Identifier domainId = new Identifier(aContext.getSessionInterface().getDomainId());
			Domain domain = (Domain)ConfigurationStorableObjectPool.
					getStorableObject(domainId, true);
			StorableObjectCondition condition = new DomainCondition(domain,
					ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
			List measurementTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(
					condition, true);

			for (Iterator it = measurementTypes.iterator(); it.hasNext(); ) {
				MeasurementType mt = (MeasurementType)it.next();
				root.add(new FilterTreeNode(mt.getDescription(), mt.getId().getIdentifierString()));
			}
			TreeModelClone myModel = new TreeModelClone(root);
			tree = new JTree(myModel);
		}
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}
}

