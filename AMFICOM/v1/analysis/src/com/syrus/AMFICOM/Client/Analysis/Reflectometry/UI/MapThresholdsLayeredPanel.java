package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

public class MapThresholdsLayeredPanel extends MapMarkersLayeredPanel
{
	public MapThresholdsLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);
	}

	protected ToolBarPanel createToolBar()
	{
		return new MapThresholdsToolBar(this);
	}

	public void addGraphPanel(SimpleGraphPanel panel)
	{
		super.addGraphPanel(panel);
		if (panel instanceof MapMarkersPanel)
			((MapMarkersPanel)panel).paint_thresholds =
			((MapThresholdsToolBar)toolbar).showThresholdsButton.isSelected();
	}

	public void setThresholdsPainted(boolean b)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
				((ThresholdsPanel)panel).paint_thresholds = b;
				repaint();
			}
		}
	}
}

class MapThresholdsToolBar extends MapMarkersToolBar
{
	protected static final String thresholds = "showThresholds";

	JToggleButton showThresholdsButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, events, modeled, thresholds, SEPARATOR, createMarker, deleteMarker
	};

	public MapThresholdsToolBar(MapThresholdsLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = new HashMap();

		buttons.put(
				thresholds,
				createToolButton(
				showThresholdsButton,
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				"",
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						showThresholdsButton_actionPerformed(e);
					}
				},
				true));

		showThresholdsButton.setSelected(true);
		buttons.putAll(super.createGraphButtons());
		return buttons;
	}

	void showThresholdsButton_actionPerformed(ActionEvent e)
	{
		MapThresholdsLayeredPanel panel = (MapThresholdsLayeredPanel)super.panel;
		panel.setThresholdsPainted(showThresholdsButton.isSelected());
	}
}
