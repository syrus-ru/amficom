/*-
* $Id: TraceEventsToolBar.java,v 1.12 2005/10/17 15:05:05 saa Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

/**
 * @version $Revision: 1.12 $, $Date: 2005/10/17 15:05:05 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module analysis
 */
public class TraceEventsToolBar extends ScalableToolBar
{
	// ������ � ��� ��������� ������ ������ �������� ��� �����������, � �����
	// ������ ��� ����������, ��� ������ ����������
	protected static final String trace = "traceButton";
	protected static final String events = "eventsButton";
	protected static final String modeled = "modeledButton";

	JToggleButton traceTButton = new JToggleButton();
	JToggleButton eventsTButton = new JToggleButton();
	JToggleButton modeledTButton = new JToggleButton();

	public TraceEventsToolBar (TraceEventsLayeredPanel panel)
	{
		super(panel);
	}

	private static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, trace, modeled, events
	};

	@Override
	protected String[] getButtons()
	{
		return buttons;
	}

	@Override
	protected Map<String, AbstractButton> createGraphButtons()
	{
		Map<String, AbstractButton> buttons1 = super.createGraphButtons();

		buttons1.put(
				trace,
				createToolButton(
				this.traceTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("showtrace"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_TRACE),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						traceTButton_actionPerformed(e);
					}
				},
				true));
		buttons1.put(
				events,
				createToolButton(
				this.eventsTButton,
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
		buttons1.put(
				modeled,
				createToolButton(
				this.modeledTButton,
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

		this.eventsTButton.doClick();
		return buttons1;
	}

	void traceTButton_actionPerformed(ActionEvent e)
	{
		((TraceEventsLayeredPanel)super.panel).updPaintingMode();
	}

	void eventsTButton_actionPerformed(ActionEvent e)
	{
		((TraceEventsLayeredPanel)super.panel).updPaintingMode();
	}

	void modeledTButton_actionPerformed(ActionEvent e)
	{
		((TraceEventsLayeredPanel)super.panel).updPaintingMode();
	}
}
