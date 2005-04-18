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
	Dispatcher dispatcher;
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
				this_componentShown(e);
			}
		});
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("analysisTitle");
	}

	public Dispatcher getInternalDispatcher()
	{
		return dispatcher;
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	void this_componentShown(ComponentEvent e)
	{
		((AnalysisLayeredPanel)panel).updMarkers();
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

	public void addEtalon()
	{
		if (traces.get(Heap.ETALON_TRACE_KEY) != null)
			return;

		BellcoreStructure bs = Heap.getBSEtalonTrace();
		if (bs != null)
			addTrace (Heap.ETALON_TRACE_KEY);
		/* XXX: remove it: Стас говорит что это неактуально
		else
		{
			if (bs.measurementId == null)
				return;

			int n = 0;
			double deltaX = 0;

			MeasurementSetup ms = Heap.getContextMeasurementSetup();
			if (ms == null)
				return;
			SetParameter[] params = ms.getParameterSet().getParameters();
			double len = 0;
			try
			{
				for (int i = 0; i < params.length; i++)
				{
					ParameterType type = (ParameterType)params[i].getType();
					if (type.getCodename().equals(ParameterTypeCodenames.TRACE_RESOLUTION))
					{
						deltaX = new ByteArray(params[i].getValue()).toDouble();
					}
					if (type.getCodename().equals(ParameterTypeCodenames.TRACE_LENGTH))
					{
						len = new ByteArray(params[i].getValue()).toDouble();
					}
				}
				if (deltaX == 0 || len == 0)
					return;
				n = (int)(len * 1000 / deltaX);

				SimpleGraphPanel oldpanel = (SimpleGraphPanel)traces.get(Heap.ETALON_TRACE_KEY);
				if (oldpanel != null)
				{
					((ScalableLayeredPanel)panel).removeGraphPanel(oldpanel);
					traces.remove(Heap.ETALON_TRACE_KEY);
				}

				ModelTraceManager mtm = Heap.getMTMEtalon();
				if (mtm != null)
				{
					double[] y = new double[n];
					y = mtm.getModelTrace().getYArrayZeroPad(0, n);
					SimpleGraphPanel epPanel = new SimpleGraphPanel(y, deltaX);
					epPanel.setColorModel(AnalysisUtil.ETALON);
					((ScalableLayeredPanel)panel).addGraphPanel(epPanel);
					panel.updScale2fit();
					traces.put(Heap.ETALON_TRACE_KEY, epPanel);
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}*/
	}

	public void removeEtalon(String etId)
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