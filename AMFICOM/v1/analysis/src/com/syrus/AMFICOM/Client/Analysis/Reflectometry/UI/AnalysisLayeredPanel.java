package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;

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
					if(rue.analysisPerformed())
					{
						String id = (String)(rue.getSource());
						if (id.equals(RefUpdateEvent.PRIMARY_TRACE))
						{
							((AnalysisPanel)panel).updEvents(id);

							ModelTraceManager mtm = Heap.getMTMByKey(id);
							((AnalysisPanel)panel).updateTrace(mtm); // FIXME: нужно UpdateMTM или UpdateTrace?
							((AnalysisPanel)panel).updMarkers();
							jLayeredPane.repaint();
						}
					}
					if(rue.eventSelected())
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
		EX, DX, EY, DY, FIX, SEPARATOR, loss, ref, noana, SEPARATOR, cA, cB, SEPARATOR, events, modeled
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
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("lossanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_LOSS),
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
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("reflectionanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_REFLECT),
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
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("noanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_NOANALYSIS),
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
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("centerA"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_A),
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
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("centerB"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_B),
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