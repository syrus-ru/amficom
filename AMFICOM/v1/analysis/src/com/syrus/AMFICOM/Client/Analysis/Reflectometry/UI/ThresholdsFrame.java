package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.io.BellcoreStructure;

public class ThresholdsFrame extends SimpleResizableFrame
implements OperationListener, BsHashChangeListener, EtalonMTMListener
{
	private Dispatcher dispatcher;
	Map traces = new HashMap();

	public ThresholdsFrame(Dispatcher dispatcher)
	{
		super (new ThresholdsLayeredPanel(dispatcher));

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

	private void jbInit() throws Exception
	{
		setTitle(LangModelAnalyse.getString("thresholdsTitle"));
	}

	public Dispatcher getInternalDispatcher ()
	{
		return dispatcher;
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
	}

	public void addEtalon()
	{
		if (traces.get(Heap.ETALON_TRACE_KEY) != null)
			return;

		BellcoreStructure bs = Heap.getBSEtalonTrace();
		if (bs != null)
			addTrace (Heap.ETALON_TRACE_KEY);
		/* не нужно?
		else
		{
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
					if (type.getCodename().equals(ParameterTypeCodenames.TRACE_LENGTH)) // FIXME: подозреваю, нам больше подойдет длина из BS, а не из параметров теста
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
					double[] y = mtm.getModelTrace().getYArrayZeroPad(0, n);
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
		}*/
	}

	void removeEtalon()
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(Heap.ETALON_TRACE_KEY);
		if (epPanel != null)
			panel.removeGraphPanel(epPanel);
	}

	void addTrace (String id)
	{
		if (traces.get(id) != null)
			return;
		SimpleGraphPanel p;
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);

		double deltaX = bs.getResolution();
		double[] y = bs.getTraceData();

	    // XXX: MODELED_TRACE_KEY case check removed by saa: we don't know now how to handle MODELED_TRACE_KEY, so take a BS only 
		if (id.equals(Heap.PRIMARY_TRACE_KEY))
		{
			p = new ThresholdsPanel(panel, dispatcher, y, deltaX);
			ModelTraceAndEvents mtae = Heap.getMTAEPrimary();
			((ThresholdsPanel)p).updateTrace(mtae);
			((ThresholdsPanel)p).updEvents(Heap.PRIMARY_TRACE_KEY);
			((ThresholdsPanel)p).updateNoiseLevel();
			((ThresholdsPanel)p).draw_min_trace_level = true;
		}
		else
		{
			p = new SimpleGraphPanel(y, deltaX);
		}
		p.setWeakColors(true);

		((ThresholdsLayeredPanel)panel).addGraphPanel(p);
		panel.updScale2fit();
		((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv(.2, 1.);
		p.setColorModel(id);
		traces.put(id, p);

		setVisible(true);
	}

	void removeTrace (String id)
	{
		if (id.equals("all"))
		{
			((ThresholdsLayeredPanel)panel).removeAllGraphPanels();
			traces = new HashMap();
		}
		else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)traces.get(id);
			if (p != null)
			{
				panel.removeGraphPanel(p);
				traces.remove(id);
				((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv(.2, 1.);
			}
		}
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
		removeEtalon();
	}
}
