/*
 * $Id: PathElementsLayeredPanel.java,v 1.5 2005/03/28 14:08:26 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/28 14:08:26 $
 * @author $Author: bob $
 * @module analysis_v1
 */

public class PathElementsLayeredPanel extends AnalysisLayeredPanel
{
	public PathElementsLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);
	}

	protected ToolBarPanel createToolBar()
	{
		return new PathElementsToolBar(this);
	}

	public void updPaintingMode()
	{
		super.updPaintingMode();
		for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if(panel instanceof PathElementsPanel)
			{
				((PathElementsPanel)panel).paint_path_elements = ((PathElementsToolBar)toolbar).pathElementsTButton.isSelected();
			}
		}
	}

	public void showPathElements(boolean b)
	{
		for (int i = 0; i < jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if(panel instanceof PathElementsPanel)
			{
				((PathElementsPanel)panel).paint_path_elements = b;
				jLayeredPane.repaint();
				return;
			}
		}
	}
}

class PathElementsToolBar extends AnalysisToolBar
{
	protected static final String pe = "showpes";

	JToggleButton pathElementsTButton = new JToggleButton();

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX, SEPARATOR, loss, ref, noana, SEPARATOR, cA, cB, SEPARATOR, events, modeled, SEPARATOR, pe
	};

	public PathElementsToolBar (PathElementsLayeredPanel panel)
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
				pe,
				createToolButton(
				pathElementsTButton,
				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
				null,
				LangModelAnalyse.getString("lossanalyse"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_LOSS),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						pathElementsTButton_actionPerformed(e);
					}
				},
				true));

		return buttons;
	}

	void pathElementsTButton_actionPerformed(ActionEvent e)
	{
		PathElementsLayeredPanel panel = (PathElementsLayeredPanel)super.panel;
		panel.showPathElements(pathElementsTButton.isSelected());
	}
}