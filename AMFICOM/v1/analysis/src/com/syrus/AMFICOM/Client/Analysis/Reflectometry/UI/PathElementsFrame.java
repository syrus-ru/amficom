package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
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

		double delta_x = bs.getDeltaX();
		double[] y = bs.getTraceData();

		if (id.equals("primarytrace") || id.equals("modeledtrace"))
		{
			SchemePath path = null;

			if (bs.monitored_element_id.equals(""))
				bs.monitored_element_id = "mone2";

			if (!bs.monitored_element_id.equals(""))
			{
				MonitoredElement me = (MonitoredElement)Pool.get(MonitoredElement.typ, bs.monitored_element_id);
				setTitle(me.getName());
				if (me.element_type.equals("path"))
				{
					TransmissionPath tpath = (TransmissionPath)Pool.get(TransmissionPath.typ, me.element_id);
					for (Iterator it = Pool.getMap(SchemePath.typ).values().iterator(); it.hasNext();)
					{
						SchemePath sp = (SchemePath)it.next();
						if (sp.path_id.equals(tpath.getId()))
						{
							path = sp;
							break;
						}
					}
				}
			}
			else
				setTitle(LangModelAnalyse.getString("analysisTitle"));

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