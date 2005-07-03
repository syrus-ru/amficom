/*-
* $Id: ScalableToolBar.java,v 1.4 2005/05/25 15:15:08 stas Exp $
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
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;


/**
 * @version $Revision: 1.4 $, $Date: 2005/05/25 15:15:08 $
 * @author $Author: stas $
 * @author Vladimir Dolzhenko
 * @module analysis_v1
 */
public class ScalableToolBar extends ToolBarPanel
{
	protected static final String EX = "exButton";
	protected static final String EY = "eyButton";
	protected static final String DX = "dxButton";
	protected static final String DY = "dyButton";
	protected static final String FIX = "fitButton";

	private JButton exButton = new JButton();
	private JButton eyButton = new JButton();
	private JButton dxButton = new JButton();
	private JButton dyButton = new JButton();
	private JButton fitButton = new JButton();

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX
	};

	public ScalableToolBar(ScalableLayeredPanel panel)
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
				EX,
				createToolButton(
				exButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("encreasex"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_X),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						exButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				EY,
				createToolButton(
				eyButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("encreasey"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_Y),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eyButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				DX,
				createToolButton(
				dxButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("decreasex"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_X),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dxButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				DY,
				createToolButton(
				dyButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("decreasey"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_Y),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dyButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				FIX,
				createToolButton(
				fitButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("fittoscreen"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_FIT),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fitButton_actionPerformed(e);
					}
				},
				true));

		return buttons;
	}

	void dxButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel.horizontalMax * k < ScalableLayeredPanel.hwidth)
			k = ScalableLayeredPanel.hwidth / panel.horizontalMax;
		if (panel.horizontalBar.getMaximum() > ScalableLayeredPanel.hwidth)
			panel.updScale(k, 1, .5, .5);
	}

	void exButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel.horizontalBar.getMaximum() < ScalableLayeredPanel.hwidth * 1000)
			panel.updScale(k, 1, .5, .5);
	}

	void dyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel.verticalMax * k < ScalableLayeredPanel.vheight)
			k = ScalableLayeredPanel.vheight / panel.verticalMax;
		if (panel.verticalBar.getMaximum() > ScalableLayeredPanel.vheight)
			panel.updScale(1, k, .5, .5);
	}

	void eyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel.verticalBar.getMaximum() < ScalableLayeredPanel.vheight * 150)
			panel.updScale(1, k, .5, .5);
	}

	void fitButton_actionPerformed(ActionEvent e)
	{
		panel.updScale2fit();
	}
}

