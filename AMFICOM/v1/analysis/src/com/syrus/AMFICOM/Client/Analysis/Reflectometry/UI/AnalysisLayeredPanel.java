package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.JToggleButton;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class AnalysisLayeredPanel extends TraceEventsLayeredPanel implements OperationListener
{
	public static final long LOSS_ANALYSIS = 0x00000001;
	public static final long REFLECTION_ANALYSIS = 0x00000010;
	public static final long NO_ANALYSIS = 0x00000100;

	public AnalysisLayeredPanel(Dispatcher dispatcher)
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
	}

	private void jbInit() throws Exception
	{
	}

	protected ToolBarPanel createToolBar()
	{
		return new AnalysisToolBar(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			for(int i=0; i<jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof AnalysisPanel)
				{
					if(rue.ANALYSIS_PERFORMED)
					{
						String id = (String)(rue.getSource());
						if (id.equals("primarytrace"))
						{
							((AnalysisPanel)panel).updEvents(id);

							ReflectogramEvent[] ep = ((ReflectogramEvent[])Pool.get("eventparams", id));
							((AnalysisPanel)panel).updateEvents(ep);
							((AnalysisPanel)panel).updMarkers();
							jLayeredPane.repaint();
						}
					}
					if(rue.EVENT_SELECTED)
					{
						int num = Integer.parseInt((String)rue.getSource());
						((AnalysisPanel)panel).move_marker_to_ev(num);
						((AnalysisPanel)panel).updAnalysisMarkerInfo();
					}
				}
			}
		}
		super.operationPerformed(ae);
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
			};
		}
	}
}

class AnalysisToolBar extends TraceEventsToolBar
{
	protected static final String loss = "lossTButton";
	protected static final String ref = "reflectionTButton";
	protected static final String noana = "noAnalysisButton";

	JToggleButton lossTButton = new JToggleButton();
	JToggleButton reflectionTButton = new JToggleButton();
	JToggleButton noAnalysisTButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		ex, dx, ey, dy, fit, separator, loss, ref, noana, separator, events, modeled
	};

	public AnalysisToolBar (AnalysisLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Hashtable createGraphButtons()
	{
		Hashtable buttons = new Hashtable();

		buttons.put(
				loss,
				createToolButton(
				lossTButton,
				btn_size,
				null,
				LangModelAnalyse.String("lossanalyse"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/loss.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						lossTButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				ref,
				createToolButton(
				reflectionTButton,
				btn_size,
				null,
				LangModelAnalyse.String("reflectionanalyse"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reflect.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						reflectionTButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				noana,
				createToolButton(
				noAnalysisTButton,
				btn_size,
				null,
				LangModelAnalyse.String("noanalyse"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/noanalyse.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						noAnalysisTButton_actionPerformed(e);
					}
				},
				true));

		ButtonGroup group = new ButtonGroup();
		for (Enumeration enum = buttons.elements(); enum.hasMoreElements();)
		{
			AbstractButton button = (AbstractButton)enum.nextElement();
			group.add(button);
		}

		lossTButton.doClick();
		buttons.putAll(super.createGraphButtons());
		return buttons;
	}

	void lossTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		if (!lossTButton.isSelected())
		{
			lossTButton.setSelected(true);
			return;
		}
		panel.setAnalysisType (AnalysisLayeredPanel.LOSS_ANALYSIS);
		reflectionTButton.setSelected(false);
		noAnalysisTButton.setSelected(false);
	}

	void reflectionTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		if(!reflectionTButton.isSelected())
		{
			reflectionTButton.setSelected(true);
			return;
		}
		panel.setAnalysisType (AnalysisLayeredPanel.REFLECTION_ANALYSIS);
		lossTButton.setSelected(false);
		noAnalysisTButton.setSelected(false);
	}

	void noAnalysisTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		if (!noAnalysisTButton.isSelected())
		{
			noAnalysisTButton.setSelected(true);
			return;
		}
		panel.setAnalysisType (AnalysisLayeredPanel.NO_ANALYSIS);
		lossTButton.setSelected(false);
		reflectionTButton.setSelected(false);
	}
}