package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.beans.*;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.*;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.io.BellcoreStructure;

public class ThresholdsFrame extends SimpleResizableFrame
implements BsHashChangeListener, EtalonMTMListener, PropertyChangeListener
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

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
		this.dispatcher.addPropertyChangeListener(RefUpdateEvent.typ, this);
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	public void addEtalon()
	{
		if (traces.get(Heap.ETALON_TRACE_KEY) != null)
			return;

		BellcoreStructure bs = Heap.getBSEtalonTrace();
		if (bs != null)
			addTrace (Heap.ETALON_TRACE_KEY);
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
