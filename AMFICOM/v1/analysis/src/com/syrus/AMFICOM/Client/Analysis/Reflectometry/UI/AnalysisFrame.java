package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.TraceResource;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.util.Log;

public class AnalysisFrame extends MultipleTracesFrame
implements BsHashChangeListener, EtalonMTMListener {

	protected AnalysisFrame(Dispatcher dispatcher, AnalysisLayeredPanel panel) {
		super (dispatcher, panel);

		init_module();
		try
		{
			jbInit();
		} catch(Exception e)
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
		setTitle(I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN));
		addComponentListener(new java.awt.event.ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent e)
			{
				this_componentShown();
			}
		});
	}

	@Override
	public String getReportTitle()
	{
		return I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
	}

	private void init_module() {
		Heap.addBsHashListener(this);
		Heap.addEtalonMTMListener(this);
	}

	protected void this_componentShown()
	{
		((AnalysisLayeredPanel)panel).updMarkers();
	}

	protected AnalysisPanel createSpecificAnalysisPanel(PFTrace pf) {
		double deltaX = pf.getResolution();
		double[] y = pf.getFilteredTraceClone();
		return new AnalysisPanel((AnalysisLayeredPanel)panel,
				dispatcher, y, deltaX);
	}

	protected void addTrace (String id)
	{
		removeOneTrace(id);

		SimpleGraphPanel p;
		PFTrace pf = Heap.getAnyPFTraceByKey(id);
		if (pf == null)
			return;

		AnalysisLayeredPanel ppp = (AnalysisLayeredPanel)panel;

		if (id.equals(Heap.PRIMARY_TRACE_KEY) || id.equals(Heap.MODELED_TRACE_KEY))
		{
			String title1;
			final String meId = pf.getBS().monitoredElementId;
			if (meId != null) {
				try {
					MonitoredElement me = StorableObjectPool.getStorableObject(Identifier.valueOf(meId), true);
					title1 = me.getName();
				} catch(ApplicationException ex) {
					Log.errorMessage(ex);
					title1 = I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
				} catch (RuntimeException ex) {
					title1 = I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
				}
			} else {
				title1 = I18N.getString(AnalysisResourceKeys.FRAME_ANALYSIS_MAIN);
			}
			setTitle(title1);

			p = createSpecificAnalysisPanel(pf);
			((AnalysisPanel)p).updEvents(id);
			((AnalysisPanel)p).updateNoiseLevel();
			((AnalysisPanel)p).draw_noise_level = true;
			((AnalysisPanel)p).eotDetectionLevel.setDrawed(true);
		} else {
			//p = new SimpleGraphPanel(y, deltaX);
			p = new ReflectogramPanel(ppp, id, true);
		}
		p.setColorModel(id);
		ppp.addGraphPanel(p);
		ppp.updPaintingMode();
		ppp.updScale2fit();
		traces.put(id, p);

		setVisible(true);
	}

	private void removeOneTrace(String id)
	{
		SimpleGraphPanel p = traces.get(id);
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
		traces = new HashMap<String, SimpleGraphPanel>();
	}

//	private void addEtalon()
//	{
//		removeEtalon();
//
//		SimpleGraphPanel p;
//
//		ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
//		p = new SimpleGraphPanel(mtae.getModelTrace().getYArray(), mtae.getDeltaX());
//		p.setColorModel(Heap.ETALON_TRACE_KEY);
//		((AnalysisLayeredPanel)panel).addGraphPanel(p);
//		((AnalysisLayeredPanel)panel).updPaintingMode();
//		panel.updScale2fit();
//
//		traces.put(Heap.ETALON_TRACE_KEY, p);
//
//		setVisible(true);
//	}
//
//	private void removeEtalon()
//	{
//		SimpleGraphPanel epPanel = (SimpleGraphPanel)traces.get(Heap.ETALON_TRACE_KEY);
//		if (epPanel != null)
//			panel.removeGraphPanel(epPanel);
//	}

	private void addEtalon() {
		addTrace(Heap.ETALON_TRACE_KEY);
	}
	private void removeEtalon() {
		removeOneTrace(Heap.ETALON_TRACE_KEY);
	}

	public void bsHashAdded(String key)
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
}
