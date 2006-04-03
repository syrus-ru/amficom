/*-
 * $Id: AnalysisToolBar.java,v 1.7 2006/04/03 10:39:42 saa Exp $
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

class AnalysisToolBar extends TraceEventsToolBar
{
	protected static final String loss = "lossTButton";
	protected static final String ref = "reflectionTButton";
	protected static final String noana = "noAnalysisButton";
	protected static final String cA = "centerA";
	protected static final String cB = "centerB";

	JToggleButton lossTButton = new JToggleButton();
	JToggleButton reflectionTButton = new JToggleButton();
	JToggleButton noAnalysisTButton = new JToggleButton();
	JButton centerAButton = new JButton();
	JButton centerBButton = new JButton();

	private static String[] buttons = new String[] {
		EX, DX, EY, DY, FIX, SEPARATOR, loss, ref, noana,
		SEPARATOR, cA, cB,
		SEPARATOR, trace, modeled, events, paleSecondary
	};

	public AnalysisToolBar (AnalysisLayeredPanel panel)
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
		Map<String, AbstractButton> buttons2 = super.createGraphButtons();

		buttons2.put(
				loss,
				createToolButton(
				lossTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("lossanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_LOSS),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						lossTButton_actionPerformed(e);
					}
				},
				true));
		buttons2.put(
				ref,
				createToolButton(
				reflectionTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("reflectionanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_REFLECT),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						reflectionTButton_actionPerformed(e);
					}
				},
				true));
		buttons2.put(
				noana,
				createToolButton(
				noAnalysisTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("noanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_NOANALYSIS),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						noAnalysisTButton_actionPerformed(e);
					}
				},
				true));
		buttons2.put(
				cA,
				createToolButton(
				centerAButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("centerA"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_A),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						centerAButton_actionPerformed(e);
					}
				},
				true));
		buttons2.put(
				cB,
				createToolButton(
				centerBButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("centerB"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_B),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						centerBButton_actionPerformed(e);
					}
				},
				true));

		ButtonGroup group = new ButtonGroup();
		group.add(lossTButton);
		group.add(reflectionTButton);
		group.add(noAnalysisTButton);

		noAnalysisTButton.doClick();
		if (! traceTButton.isSelected())
			traceTButton.doClick();
		return buttons2;
	}

	void lossTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel1 = (AnalysisLayeredPanel)super.panel;
		panel1.setAnalysisType (AnalysisLayeredPanel.LOSS_ANALYSIS);
	}

	void reflectionTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel1 = (AnalysisLayeredPanel)super.panel;
		panel1.setAnalysisType (AnalysisLayeredPanel.REFLECTION_ANALYSIS);
	}

	void noAnalysisTButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel1 = (AnalysisLayeredPanel)super.panel;
		panel1.setAnalysisType (AnalysisLayeredPanel.NO_ANALYSIS);
	}

	void centerAButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel1 = (AnalysisLayeredPanel)super.panel;
		panel1.centerMarkerA();
	}

	void centerBButton_actionPerformed(ActionEvent e)
	{
		AnalysisLayeredPanel panel1 = (AnalysisLayeredPanel)super.panel;
		panel1.centerMarkerB();
	}
}
