package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.io.BellcoreStructure;

public class PathElementsFrame extends AnalysisFrame
{
	ApplicationContext aContext;
	public PathElementsFrame(ApplicationContext aContext, Dispatcher dispatcher, AnalysisLayeredPanel panel)
	{
		super(dispatcher, panel);
		this.aContext = aContext;
	}

	public PathElementsFrame(ApplicationContext aContext, Dispatcher dispatcher)
	{
		this(aContext, dispatcher, new PathElementsLayeredPanel(dispatcher));
	}

	public void addTrace (String id)
	{
		if (traces.get(id) != null)
			return;
		SimpleGraphPanel p;
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs == null)
			return;

		double deltaX = bs.getResolution();
		double[] y = bs.getTraceData();

		if (id.equals("primarytrace") || id.equals("modeledtrace"))
		{
			SchemePath path = null;

			try
			{
				MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(
								new Identifier(bs.monitoredElementId), true);

				if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
							domain_id, true);
					DomainCondition condition = new DomainCondition(domain,
							ObjectEntities.SCHEME_PATH_ENTITY_CODE);
					List paths = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					Collection tpathIds = me.getMonitoredDomainMemberIds();
					for (Iterator it = paths.iterator(); it.hasNext(); ) {
						SchemePath sp = (SchemePath)it.next();
						/**
						 * @todo remove comment when SchemePath moves to new TransmissionPath
						 */
						if(sp.getTransmissionPath() != null && tpathIds.contains(sp.getTransmissionPath().getId()))
						{
							path = sp;
							break;
						}
					}
				}
				setTitle(me.getName());
			}
			catch(Exception ex)
			{
				setTitle(LangModelAnalyse.getString("analysisTitle"));
			}

			p = new PathElementsPanel((PathElementsLayeredPanel)panel, dispatcher, y, deltaX);
			((PathElementsPanel)p).updEvents(id);
			((PathElementsPanel)p).updateNoiseLevel();
			((PathElementsPanel)p).draw_noise_level = true;

			if (path != null)
				((PathElementsPanel)p).setPath(path);
		}
		else
			p = new SimpleGraphPanel(y, deltaX);
		p.setColorModel(id);
		((PathElementsLayeredPanel)panel).addGraphPanel(p);
		((PathElementsLayeredPanel)panel).updPaintingMode();
		panel.updScale2fit();
		traces.put(id, p);

		setVisible(true);
	}
}