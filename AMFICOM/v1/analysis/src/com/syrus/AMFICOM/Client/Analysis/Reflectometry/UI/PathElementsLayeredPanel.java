/*
 * $Id: PathElementsLayeredPanel.java,v 1.3 2005/03/23 10:47:33 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/23 10:47:33 $
 * @author $Author: bob $
 * @module analysis_v1
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

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
		EX, DX, EY, DY, FIX, separator, loss, ref, noana, separator, cA, cB, separator, events, modeled, separator, pe
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