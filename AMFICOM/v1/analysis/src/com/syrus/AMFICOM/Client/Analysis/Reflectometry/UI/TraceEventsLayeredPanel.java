package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.beans.*;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.Client.Analysis.Heap;
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
				if (panel instanceof ReflectogramEventsPanel)
				{
					if(rue.minTraceLevelChanged())
					{
						((ReflectogramEventsPanel)panel).updateMinTraceLevel(Heap.getMinTraceLevel());
						jLayeredPane.repaint();
					}
				}
			}
		}
	}

	public void drawEvents (boolean b)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof TraceEventsPanel)
			{
				((TraceEventsPanel)panel).draw_events = b;
				jLayeredPane.repaint();
			}
		}
	}

	public void drawModeled (boolean b)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ReflectogramEventsPanel)
			{
				((ReflectogramEventsPanel)panel).draw_modeled = b;
				jLayeredPane.repaint();
			}
		}
	}

	public void updEvents(String id)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof TraceEventsPanel)
			{
				((TraceEventsPanel)panel).updEvents(id);
				jLayeredPane.repaint();
				return;
			}
		}
	}

	public void updPaintingMode()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof TraceEventsPanel)
				((TraceEventsPanel)panel).draw_events = ((TraceEventsToolBar)toolbar).eventsTButton.isSelected();
			if (panel instanceof ReflectogramEventsPanel)
				((ReflectogramEventsPanel)panel).draw_modeled = ((TraceEventsToolBar)toolbar).modeledTButton.isSelected();
		}
	}
}
