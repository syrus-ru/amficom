package com.syrus.AMFICOM.Client.Schedule.Filter;

import java.util.*;

import javax.swing.JTree;

import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

public class KISTree extends FilterTree
{
	ApplicationContext aContext;
	FilterTreeNode root = new FilterTreeNode(LangModelSchedule.getString("Root"), "");

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
					MeasurementPort port = (MeasurementPort)MeasurementStorableObjectPool.getStorableObject(
									 me.getMeasurementPortId(), true);

					FilterTreeNode portnode = new FilterTreeNode(port.getName(),
							port.getId().getIdentifierString());
					kisnode.add(portnode);

					FilterTreeNode vol_ = new FilterTreeNode(LangModelSchedule.getString("TestTypes"), "mone");
					FilterTreeNode tt_ = new FilterTreeNode(LangModelSchedule.getString("TestTypes"), "testtypes");
					portnode.add(vol_);
					portnode.add(tt_);

					FilterTreeNode pathnode = new FilterTreeNode(me.getName(), me.getId().getIdentifierString());
					vol_.add(pathnode);

					MeasurementPortType pType = (MeasurementPortType)port.getType();
					LinkedIdsCondition mCondition = LinkedIdsCondition.getInstance();
					mCondition.setIdentifier(pType.getId());
					mCondition.setEntityCode(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					List measurementTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(
							mCondition, true);

					for (Iterator it3 = measurementTypes.iterator(); it.hasNext();)
					{
						MeasurementType type = (MeasurementType)it.next();
						FilterTreeNode ttnode = new FilterTreeNode(type.getDescription(),
								type.getId().getIdentifierString());
						tt_.add(ttnode);
					}
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

