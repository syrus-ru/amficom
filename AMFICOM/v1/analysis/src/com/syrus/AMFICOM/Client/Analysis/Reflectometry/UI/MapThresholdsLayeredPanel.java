package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;

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
		ex, dx, ey, dy, fit, separator, events, modeled, thresholds, separator, createMarker, deleteMarker
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
				btn_size,
				null,
				"",
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/threshold.gif")),
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
