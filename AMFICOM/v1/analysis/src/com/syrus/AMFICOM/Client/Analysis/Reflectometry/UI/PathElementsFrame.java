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

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.io.BellcoreStructure;

public class PathElementsFrame extends AnalysisFrame
{
	public PathElementsFrame(Dispatcher dispatcher, AnalysisLayeredPanel panel)
	{
		super(dispatcher, panel);
	}

	public PathElementsFrame(Dispatcher dispatcher)
	{
		this(dispatcher, new PathElementsLayeredPanel(dispatcher));
	}

	public void addTrace (String id)
	{
		if (traces.get(id) != null)
			return;
		SimpleGraphPanel p;
		BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
		if (bs == null)
			return;

		double delta_x = bs.getResolution();
		double[] y = bs.getTraceData();

		if (id.equals("primarytrace") || id.equals("modeledtrace"))
		{
			SchemePath path = null;

			try
			{
				MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(bs.monitoredElementId, true);
				if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH))
				{
					List tpathIds = me.getMonitoredDomainMemberIds();
					List paths = Pool.getList(SchemePath.typ);
					if (path != null)
						for(Iterator it = paths.iterator(); it.hasNext(); )
						{
							SchemePath sp = (SchemePath)it.next();
							/**
							 * @todo remove comment when SchemePath moves to new TransmissionPath
							 */
//						if(sp.path != null && tpathIds.contains(sp.path.getId()))
							{
								path = sp;
								break;
							}
						}
				}
				setTitle(me.getName());
			}
			catch(ApplicationException ex)
			{
				setTitle(LangModelAnalyse.getString("analysisTitle"));
			}

			p = new PathElementsPanel((PathElementsLayeredPanel)panel, dispatcher, y, delta_x);
			((PathElementsPanel)p).updEvents(id);
			((PathElementsPanel)p).updateNoiseLevel();
			((PathElementsPanel)p).draw_noise_level = true;

			if (path != null)
				((PathElementsPanel)p).setPath(path);
		}
		else
			p = new SimpleGraphPanel(y, delta_x);
		p.setColorModel(id);
		((PathElementsLayeredPanel)panel).addGraphPanel(p);
		((PathElementsLayeredPanel)panel).updPaintingMode();
		panel.updScale2fit();
		traces.put(id, p);

		setVisible(true);
	}
}