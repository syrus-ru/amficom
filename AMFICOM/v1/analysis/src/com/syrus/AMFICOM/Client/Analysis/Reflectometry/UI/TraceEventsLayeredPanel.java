package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;

public class TraceEventsLayeredPanel extends ScalableLayeredPanel
implements PropertyChangeListener
{
	public TraceEventsLayeredPanel(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		dispatcher.addPropertyChangeListener(RefUpdateEvent.typ, this);
	}

	private void jbInit() throws Exception
	{ // empty
	}

	protected ToolBarPanel createToolBar()
	{
		return new TraceEventsToolBar(this);
	}

	public void propertyChange(PropertyChangeEvent ae)
	{
		if(ae.getPropertyName().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			for(int i=0; i<jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof ReflectogramPanel)
				{
					if(rue.minTraceLevelChanged())
					{
						jLayeredPane.repaint();
					}
				}
			}
		}
	}

	public void updDrawGraphs()
	{
		boolean b = graphsShowDesired();
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			panel.showGraph = b;
			jLayeredPane.repaint();
		}
	}

	public void updDrawModeled()
	{
		boolean b = modelShowDesired();
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ReflectogramPanel)
			{
				((ReflectogramPanel)panel).draw_modeled = b;
				jLayeredPane.repaint();
			}
		}
	}

	public boolean graphsShowDesired() {
		return ((TraceEventsToolBar)toolbar).eventsTButton.isSelected();
	}
	public boolean modelShowDesired() {
		return ((TraceEventsToolBar)toolbar).modeledTButton.isSelected();
	}
	public void updPaintingMode()
	{
		updDrawGraphs();
		updDrawModeled();
	}
}
