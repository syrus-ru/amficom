/*-
 * $Id: ThresholdsToolBar.java,v 1.3 2005/10/17 15:05:05 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

class ThresholdsToolBar extends TraceEventsToolBar
{
	protected static final String FIT_EVENT = "fit2event";
	protected static final String ALL_TRESHOLDS = "allTresholds";

	private JButton fitEvButton = new JButton();
	protected JToggleButton showThresholdButton = new JToggleButton();

	private static String[] buttons = new String[]
	{
		FIT_EVENT, ALL_TRESHOLDS, SEPARATOR, EX, DX, EY, DY, FIX,
		SEPARATOR, trace, modeled, events
	};

	public ThresholdsToolBar (ThresholdsLayeredPanel panel)
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
		Map buttons1 = super.createGraphButtons();

		buttons1.put(
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
		buttons1.put(
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

		return buttons1;
	}

	void fitEvButton_actionPerformed(ActionEvent e)
	{
		((ThresholdsLayeredPanel)panel).updScale2fitCurrentEv (.2, 1.);
	}

	void showThresholdButton_actionPerformed(ActionEvent e)
	{
		panel.jLayeredPane.repaint();
	}
}
