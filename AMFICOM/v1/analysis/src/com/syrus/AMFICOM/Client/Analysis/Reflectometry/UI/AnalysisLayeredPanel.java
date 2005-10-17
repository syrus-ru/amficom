package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.AnalysisParametersListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;

public class AnalysisLayeredPanel
extends TraceEventsLayeredPanel
implements CurrentEventChangeListener,
		PrimaryRefAnalysisListener, RefMismatchListener,
		AnalysisParametersListener
{
	public static final long LOSS_ANALYSIS = 0x00000001;
	public static final long REFLECTION_ANALYSIS = 0x00000010;
	public static final long NO_ANALYSIS = 0x00000100;

	private static Identifier refMismatchMarkerId;
	{
		try {
			refMismatchMarkerId = LocalIdentifierGenerator.generateIdentifier(ObjectEntities.MARK_CODE);
		} catch (IllegalObjectEntityException e) {
			throw new InternalError("generateIdentifier - IllegalObjectEntityException: " + e);
		}
	}

	public AnalysisLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);
		Heap.addCurrentEventChangeListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
		Heap.addRefMismatchListener(this);
		Heap.addAnalysisParametersListener(this);
	}

	@Override
	protected ToolBarPanel createToolBar()
	{
		return new AnalysisToolBar(this);
	}

	public void updMarkers()
	{
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).updMarkers();
				((AnalysisPanel)panel).updAnalysisMarkerInfo();
			}
		}
	}

	void centerMarkerA()
	{
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				AnalysisPanel p = (AnalysisPanel)panel;
				p.scrollToMarkerVisible(p.markerA);
			}
		}
	}

	void centerMarkerB()
	{
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				AnalysisPanel p = (AnalysisPanel)panel;
				p.scrollToMarkerVisible(p.markerB);
			}
		}
	}

	@Override
	public void updPaintingMode()
	{
		super.updPaintingMode();
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).loss_analysis = ((AnalysisToolBar)this.toolbar).lossTButton.isSelected();
				((AnalysisPanel)panel).reflection_analysis = ((AnalysisToolBar)this.toolbar).reflectionTButton.isSelected();
			}
		}
	}

	public void setAnalysisType (long type)
	{
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).loss_analysis = ((type & LOSS_ANALYSIS) != 0);
				((AnalysisPanel)panel).reflection_analysis = ((type & REFLECTION_ANALYSIS) != 0);
				((AnalysisPanel)panel).updMarkers();
				((AnalysisPanel)panel).updAnalysisMarkerInfo();
				this.jLayeredPane.repaint();
				return;
			}
		}
	}

	public void currentEventChanged()
	{
		int num = Heap.getCurrentEvent2();
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).move_marker_to_ev(num);
				((AnalysisPanel)panel).updAnalysisMarkerInfo();
			}
		}
	}

	public void primaryRefAnalysisCUpdated() {
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				{
					((AnalysisPanel)panel).updEvents(Heap.PRIMARY_TRACE_KEY);
					((AnalysisPanel)panel).updMarkers();
					this.jLayeredPane.repaint();
				}
			}
		}
	}

	public void primaryRefAnalysisRemoved() {
		// do nothing. AnalysisPanel will be removed when bsHashRemoved comes
	}

	private void updRefMismatch() {
		ReflectogramMismatchImpl alarm = Heap.getRefMismatch();
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof MapMarkersPanel)
			{
				// удаляем маркер (если он есть) и запоминаем, был ли он удален
				boolean updated =
						((MapMarkersPanel)panel).
								deleteMarker(refMismatchMarkerId) != null;
				// создаем новый маркер (если надо)
				if (alarm != null) {
					updated = true;
					double dist = alarm.getDistance(); 
					((MapMarkersPanel)panel).createAlarmMarker(
							LangModelAnalyse.getString("mismatch"),
							refMismatchMarkerId, dist);
				}
				if (updated)
					this.jLayeredPane.repaint();
			}
		}
	}
	public void refMismatchCUpdated() {
		updRefMismatch();
	}

	public void refMismatchRemoved() {
		updRefMismatch();
	}

	public void analysisParametersUpdated() {
		boolean updated = false;
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof EnhancedReflectogramPanel) {
				EnhancedReflectogramPanel p2 = (EnhancedReflectogramPanel)panel;
				updated = p2.eotDetectionLevel.isDrawed();
			}
		}
		if (updated) {
			this.jLayeredPane.repaint();
		}
	}
}
