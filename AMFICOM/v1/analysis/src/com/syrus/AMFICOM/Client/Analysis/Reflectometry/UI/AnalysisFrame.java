package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.io.IOException;
import java.util.HashMap;

import java.awt.event.*;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.ByteArray;

public class AnalysisFrame extends ScalableFrame implements OperationListener
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
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN)
			{
				String id = (String)(rce.getSource());
				addTrace (id);
				setVisible(true);
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				removeTrace(id);
				if (id.equals("all"))
					setVisible (false);
			}
			if (rce.OPEN_ETALON)
			{
				String etId = (String)rce.getSource();
				addEtalon(etId);
			}
			if (rce.CLOSE_ETALON)
			{
				String etId = (String)rce.getSource();
				removeEtalon(etId);
			}
		}
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

		if (id.equals(RefUpdateEvent.PRIMARY_TRACE) || id.equals("modeledtrace"))
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

	public void removeTrace (String id)
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

	public void addEtalon(String id)
	{
		if (traces.get(id) != null)
			return;

		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
		if (bs != null)
			addTrace (id);
		else
		{
			if (bs.measurementId == null)
				return;

			int n = 0;
			double deltaX = 0;

//			Measurement m = null;
//			try
//			{
//				m = (Measurement)MeasurementStorableObjectPool.getStorableObject(
//								new Identifier(bs.measurementId), true);
//			}
//			catch(ApplicationException ex)
//			{
//				System.err.println("Exception retrieving measurenent with " + bs.measurementId);
//				ex.printStackTrace();
//				return;
//			}

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

				SimpleGraphPanel oldpanel = (SimpleGraphPanel)traces.get(id);
				if (oldpanel != null)
				{
					((ScalableLayeredPanel)panel).removeGraphPanel(oldpanel);
					traces.remove(id);
				}

				ModelTraceManager mtm = Heap.getMTMByKey(id);
				if (mtm != null)
				{
					double[] y = new double[n];
					y = mtm.getModelTrace().getYArrayZeroPad(0, n);
					SimpleGraphPanel epPanel = new SimpleGraphPanel(y, deltaX);
					epPanel.setColorModel(AnalysisUtil.ETALON);
					((ScalableLayeredPanel)panel).addGraphPanel(epPanel);
					panel.updScale2fit();
					traces.put(id, epPanel);
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public void removeEtalon(String etId)
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(etId);
		panel.removeGraphPanel(epPanel);
	}
}