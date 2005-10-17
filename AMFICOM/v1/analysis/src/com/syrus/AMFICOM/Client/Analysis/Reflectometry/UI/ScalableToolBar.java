/*-
* $Id: ScalableToolBar.java,v 1.8 2005/10/17 15:05:05 saa Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;


/**
 * @version $Revision: 1.8 $, $Date: 2005/10/17 15:05:05 $
 * @author $Author: saa $
 * @author Vladimir Dolzhenko
 * @module analysis
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

	private static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX
	};

	public ScalableToolBar(ScalableLayeredPanel panel)
	{
		super(panel);
	}

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
				EX,
				createToolButton(
				this.exButton,
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
		buttons1.put(
				EY,
				createToolButton(
				this.eyButton,
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
		buttons1.put(
				DX,
				createToolButton(
				this.dxButton,
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
		buttons1.put(
				DY,
				createToolButton(
				this.dyButton,
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
		buttons1.put(
				FIX,
				createToolButton(
				this.fitButton,
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

		return buttons1;
	}

	void dxButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel1 = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel1.horizontalMax * k < ScalableLayeredPanel.hwidth)
			k = ScalableLayeredPanel.hwidth / panel1.horizontalMax;
		if (panel1.horizontalBar.getMaximum() > ScalableLayeredPanel.hwidth)
			panel1.updScale(k, 1, .5, .5);
	}

	void exButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel1 = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel1.horizontalBar.getMaximum() < ScalableLayeredPanel.hwidth * 1000)
			panel1.updScale(k, 1, .5, .5);
	}

	void dyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel1 = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel1.verticalMax * k < ScalableLayeredPanel.vheight)
			k = ScalableLayeredPanel.vheight / panel1.verticalMax;
		if (panel1.verticalBar.getMaximum() > ScalableLayeredPanel.vheight)
			panel1.updScale(1, k, .5, .5);
	}

	void eyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel1 = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel1.verticalBar.getMaximum() < ScalableLayeredPanel.vheight * 150)
			panel1.updScale(1, k, .5, .5);
	}

	void fitButton_actionPerformed(ActionEvent e)
	{
		this.panel.updScale2fit();
	}
}

