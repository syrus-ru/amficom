package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;

public class ThresholdsLayeredPanel extends TraceEventsLayeredPanel implements OperationListener
{
	public ThresholdsLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);

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

	private void jbInit() throws Exception
	{ // empty
	}

	protected ToolBarPanel createToolBar()
	{
		return new ThresholdsToolBar(this);
	}

	void init_module(Dispatcher dispatcher)
	{
		super.init_module(dispatcher);
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if (rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					for(int i=0; i<jLayeredPane.getComponentCount(); i++)
					{
						SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
						if (panel instanceof ThresholdsPanel)
							((ThresholdsPanel)panel).mtm = null;
					}
				}
			}
		}
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			for(int i=0; i<jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof ThresholdsPanel)
				{
					if(rue.analysisPerformed())
					{
						String id = (String)(rue.getSource());
						//if (id.equals("primarytrace"))
						{
							ModelTraceManager mtm = ((ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, id));
							((ThresholdsPanel)panel).updEvents(id);
							((ThresholdsPanel)panel).updateTrace(mtm);
							updScale2fitCurrentEv(.2, 1.);
							jLayeredPane.repaint();
						}
					}
					if(rue.thresholdsUpdated())
					{
						String id = (String)(rue.getSource());

						//ReflectogramEvent[] ep = ((ReflectogramEvent[])Pool.get("eventparams", id));
						//((ThresholdsPanel)panel).updateThresholds(ep);

						ModelTraceManager mtm = ((ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, id));
						((ThresholdsPanel)panel).updateThresholds(mtm);

						updScale2fitCurrentEv(.2, 1.);
						jLayeredPane.repaint();
					}
					if(rue.eventSelected())
					{
						int num = Integer.parseInt((String)rue.getSource());
						((ThresholdsPanel)panel).showEvent(num);
						updScale2fitCurrentEv (.2, 1.);
					}
					if(rue.thresholdChanged())
					{
						jLayeredPane.repaint();
					}
					if (rue.modelFunctionChanged())
					{
					    jLayeredPane.repaint();
					}
				}
			}
		}
		super.operationPerformed(ae);
	}

	public void updScale2fitCurrentEv (double indent_x, double iy)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
				if (((ThresholdsPanel)panel).c_event > ((ThresholdsPanel)panel).events.length)
					((ThresholdsPanel)panel).c_event = ((ThresholdsPanel)panel).events.length - 1;
				int start = ((ThresholdsPanel)panel).events[((ThresholdsPanel)panel).c_event].first_point;
				if (((ThresholdsPanel)panel).c_event == 0)
					start = 2;
				int end = ((ThresholdsPanel)panel).events[((ThresholdsPanel)panel).c_event].last_point;
				updScale2fit(start, end, indent_x, iy);
				jLayeredPane.repaint();
				return;
			}
		}
	}

	boolean hasShowThresholdButtonSelected()
	{
		return ((ThresholdsToolBar )toolbar).showThresholdButton.isSelected();
	}
}

class ThresholdsToolBar extends TraceEventsToolBar
{
	protected static final String FIT_EVENT = "fit2event";
	protected static final String ALL_TRESHOLDS = "allTresholds";

	private JButton fitEvButton = new JButton();
	protected JToggleButton showThresholdButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		FIT_EVENT, ALL_TRESHOLDS, separator, EX, DX, EY, DY, FIX, separator, modeled
	};

	public ThresholdsToolBar (ThresholdsLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = super.createGraphButtons();

		buttons.put(
				FIT_EVENT,
				createToolButton(
				fitEvButton,
				btn_size,
				null,
				LangModelAnalyse.getString("fittoevent"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_ZOOM_BOX),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fitEvButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				ALL_TRESHOLDS,
				createToolButton(
				showThresholdButton,
				btn_size,
				null,
				LangModelAnalyse.getString("allThresholds"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						showThresholdButton_actionPerformed(e);
					}
				},
				true));
		
		if (!modeledTButton.isSelected())
			modeledTButton.doClick();

		return buttons;
	}

	void fitEvButton_actionPerformed(ActionEvent e)
	{
		((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv (.2, 1.);
	}

	void showThresholdButton_actionPerformed(ActionEvent e)
	{
		/*for(int i=0; i<panel.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel sgp = (SimpleGraphPanel)(panel.jLayeredPane.getComponent(i));

			if (sgp instanceof ThresholdsPanel)
			{
				((ThresholdsPanel)sgp).paint_all_thresholds = showThresholdButton.isSelected();
			}
		}*/
		panel.jLayeredPane.repaint();
	}
}
