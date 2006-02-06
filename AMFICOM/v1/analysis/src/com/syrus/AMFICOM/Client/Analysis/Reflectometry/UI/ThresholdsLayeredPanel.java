package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;

public class ThresholdsLayeredPanel extends TraceEventsLayeredPanel
implements PropertyChangeListener,
	CurrentEventChangeListener, EtalonMTMListener, PrimaryRefAnalysisListener
{
	public ThresholdsLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);

		try
		{
			jbInit();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
		init_module(dispatcher);
	}

	private void jbInit() throws Exception
	{ // empty
	}

	@Override
	protected ToolBarPanel createToolBar()
	{
		return new ThresholdsToolBar(this);
	}

	@Override void init_module(Dispatcher dispatcher)
	{
		super.init_module(dispatcher);
		// на RefUpdateEvent подписывается суперкласс - нам подписываться не надо
		Heap.addCurrentEventChangeListener(this);
		Heap.addEtalonMTMListener(this);
		Heap.addPrimaryRefAnalysisListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent ae)
	{
		if(ae.getPropertyName().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			if(rue.thresholdChanged()) {
				for(int i=0; i<jLayeredPane.getComponentCount(); i++)
				{
					SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
					if (panel instanceof ThresholdsPanel)
					{
						jLayeredPane.repaint();
					}
				}
			}
			
		}
		super.propertyChange(ae);
	}

	public void updScale2fitCurrentEv (double indent_x, double iy)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
				if (((ThresholdsPanel)panel).sevents.length > 0)
				{
					int[] startAndEnd = ((ThresholdsPanel)panel).getStartAndEndOfCurrentEvent();
					int start = startAndEnd[0];
					int end = startAndEnd[1];
					updScale2fit(start, end, indent_x, iy);
					jLayeredPane.repaint();
				}
				return;
			}
		}
	}
	
	@Override
	protected void updScale2fit(int start, int end, double indent_x, double iy)
	{
		SimpleGraphPanel activePanel = (SimpleGraphPanel)jLayeredPane.getComponent(0);
		if (activePanel instanceof ThresholdsPanel) {
			ThresholdsPanel p = (ThresholdsPanel)activePanel;
			SimpleGraphPanel.GraphRange range = new SimpleGraphPanel.GraphRange();
			p.updateGraphRangeByThresholds(range);
			if (!range.isEmpty()) {
				int ix = (int)((range.getXMax() - range.getXMin()) * indent_x);

				double _scale_x = activePanel.scaleX;
				double _scale_y = activePanel.scaleY;
				double sc_x = ((double)(jLayeredPane.getWidth()) / (double)(range.getXMax() - range.getXMin() + 2*ix));
				double sc_y = (jLayeredPane.getHeight() / (range.getYMax() - range.getYMin() + 2*iy));

				updScale (sc_x/_scale_x, sc_y/_scale_y, 0.5, 0.5);
			
				horizontalBar.setMinimum(-5);
				horizontalBar.setValue((int)(horizontalMax * ((range.getXMin() - ix)  * activePanel.deltaX) / (maxLength*maxDeltaX/scale_x)));
				verticalBar.setMinimum(-5);
				verticalBar.setValue((int)(verticalMax * ((activePanel.maxY - range.getYMax() - iy) / (maxY/scale_y))));
				return;
			}
		}
		super.updScale2fit(start, end, indent_x, iy);
	}

	boolean hasShowThresholdButtonSelected()
	{
		return ((ThresholdsToolBar )toolbar).showThresholdButton.isSelected();
	}

	public void currentEventChanged()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
				((ThresholdsPanel)panel).updateCurrentEvent();
		}
		updScale2fitCurrentEv (.2, 1.);
	}

	private void etalonUpdated()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
					((ThresholdsPanel)panel).updateEtalon();

					updScale2fitCurrentEv(.2, 1.);
					jLayeredPane.repaint();
			}
		}
	}
	public void etalonMTMCUpdated()
	{
		etalonUpdated();
	}

	public void etalonMTMRemoved()
	{
		etalonUpdated();
	}

	public void primaryRefAnalysisCUpdated() {
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel) {
				((ThresholdsPanel)panel).updEvents(Heap.PRIMARY_TRACE_KEY);
				this.jLayeredPane.repaint();
			}
		}
	}

	public void primaryRefAnalysisRemoved() {
		// do nothing. Panel will be removed when bsHashRemoved comes
	}
}
