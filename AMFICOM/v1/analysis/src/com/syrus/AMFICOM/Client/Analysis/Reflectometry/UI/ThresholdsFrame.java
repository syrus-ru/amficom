package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;

public class ThresholdsFrame extends SimpleResizableFrame
implements BsHashChangeListener, EtalonMTMListener, PropertyChangeListener {
	private Dispatcher dispatcher;
	Map traces = new HashMap();

	public ThresholdsFrame(Dispatcher dispatcher) {
		super (new ThresholdsLayeredPanel(dispatcher));
		setTitle(I18N.getString(AnalysisResourceKeys.FRAME_THRESHOLDS));
		init_module(dispatcher);
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
		if (this.traces.get(Heap.ETALON_TRACE_KEY) != null)
			return;

		PFTrace pf = Heap.getPFTraceEtalon();
		if (pf != null)
			addTrace (Heap.ETALON_TRACE_KEY);
	}

	void removeEtalon()
	{
		SimpleGraphPanel epPanel = (SimpleGraphPanel)this.traces.get(Heap.ETALON_TRACE_KEY);
		if (epPanel != null)
			this.panel.removeGraphPanel(epPanel);
	}

	void addTrace (String id)
	{
		if (this.traces.get(id) != null)
			return;
		SimpleGraphPanel p;
		PFTrace pf = Heap.getAnyPFTraceByKey(id);

		double deltaX = pf.getResolution();
		double[] y = pf.getFilteredTraceClone();

		ThresholdsLayeredPanel ppp = (ThresholdsLayeredPanel)this.panel;
		// XXX: MODELED_TRACE_KEY case check removed by saa: we don't know now how to handle MODELED_TRACE_KEY, so take a BS only 
		if (id.equals(Heap.PRIMARY_TRACE_KEY))
		{
			p = new ThresholdsPanel(ppp, this.dispatcher, y, deltaX);
			((ThresholdsPanel)p).updEvents(Heap.PRIMARY_TRACE_KEY);
			((ThresholdsPanel)p).updateNoiseLevel();
			((ThresholdsPanel)p).minTraceLevel.setDrawed(true);
		} else {
			//p = new SimpleGraphPanel(y, deltaX);
			p = new ReflectogramPanel(ppp, id, true);
		}
//		p.setWeakColors(true);

		ppp.addGraphPanel(p);
		ppp.updScale2fit();
		ppp.updScale2fitCurrentEv(.2, 1.);
		p.setColorModel(id);
		this.traces.put(id, p);
	}

	void removeTrace (String id)
	{
		if (id.equals("all"))
		{
			((ThresholdsLayeredPanel)this.panel).removeAllGraphPanels();
			this.traces = new HashMap();
		} else
		{
			SimpleGraphPanel p = (SimpleGraphPanel)this.traces.get(id);
			if (p != null)
			{
				this.panel.removeGraphPanel(p);
				this.traces.remove(id);
				((ThresholdsLayeredPanel)this.panel).updScale2fitCurrentEv(.2, 1.);
			}
		}
	}

	public void bsHashAdded(String key)
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
				SimpleGraphPanel p = (SimpleGraphPanel)this.traces.get(tr.getId());
				if (p != null) {
					p.setShowAll(tr.isShown());
					this.panel.repaint();
				}
			}
		}
	}
}
