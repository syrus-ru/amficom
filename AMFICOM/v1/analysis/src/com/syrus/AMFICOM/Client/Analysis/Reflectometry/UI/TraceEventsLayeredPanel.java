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

	@Override
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

	public boolean eventsShowDesired() {
		return ((TraceEventsToolBar)toolbar).eventsTButton.isSelected();
	}
	public boolean graphsShowDesired() {
		return ((TraceEventsToolBar)toolbar).traceTButton.isSelected();
	}
	public boolean modelShowDesired() {
		return ((TraceEventsToolBar)toolbar).modeledTButton.isSelected();
	}
	public boolean paleSecondaryDesired() {
		return ((TraceEventsToolBar)toolbar).paleSecondaryTButton.isSelected();
	}

	public void updPaintingMode() {
		// just mark to repaint
		// all updates will be found via *Desired() methods
		jLayeredPane.repaint();
	}
}
