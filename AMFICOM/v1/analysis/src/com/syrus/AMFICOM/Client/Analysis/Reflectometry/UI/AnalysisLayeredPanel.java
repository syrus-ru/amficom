package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;

public class AnalysisLayeredPanel
extends TraceEventsLayeredPanel
implements CurrentEventChangeListener,
		PrimaryRefAnalysisListener, RefMismatchListener
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
	}

	protected ToolBarPanel createToolBar()
	{
		return new AnalysisToolBar(this);
	}

	public void updMarkers()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).updMarkers();
				((AnalysisPanel)panel).updAnalysisMarkerInfo();
			}
		}
	}

	void centerMarkerA()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				AnalysisPanel p = (AnalysisPanel)panel;
				p.scrollToMarkerVisible(p.markerA);
			}
		}
	}

	void centerMarkerB()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				AnalysisPanel p = (AnalysisPanel)panel;
				p.scrollToMarkerVisible(p.markerB);
			}
		}
	}

	public void updPaintingMode()
	{
		super.updPaintingMode();
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).loss_analysis = ((AnalysisToolBar)toolbar).lossTButton.isSelected();
				((AnalysisPanel)panel).reflection_analysis = ((AnalysisToolBar)toolbar).reflectionTButton.isSelected();
			}
		}
	}

	public void setAnalysisType (long type)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).loss_analysis = ((type & LOSS_ANALYSIS) != 0);
				((AnalysisPanel)panel).reflection_analysis = ((type & REFLECTION_ANALYSIS) != 0);
				((AnalysisPanel)panel).updMarkers();
				((AnalysisPanel)panel).updAnalysisMarkerInfo();
				jLayeredPane.repaint();
				return;
			}
		}
	}

	public void currentEventChanged()
	{
		int num = Heap.getCurrentEvent2();
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).move_marker_to_ev(num);
				((AnalysisPanel)panel).updAnalysisMarkerInfo();
			}
		}
	}

	public void primaryRefAnalysisCUpdated() {
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				{
					((AnalysisPanel)panel).updEvents(Heap.PRIMARY_TRACE_KEY);
					((AnalysisPanel)panel).updMarkers();
					jLayeredPane.repaint();
				}
			}
		}
	}

	public void primaryRefAnalysisRemoved() {
		// do nothing. AnalysisPanel will be removed when bsHashRemoved comes
	}

	private void updRefMismatch() {
		ReflectogramMismatch alarm = Heap.getRefMismatch();
		for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
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
					jLayeredPane.repaint();
			}
		}
	}
	public void refMismatchCUpdated() {
		updRefMismatch();
	}

	public void refMismatchRemoved() {
		updRefMismatch();
	}
}
