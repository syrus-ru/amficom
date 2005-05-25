package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemePath;
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
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
		if (bs == null)
			return;

		double deltaX = bs.getResolution();
		double[] y = bs.getTraceData();

		if (id.equals(Heap.PRIMARY_TRACE_KEY) || id.equals(Heap.MODELED_TRACE_KEY))
		{
			SchemePath path = null;

			try
			{
				MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(
								new Identifier(bs.monitoredElementId), true);

				if (me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
					LinkedIdsCondition condition = new LinkedIdsCondition(LoginManager.getDomainId(),
							ObjectEntities.SCHEME_PATH_ENTITY_CODE);
					Set paths = StorableObjectPool.getStorableObjectsByCondition(condition, true);

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
