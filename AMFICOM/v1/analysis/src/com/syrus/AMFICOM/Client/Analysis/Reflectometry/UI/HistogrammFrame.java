package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.AnalysisParametersListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;

public class HistogrammFrame
extends ScalableFrame
implements BsHashChangeListener, AnalysisParametersListener {
	Dispatcher dispatcher;
	private Map<String, HistogrammPanel> currentPanels =
		new HashMap<String, HistogrammPanel>();
	public HistogrammFrame(Dispatcher dispatcher) {
		super (new HistogrammLayeredPanel(dispatcher));

		init_module(dispatcher);
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setTitle(I18N.getString(AnalysisResourceKeys.FRAME_HISTOGRAMM));
	}

	@Deprecated // seems unused
	public Dispatcher getInternalDispatcher() {
		return dispatcher;
	}

	void init_module(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		Heap.addBsHashListener(this);
		Heap.addAnalysisParametersListener(this);
	}

	/**
	 * If id is present, removes panel with id id.
	 * Then, if hp is not null, adds panel hp.
	 * @param id key
	 * @param hp new HistogramPanel or null
	 */
	private void setHistogramPanel(String id, HistogrammPanel hp) {
		final HistogrammPanel old = this.currentPanels.get(id);
		if (old != null) {
			((ScalableLayeredPanel)this.panel).removeGraphPanel(old);
			this.currentPanels.remove(id);
		}

		if (hp != null) {
			this.currentPanels.put(id, hp);
			((ScalableLayeredPanel)this.panel).addGraphPanel(hp);
		}
	}

	public void addTrace (String id) {
		if (id.equals(Heap.PRIMARY_TRACE_KEY) || id.equals(Heap.MODELED_TRACE_KEY))
		{
			HistogrammPanel p;

			PFTrace pf = Heap.getAnyPFTraceByKey(id);
			if (pf == null)
				return;

			double deltaX = pf.getResolution();
			double[] y = pf.getFilteredTraceClone();

			p = new HistogrammPanel(panel, y, deltaX);
			p.setColorModel(id);
			setHistogramPanel(id, p);
			panel.updScale2fit();

			setVisible(true);
		}
	}

	private void removeTrace(String id) {
		setHistogramPanel(id, null);
	}

	public void bsHashAdded(String key) {
			addTrace(key);
			setVisible(true);
	}

	public void bsHashRemoved(String key) {
		removeTrace(key);
		// Need not update visibility in this case,
		// only bsHashRemovedAll() requires we hide window;
		// This is a contract of Heap.
	}

	public void bsHashRemovedAll() {
		((ScalableLayeredPanel)panel).removeAllGraphPanels();
		setVisible (false);
	}

	public void analysisParametersUpdated() {
		JLayeredPane slp = ((ScalableLayeredPanel)panel).jLayeredPane;
		for(int i=0; i<slp.getComponentCount(); i++) {
			JPanel panel1 = (JPanel)slp.getComponent(i);
			if (panel1 instanceof HistogrammPanel) {
				((HistogrammPanel)panel1).updAnalysisParameters();
			}
		}
	}
}
