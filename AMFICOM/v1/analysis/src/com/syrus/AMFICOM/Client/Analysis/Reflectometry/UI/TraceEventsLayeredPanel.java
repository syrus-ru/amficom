package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Map;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

public class TraceEventsLayeredPanel extends ScalableLayeredPanel implements OperationListener
{
	Dispatcher dispatcher;

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
		this.dispatcher = dispatcher;
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

class TraceEventsToolBar extends ScalableToolBar
{
	protected static final String events = "eventsButton";
	protected static final String modeled = "modeledButton";

	JToggleButton eventsTButton = new JToggleButton();
	JToggleButton modeledTButton = new JToggleButton();

	public TraceEventsToolBar (TraceEventsLayeredPanel panel)
	{
		super(panel);
	}

	protected static String[] buttons = new String[]
	{
		ex, dx, ey, dy, fit, separator, events, modeled
	};

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = super.createGraphButtons();

		buttons.put(
				events,
				createToolButton(
				eventsTButton,
				btn_size,
				null,
				LangModelAnalyse.getString("showevents"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/events.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eventsTButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				modeled,
				createToolButton(
				modeledTButton,
				btn_size,
				null,
				LangModelAnalyse.getString("showmodel"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/modeled.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						modeledTButton_actionPerformed(e);
					}
				},
				true));

		eventsTButton.doClick();
		return buttons;
	}

	void eventsTButton_actionPerformed(ActionEvent e)
	{
		TraceEventsLayeredPanel panel = (TraceEventsLayeredPanel)super.panel;
		boolean b = eventsTButton.isSelected();
		panel.drawEvents (b);
	}

	void modeledTButton_actionPerformed(ActionEvent e)
	{
		TraceEventsLayeredPanel panel = (TraceEventsLayeredPanel)super.panel;
		boolean b = modeledTButton.isSelected();
		panel.drawModeled (b);
	}
}