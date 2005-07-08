/*-
* $Id: TraceEventsToolBar.java,v 1.6 2005/07/08 10:08:37 saa Exp $
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
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @version $Revision: 1.6 $, $Date: 2005/07/08 10:08:37 $
 * @author $Author: saa $
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
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
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
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
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

	// FIXME: эта кнопочка выглядит как старая, отключающая расцветку событий, и называется тоже так. Но её действие новое - отключение отображение исходной (Bellcore) р/г
	void eventsTButton_actionPerformed(ActionEvent e)
	{
		((TraceEventsLayeredPanel)super.panel).updDrawGraphs();
	}

	void modeledTButton_actionPerformed(ActionEvent e)
	{
		((TraceEventsLayeredPanel)super.panel).updDrawModeled();
	}
}

