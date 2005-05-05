package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTAEListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;

public class ThresholdsLayeredPanel extends TraceEventsLayeredPanel
implements OperationListener,
    CurrentEventChangeListener, EtalonMTMListener, PrimaryMTAEListener
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
		// на RefUpdateEvent подписывается суперкласс - нам подписываться не надо
		Heap.addCurrentEventChangeListener(this);
		Heap.addEtalonMTMListener(this);
        Heap.addPrimaryMTMListener(this);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent rue = (RefUpdateEvent)ae;

			for(int i=0; i<jLayeredPane.getComponentCount(); i++)
			{
				SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
				if (panel instanceof ThresholdsPanel)
				{
					if(rue.thresholdChanged())
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
                if (((ThresholdsPanel)panel).sevents.length > 0)
                {
                	int[] startAndEnd = ((ThresholdsPanel)panel).getStartAndEndOfCurrentEvent();
    				int start = startAndEnd[0];
    				int end = startAndEnd[1];
    				updScale2fit(start, end, indent_x, iy);
    				jLayeredPane.repaint();
                }
   				return;
			}
		}
	}

	boolean hasShowThresholdButtonSelected()
	{
		return ((ThresholdsToolBar )toolbar).showThresholdButton.isSelected();
	}

	public void currentEventChanged()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
				((ThresholdsPanel)panel).updateCurrentEvent();
		}
		updScale2fitCurrentEv (.2, 1.);
	}

	private void etalonUpdated()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
					((ThresholdsPanel)panel).updateEtalon();

					updScale2fitCurrentEv(.2, 1.);
					jLayeredPane.repaint();
			}
		}
	}
	public void etalonMTMCUpdated()
	{
		etalonUpdated();
	}

	public void etalonMTMRemoved()
	{
		etalonUpdated();
	}

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.Client.General.Event.PrimaryMTAEListener#primaryMTMCUpdated()
     */
    public void primaryMTMCUpdated() {
        for(int i=0; i<jLayeredPane.getComponentCount(); i++)
        {
            SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
            if (panel instanceof ThresholdsPanel)
            {
                ModelTraceAndEvents mtae = Heap.getMTAEPrimary();
                ((ThresholdsPanel)panel).updEvents(Heap.PRIMARY_TRACE_KEY);
                ((ThresholdsPanel)panel).updateTrace(mtae);
                updScale2fitCurrentEv(.2, 1.);
                jLayeredPane.repaint();
            }
        }
    }

    public void primaryMTMRemoved() {
        for(int i=0; i<jLayeredPane.getComponentCount(); i++)
        {
            SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
            if (panel instanceof ThresholdsPanel)
                ((ThresholdsPanel)panel).mtae = null;
        }
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
		FIT_EVENT, ALL_TRESHOLDS, SEPARATOR, EX, DX, EY, DY, FIX, SEPARATOR, modeled
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
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
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
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
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
