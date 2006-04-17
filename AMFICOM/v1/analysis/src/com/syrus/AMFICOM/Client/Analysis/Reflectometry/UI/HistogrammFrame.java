package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

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
	public HistogrammFrame(Dispatcher dispatcher) {
		super (new HistogrammLayeredPanel(dispatcher));

		init_module();
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setTitle(I18N.getString(AnalysisResourceKeys.FRAME_HISTOGRAMM));
	}

	private void init_module() {
		Heap.addBsHashListener(this);
		Heap.addAnalysisParametersListener(this);
	}

	/**
	 * If id is present, removes panel with id id.
	 * Then, if hp is not null, adds panel hp.
	 * @param hp new HistogramPanel or null
	 */
	private void setHistogramPanel(HistogrammPanel hp) {
		this.panel.removeAllGraphPanels();
		((ScalableLayeredPanel)this.panel).addGraphPanel(hp);
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

			p = new HistogrammPanel(super.panel, y, deltaX);
			p.setColorModel(id);
			setHistogramPanel(p);
			super.panel.updScale2fit();

			setVisible(true);
		}
	}

	public void bsHashAdded(String key) {
			addTrace(key);
			setVisible(true);
	}

	public void bsHashRemoved(String key) {
		// nothing - as we remove only main trace - with bsHashRemovedAll() event 
	}

	public void bsHashRemovedAll() {
		super.panel.removeAllGraphPanels();
		setVisible (false);
	}

	public void analysisParametersUpdated() {
		JLayeredPane slp = super.panel.jLayeredPane;
		for(int i=0; i<slp.getComponentCount(); i++) {
			JPanel panel1 = (JPanel)slp.getComponent(i);
			if (panel1 instanceof HistogrammPanel) {
				((HistogrammPanel)panel1).updAnalysisParameters();
			}
		}
	}
}
