package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.Map;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;

public class AnalysisLayeredPanel extends TraceEventsLayeredPanel implements OperationListener
{
	public static final long LOSS_ANALYSIS = 0x00000001;
	public static final long REFLECTION_ANALYSIS = 0x00000010;
	public static final long NO_ANALYSIS = 0x00000100;

	public AnalysisLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);
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
}

class AnalysisToolBar extends TraceEventsToolBar
{
	protected static final String loss = "lossTButton";
	protected static final String ref = "reflectionTButton";
	protected static final String noana = "noAnalysisButton";
	protected static final String cA = "centerA";
	protected static final String cB = "centerB";

	JToggleButton lossTButton = new JToggleButton();
	JToggleButton reflectionTButton = new JToggleButton();
	JToggleButton noAnalysisTButton = new JToggleButton();
	JButton centerAButton = new JButton();
	JButton centerBButton = new JButton();

	protected static String[] buttons = new String[]
	{
		ex, dx, ey, dy, fit, separator, loss, ref, noana, separator, cA, cB, separator, events, modeled
	};

	public AnalysisToolBar (AnalysisLayeredPanel panel)
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
				loss,
				createToolButton(
				lossTButton,
				btn_size,
				null,
				LangModelAnalyse.getString("lossanalyse"),
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
				LangModelAnalyse.getString("reflectionanalyse"),
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
				LangModelAnalyse.getString("noanalyse"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/noanalyse.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						noAnalysisTButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				cA,
				createToolButton(
				centerAButton,
				btn_size,
				null,
				LangModelAnalyse.getString("centerA"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/centera.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						centerAButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				cB,
				createToolButton(
				centerBButton,
				btn_size,
				null,
				LangModelAnalyse.getString("centerB"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/centerb.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						centerBButton_actionPerformed(e);
					}
				},
				true));

		ButtonGroup group = new ButtonGroup();
		group.add(lossTButton);
		group.add(reflectionTButton);
		group.add(noAnalysisTButton);

		noAnalysisTButton.doClick();
		return buttons;
	}

	void lossTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		panel.setAnalysisType (AnalysisLayeredPanel.LOSS_ANALYSIS);
	}

	void reflectionTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		panel.setAnalysisType (AnalysisLayeredPanel.REFLECTION_ANALYSIS);
	}

	void noAnalysisTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		panel.setAnalysisType (AnalysisLayeredPanel.NO_ANALYSIS);
	}

	void centerAButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		panel.centerMarkerA();
	}

	void centerBButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel = (AnalysisLayeredPanel)super.panel;
		panel.centerMarkerB();
	}
}