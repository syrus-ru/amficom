package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

// base class changed by saa on Stas's direction
// from com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel
// to com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeModel

//import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
//import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeModel;
import com.syrus.AMFICOM.client_.general.ui_.tree.ObjectResourceTreeNode;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.io.BellcoreStructure;

class TestSetupTreeModel extends ObjectResourceTreeModel
{
	ApplicationContext aContext;

	public TestSetupTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public ObjectResourceTreeNode getRoot()
	{
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
		try
		{
			MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(
						 new Identifier(bs.monitoredElementId), true);
			return new ObjectResourceTreeNode("root", "Шаблоны на \"" +
					(me.getName().equals("") ? me.getId().getIdentifierString() : me.getName()) + "\"", true);
		}
		catch(ApplicationException ex)
		{
			return new ObjectResourceTreeNode("root", "Шаблоны", true);
		}
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	private Class getNodeClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof MeasurementSetup)
		{
			return MeasurementSetup.class;
		}
		else
			return null;
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		return MeasurementSetup.class;
	}

	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		ObjectResourceTreeNode ortn;

		if(node.getObject() instanceof String)
		{
			String s = (String)node.getObject();

			if(s.equals("root"))
			{
				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
				if (bs != null && !bs.monitoredElementId.equals(""))
				{
					Identifier me_id = new Identifier(bs.monitoredElementId);
					Identifier domainId = ((RISDSessionInfo)aContext.getSessionInterface()).getDomainIdentifier();
					try
					{
						Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(domainId, true);

						StorableObjectCondition mSetupCondition = new DomainCondition(domain, ObjectEntities.MS_ENTITY_CODE);
						List mSetups = MeasurementStorableObjectPool.getStorableObjectsByCondition(mSetupCondition, true);

						java.util.Set testsHt = new HashSet();
						for(Iterator it = mSetups.iterator(); it.hasNext(); )
						{
							MeasurementSetup t = (MeasurementSetup)it.next();
							List me_ids = t.getMonitoredElementIds();
							for(Iterator it2 = me_ids.iterator(); it2.hasNext(); )
							{
								Identifier meId = (Identifier)it2.next();
								if(meId.equals(me_id))
								{
									testsHt.add(t);
									break;
								}
							}
						}
						// FIXME
						/*
						ObjectResourceSorter sorter = StubResource.getDefaultSorter();
						sorter.setDataSet(testsHt);
						for(Iterator it = sorter.default_sort().iterator(); it.hasNext(); )
						{
							MeasurementSetup t = (MeasurementSetup)it.next();
							ortn = new ObjectResourceTreeNode(t, t.getDescription(), true, true);
							vec.add(ortn);
						}
						*/
						// quick fix by saa.
						for(Iterator it = testsHt.iterator(); it.hasNext(); )
						{
							MeasurementSetup t = (MeasurementSetup)it.next();
							ortn = new ObjectResourceTreeNode(t, t.getDescription(), true, true);
							vec.add(ortn);
						}
					}
					catch(ApplicationException ex1)
					{
					}
				}
			}
		}
		return vec;
	}
}

