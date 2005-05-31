package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ComponentEvent;
import java.beans.*;
import java.util.HashMap;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.*;
import com.syrus.io.BellcoreStructure;

public class AnalysisFrame extends ScalableFrame implements BsHashChangeListener, EtalonMTMListener, PropertyChangeListener
{
	protected Dispatcher dispatcher;
	public HashMap traces = new HashMap();

	protected AnalysisFrame(Dispatcher dispatcher, AnalysisLayeredPanel panel)
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
		this.dispatcher.addPropertyChangeListener(RefUpdateEvent.typ, this);
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	protected void this_componentShown()
	{
		((AnalysisLayeredPanel)panel).updMarkers();
	}

	protected void addTrace (String id)
	{
		removeOneTrace(id);

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
				MonitoredElement me = (MonitoredElement)StorableObjectPool.getStorableObject(
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

	private void removeOneTrace(String id)
	{
		SimpleGraphPanel p = (SimpleGraphPanel)traces.get(id);
		if (p != null)
		{
			panel.removeGraphPanel(p);
			traces.remove(id);
			((ScalableLayeredPanel)panel).updScale2fit();
		}
	}

	private void removeAllTraces()
	{
		((ScalableLayeredPanel)panel).removeAllGraphPanels();
		traces = new HashMap();
	}

	private void addEtalon()
	{
		removeEtalon();

		SimpleGraphPanel p;

		ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
		p = new SimpleGraphPanel(mtae.getModelTrace().getYArray(), mtae.getDeltaX());
		p.setColorModel(Heap.ETALON_TRACE_KEY);
		((AnalysisLayeredPanel)panel).addGraphPanel(p);
		((AnalysisLayeredPanel)panel).updPaintingMode();
		panel.updScale2fit();

		traces.put(Heap.ETALON_TRACE_KEY, p);

		setVisible(true);
	}

	private void removeEtalon()
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(Heap.ETALON_TRACE_KEY);
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
		removeOneTrace(key);
	}

	public void bsHashRemovedAll()
	{
		removeAllTraces();
		setVisible (false);
	}

	public void etalonMTMCUpdated()
	{
		removeEtalon();
		addEtalon();
	}

	public void etalonMTMRemoved()
	{
		removeEtalon();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(RefUpdateEvent.typ)) {
			RefUpdateEvent ev = (RefUpdateEvent)evt;
			if (ev.traceChanged()) {
				TraceResource tr = (TraceResource)evt.getNewValue();
				SimpleGraphPanel p = (SimpleGraphPanel)traces.get(tr.getId());
				if (p != null) {
					p.setShown(tr.isShown());
					panel.repaint();
				}
			}
		}
	}
}
