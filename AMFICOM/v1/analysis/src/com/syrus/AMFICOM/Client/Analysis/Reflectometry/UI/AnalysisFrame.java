package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ComponentEvent;
import java.util.HashMap;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.io.BellcoreStructure;

public class AnalysisFrame extends ScalableFrame implements BsHashChangeListener, EtalonMTMListener
{
	protected Dispatcher dispatcher;
	public HashMap traces = new HashMap();

	public AnalysisFrame(Dispatcher dispatcher, AnalysisLayeredPanel panel)
	{
		super (panel);

		init_module(dispatcher);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public AnalysisFrame(Dispatcher dispatcher)
	{
		this(dispatcher, new AnalysisLayeredPanel(dispatcher));
	}

	private void jbInit() throws Exception
	{
		setTitle(LangModelAnalyse.getString("analysisTitle"));
		addComponentListener(new java.awt.event.ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{
				this_componentShown();
			}
		});
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("analysisTitle");
	}

	private void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	protected void this_componentShown()
	{
		((AnalysisLayeredPanel)panel).updMarkers();
	}

	protected void addTrace (String id)
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
			try
			{
				MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(
								new Identifier(bs.monitoredElementId), true);
				setTitle(me.getName());
			}
			catch(Exception ex)
			{
				setTitle(LangModelAnalyse.getString("analysisTitle"));
			}

			p = new AnalysisPanel((AnalysisLayeredPanel)panel, dispatcher, y, deltaX);
			((AnalysisPanel)p).updEvents(id);
			((AnalysisPanel)p).updateNoiseLevel();
			((AnalysisPanel)p).draw_noise_level = true;
		}
		else
			p = new SimpleGraphPanel(y, deltaX);
		p.setColorModel(id);
		((AnalysisLayeredPanel)panel).addGraphPanel(p);
		((AnalysisLayeredPanel)panel).updPaintingMode();
		panel.updScale2fit();
		traces.put(id, p);

		setVisible(true);
	}

	private void removeTrace (String id) // @todo: split to removeOneTrace and removeAllTraces
	{
		if (id.equals("all"))
		{
			((ScalableLayeredPanel)panel).removeAllGraphPanels();
			traces = new HashMap();
		}
		else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)traces.get(id);
			if (p != null)
			{
				panel.removeGraphPanel(p);
				traces.remove(id);
				((ScalableLayeredPanel)panel).updScale2fit();
			}
		}
	}

	private void addEtalon()
	{
		if (traces.get(Heap.ETALON_TRACE_KEY) != null)
			return;

		BellcoreStructure bs = Heap.getBSEtalonTrace();
		if (bs != null)
			addTrace (Heap.ETALON_TRACE_KEY);
	}

	private void removeEtalon(String etId)
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(etId);
		if (epPanel != null)
			panel.removeGraphPanel(epPanel);
	}

	public void bsHashAdded(String key, BellcoreStructure bs)
	{
		addTrace (key);
		setVisible(true);
	}

	public void bsHashRemoved(String key)
	{
		removeTrace(key);
	}

	public void bsHashRemovedAll()
	{
		removeTrace("all");
		setVisible (false);
	}

	public void etalonMTMCUpdated()
	{
		addEtalon();
	}

	public void etalonMTMRemoved()
	{
		removeEtalon(Heap.ETALON_TRACE_KEY);
	}
}
