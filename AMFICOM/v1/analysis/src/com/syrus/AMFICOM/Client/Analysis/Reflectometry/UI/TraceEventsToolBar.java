/*-
* $Id: TraceEventsToolBar.java,v 1.1 2005/03/28 14:07:41 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/28 14:07:41 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module analysis_v1
 */
public class TraceEventsToolBar extends ScalableToolBar
{
	protected static final String events = "eventsButton";
	protected static final String modeled = "modeledButton";

	JToggleButton eventsTButton = new JToggleButton();
	JToggleButton modeledTButton = new JToggleButton();

	public TraceEventsToolBar (TraceEventsLayeredPanel panel)
	{
		super(panel);
	}

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, events, modeled
	};

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = super.createGraphButtons();

		buttons.put(
				events,
				createToolButton(
				eventsTButton,
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("showevents"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_EVENTS),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eventsTButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				modeled,
				createToolButton(
				modeledTButton,
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("showmodel"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_MODELED),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						modeledTButton_actionPerformed(e);
					}
				},
				true));

		eventsTButton.doClick();
		return buttons;
	}

	void eventsTButton_actionPerformed(ActionEvent e)
	{
		TraceEventsLayeredPanel panel = (TraceEventsLayeredPanel)super.panel;
		boolean b = eventsTButton.isSelected();
		panel.drawEvents (b);
	}

	void modeledTButton_actionPerformed(ActionEvent e)
	{
		TraceEventsLayeredPanel panel = (TraceEventsLayeredPanel)super.panel;
		boolean b = modeledTButton.isSelected();
		panel.drawModeled (b);
	}
}

