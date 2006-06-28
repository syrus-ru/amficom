package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * Используется из модуля "наблюдение".
 */
public class MapThresholdsLayeredPanel extends MapMarkersLayeredPanel
{
	public MapThresholdsLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected ToolBarPanel createToolBar()
	{
		return new MapThresholdsToolBar(this);
	}

	@Override
	public void addGraphPanel(SimpleGraphPanel panel)
	{
		super.addGraphPanel(panel);
		if (panel instanceof ThresholdsPanel)
			((ThresholdsPanel)panel).paint_thresholds =
			((MapThresholdsToolBar)this.toolbar).showThresholdsButton.isSelected();
	}

	public void setThresholdsPainted(boolean b)
	{
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof ThresholdsPanel)
			{
				((ThresholdsPanel)panel).paint_thresholds = b;
				repaint();
			}
		}
	}
	@Override
	public boolean modelShowDesired() {
		return super.modelShowDesired() && Heap.getPrimaryTrace().hasAnalysisLoaded();
	}
	@Override
	public boolean eventsShowDesired() {
		return super.eventsShowDesired() && Heap.getPrimaryTrace().hasAnalysisLoaded();
	}
}

class MapThresholdsToolBar extends MapMarkersToolBar
{
	protected static final String thresholds = "showThresholds";

	JToggleButton showThresholdsButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, trace, events, modeled, thresholds, SEPARATOR, createMarker, deleteMarker
	};

	public MapThresholdsToolBar(MapThresholdsLayeredPanel panel)
	{
		super(panel);
	}

	@Override
	protected String[] getButtons()
	{
		return buttons;
	}

	@Override
	protected Map createGraphButtons()
	{
		Map buttons2 = new HashMap();

		buttons2.put(
				thresholds,
				createToolButton(
				showThresholdsButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("allThresholds"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						showThresholdsButton_actionPerformed(e);
					}
				},
				true));

		this.showThresholdsButton.setSelected(true);
		buttons2.putAll(super.createGraphButtons());
		return buttons2;
	}

	void showThresholdsButton_actionPerformed(ActionEvent e)
	{
		MapThresholdsLayeredPanel panel1 = (MapThresholdsLayeredPanel)super.panel;
		panel1.setThresholdsPainted(this.showThresholdsButton.isSelected());
	}
}
