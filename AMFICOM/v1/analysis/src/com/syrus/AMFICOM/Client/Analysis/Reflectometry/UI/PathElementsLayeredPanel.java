/*
 * $Id: PathElementsLayeredPanel.java,v 1.9 2005/07/29 13:14:20 saa Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;


import java.util.Map;

import javax.swing.JToggleButton;

import com.syrus.AMFICOM.client.event.Dispatcher;


/**
 * @version $Revision: 1.9 $, $Date: 2005/07/29 13:14:20 $
 * @author $Author: saa $
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

	private static String[] buttons = new String[] {
 		EX, DX, EY, DY, FIX, SEPARATOR, loss, ref, noana,
 		SEPARATOR, cA, cB,
 		SEPARATOR, trace, modeled, events
		//, pe
 	};

	public PathElementsToolBar (PathElementsLayeredPanel panel) {
		super(panel);
	}

	@Override
	protected String[] getButtons() {
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = super.createGraphButtons();

//		buttons.put(
//				pe,
//				createToolButton(
//				pathElementsTButton,
//				UIManager.getDimension(ResourceKeys.SIZE_BUTTON),
//				null,
//				LangModelAnalyse.getString("lossanalyse"),
//				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_LOSS),
//				new ActionListener()
//				{
//					public void actionPerformed(ActionEvent e)
//					{
//						pathElementsTButton_actionPerformed(e);
//					}
//				},
//				true));
//
		return buttons;
	}

//	void pathElementsTButton_actionPerformed(ActionEvent e)
//	{
//		PathElementsLayeredPanel panel = (PathElementsLayeredPanel)super.panel;
//		panel.showPathElements(pathElementsTButton.isSelected());
//	}
}
