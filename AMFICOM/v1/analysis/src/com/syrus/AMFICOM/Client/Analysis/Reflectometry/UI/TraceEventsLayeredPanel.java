package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;

public class TraceEventsLayeredPanel extends ScalableLayeredPanel implements OperationListener
{
	public TraceEventsLayeredPanel(Dispatcher dispatcher)
	{
		super();

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	private void jbInit() throws Exception
	{ // empty
	}

	protected ToolBarPanel createToolBar()
	{
		return new TraceEventsToolBar(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			for(int i=0; i<jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof ReflectogramEventsPanel)
				{
					if(rue.minTraceLevelChanged())
					{
						((ReflectogramEventsPanel)panel).updateMinTraceLevel((Double)rue.getSource());
						jLayeredPane.repaint();
					}
				}
			}
			if (rue.modelFunctionChanged())
			{
			    jLayeredPane.repaint();
			}
			if (rue.analysisPerformed())
			{
			    updPaintingMode();
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
