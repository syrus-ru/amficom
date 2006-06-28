/*
 * $Id: PathElementsLayeredPanel.java,v 1.14 2006/04/03 10:39:42 saa Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;


/**
 * @version $Revision: 1.14 $, $Date: 2006/04/03 10:39:42 $
 * @author $Author: saa $
 * @module analysis
 */

public class PathElementsLayeredPanel extends AnalysisLayeredPanel
{
	public static final long NO_MARKERS = 0x00001000;
	
	public PathElementsLayeredPanel(Dispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected ToolBarPanel createToolBar()
	{
		return new PathElementsToolBar(this);
	}

	@Override
	public void updPaintingMode()
	{
		super.updPaintingMode();
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if(panel instanceof PathElementsPanel)
			{
				((PathElementsPanel)panel).paint_path_elements = ((PathElementsToolBar)this.toolbar).pathElementsTButton.isSelected();
			}
		}
	}

	public void showPathElements(boolean b)
	{
		for (int i = 0; i < this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if(panel instanceof PathElementsPanel)
			{
				((PathElementsPanel)panel).setPaintPathElements(b);
				this.jLayeredPane.repaint();
				return;
			}
		}
	}
	
	@Override
	public void setAnalysisType (long type)
	{
		for(int i=0; i<this.jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (panel instanceof AnalysisPanel)
			{
				((AnalysisPanel)panel).show_markers = ((type & NO_MARKERS) == 0);
				showPathElements((type & NO_MARKERS) != 0);
			}
		}
		super.setAnalysisType(type);
	}
}

class PathElementsToolBar extends AnalysisToolBar
{
	protected static final String pe = "showpes";

	JToggleButton pathElementsTButton = new JToggleButton();

	private static String[] buttons = new String[] {
 		EX, DX, EY, DY, FIX, SEPARATOR, loss, ref, noana, pe, 
 		SEPARATOR, cA, cB,
 		SEPARATOR, trace, modeled, events, paleSecondary
 	};

	public PathElementsToolBar (PathElementsLayeredPanel panel) {
		super(panel);
	}

	@Override
	protected String[] getButtons() {
		return buttons;
	}

	@Override
	protected Map<String, AbstractButton> createGraphButtons()
	{
		Map<String, AbstractButton> buttons2 = super.createGraphButtons();

		buttons2.put(
				pe,
				createToolButton(
				this.pathElementsTButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelAnalyse.getString("show_pes"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_SHOW_PATH_ELEMENTS),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						pathElementsTButton_actionPerformed(e);
					}
				},
				true));
		
		ButtonGroup group = new ButtonGroup();
		group.add(super.lossTButton);
		group.add(super.reflectionTButton);
		group.add(super.noAnalysisTButton);
		group.add(this.pathElementsTButton);

		return buttons2;
	}

	void pathElementsTButton_actionPerformed(ActionEvent e)
	{
		PathElementsLayeredPanel panel1 = (PathElementsLayeredPanel)super.panel;
		panel1.setAnalysisType (PathElementsLayeredPanel.NO_MARKERS);
	}
}
