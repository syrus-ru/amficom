package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.io.IOException;
import java.util.*;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.ByteArray;

public class ThresholdsFrame extends SimpleResizableFrame
implements OperationListener, bsHashChangeListener, EtalonMTMListener
{
	private Dispatcher dispatcher;
	Map traces = new HashMap();
	Map bellcoreTraces = null; // надо знать, какие р/г отображены

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

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		bellcoreTraces = new HashMap();
		Heap.setBsBellCoreMap(bellcoreTraces);
		//dispatcher.register(this, RefChangeEvent.typ);
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

	void removeEtalon(String etId)
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(etId);
		panel.removeGraphPanel(epPanel);
	}

	void addTrace (String id)
	{
		if (traces.get(id) != null)
			return;
		SimpleGraphPanel p;
		BellcoreStructure bs = Heap.getAnyBSTraceByKey(id);
		bellcoreTraces.put(id, bs);

		double deltaX = bs.getResolution();
		double[] y = bs.getTraceData();

		if (id.equals(Heap.PRIMARY_TRACE_KEY))
	    //if (id.equals(Heap.PRIMARY_TRACE_KEY)) removed by saa: XXX: we don't know now how to handle MODELED_TRACE_KEY, so take a BS only 
		{
			p = new ThresholdsPanel(panel, dispatcher, y, deltaX);
			ModelTraceManager mtm = Heap.getMTMByKey(id);
			((ThresholdsPanel)p).updateTrace(mtm);
			((ThresholdsPanel)p).updEvents(id);
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
			bellcoreTraces = new HashMap();
			Heap.setBsBellCoreMap(bellcoreTraces);
		}
		else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)traces.get(id);
			if (p != null)
			{
//				p.removeColorModel(id);
				panel.removeGraphPanel(p);
				traces.remove(id);
				bellcoreTraces.remove(id);
				((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv(.2, 1.);
			}
		}
	}

	public Map getBellcoreTraces()
	{
			return bellcoreTraces;
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
