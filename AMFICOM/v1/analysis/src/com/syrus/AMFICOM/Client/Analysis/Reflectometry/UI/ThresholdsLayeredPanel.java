package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Map;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;

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
	{
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
							((ThresholdsPanel)panel).et_ep = null;
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
							ReflectogramEvent[] ep = ((ReflectogramEvent[])Pool.get("eventparams", id));
							((ThresholdsPanel)panel).updEvents(id);
							((ThresholdsPanel)panel).updateEvents(ep);
							updScale2fitCurrentEv(.2, 1.);
							jLayeredPane.repaint();
						}
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
					if(rue.thresholdsUpdated())
					{
						String id = (String)(rue.getSource());
						ReflectogramEvent[] ep = ((ReflectogramEvent[])Pool.get("eventparams", id));
						((ThresholdsPanel)panel).updateThresholds(ep);
						updScale2fitCurrentEv(.2, 1.);
						jLayeredPane.repaint();
					}
				}
			}
		}
		super.operationPerformed(ae);
	}


	public void changeThreshold(double dx, double dl, double da, double dc, int key)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
				ThresholdsPanel tp = (ThresholdsPanel)panel;
				if (tp.et_ep != null)
				{
					ReflectogramEvent ep = tp.et_ep[tp.c_event];
					ep.getThreshold().changeThresholdBy(da, dc, dx, dl, ep, key);
					jLayeredPane.repaint();
					return;
				}
			}
		}
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
}

class ThresholdsToolBar extends TraceEventsToolBar
{
	protected static final String eA = "encreaseAmpl";
	protected static final String dA = "decreaseAmpl";
	protected static final String eW = "encreaseWidth";
	protected static final String dW = "decreaseWidth";
	protected static final String fitEv = "fit2event";
	protected static final String allTresh = "allTresholds";

	JButton fitEvButton = new JButton();
	JButton eAButton = new JButton();
	JButton dAButton = new JButton();
	JButton eXButton = new JButton();
	JButton dXButton = new JButton();
	JToggleButton showThresholdButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		eA, dA, eW, dW, separator, fitEv, allTresh, separator, ex, dx, ey, dy, fit, separator, modeled
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
				eW,
				createToolButton(
				eXButton,
				btn_size,
				null,
				LangModelAnalyse.getString("encreasetx"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/enlargetx.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eXButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				eA,
				createToolButton(
				eAButton,
				btn_size,
				null,
				LangModelAnalyse.getString("encreasety"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/enlargety.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eAButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				dW,
				createToolButton(
				dXButton,
				btn_size,
				null,
				LangModelAnalyse.getString("decreasetx"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reducetx.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dXButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				dA,
				createToolButton(
				dAButton,
				btn_size,
				null,
				LangModelAnalyse.getString("decreasety"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reducety.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dAButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				fitEv,
				createToolButton(
				fitEvButton,
				btn_size,
				null,
				LangModelAnalyse.getString("fittoevent"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/zoom_box.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fitEvButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				allTresh,
				createToolButton(
				showThresholdButton,
				btn_size,
				null,
				LangModelAnalyse.getString("allThresholds"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/threshold.gif")),
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

	void eAButton_actionPerformed(ActionEvent e)
	{
		ThresholdsLayeredPanel panel = (ThresholdsLayeredPanel)super.panel;
		panel.changeThreshold(0, 0, .1, 0, Threshold.UP1);
		panel.changeThreshold(0, 0, .2, 0, Threshold.UP2);
		panel.changeThreshold(0, 0, -.1, 0, Threshold.DOWN1);
		panel.changeThreshold(0, 0, -.2, 0, Threshold.DOWN2);
		panel.dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
	}

	void dAButton_actionPerformed(ActionEvent e)
	{
		ThresholdsLayeredPanel panel = (ThresholdsLayeredPanel)super.panel;
		panel.changeThreshold(0, 0, -.1, 0, Threshold.UP1);
		panel.changeThreshold(0, 0, -.2, 0, Threshold.UP2);
		panel.changeThreshold(0, 0, .1, 0, Threshold.DOWN1);
		panel.changeThreshold(0, 0, .2, 0, Threshold.DOWN2);
		panel.dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
	}

	void eXButton_actionPerformed(ActionEvent e)
	{
		ThresholdsLayeredPanel panel = (ThresholdsLayeredPanel)super.panel;
		panel.changeThreshold(1, 0, 0, 0, Threshold.UP1);
		panel.changeThreshold(2, 0, 0, 0, Threshold.UP2);
		panel.changeThreshold(-1, 0, 0, 0, Threshold.DOWN1);
		panel.changeThreshold(-2, 0, 0, 0, Threshold.DOWN2);
		panel.dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
	}

	void dXButton_actionPerformed(ActionEvent e)
	{
		ThresholdsLayeredPanel panel = (ThresholdsLayeredPanel)super.panel;
		panel.changeThreshold(-1, 0, 0, 0, Threshold.UP1);
		panel.changeThreshold(-2, 0, 0, 0, Threshold.UP2);
		panel.changeThreshold(1, 0, 0, 0, Threshold.DOWN1);
		panel.changeThreshold(2, 0, 0, 0, Threshold.DOWN2);
		panel.dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
	}

	void showThresholdButton_actionPerformed(ActionEvent e)
	{
		for(int i=0; i<panel.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel sgp = (SimpleGraphPanel)(panel.jLayeredPane.getComponent(i));

			if (sgp instanceof ThresholdsPanel)
			{
				((ThresholdsPanel)sgp).paint_all_thresholds = showThresholdButton.isSelected();
			}
		}
		panel.jLayeredPane.repaint();
	}
}
