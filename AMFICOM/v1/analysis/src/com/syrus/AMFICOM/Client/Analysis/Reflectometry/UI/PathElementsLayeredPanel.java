package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Map;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

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
		ex, dx, ey, dy, fit, separator, loss, ref, noana, separator, cA, cB, separator, events, modeled, separator, pe
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
				btn_size,
				null,
				LangModelAnalyse.getString("lossanalyse"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/loss.gif")),
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