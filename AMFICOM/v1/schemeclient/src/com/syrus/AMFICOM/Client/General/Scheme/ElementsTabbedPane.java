package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class ElementsTabbedPane extends UgoTabbedPane
{
	public ElementsTabbedPane(ApplicationContext aContext)
	{
		super(aContext);
		panel = new ElementsPanel(aContext);

		removeAll();
		setLayout(new BorderLayout());
		add(panel);
	}


	protected void openScheme(Scheme sch)
	{
	}

	protected void openSchemeElement(SchemeElement se)
	{
		SchemeGraph graph = getPanel().getGraph();
		graph.setScheme(null);
		graph.setSchemeElement(se);
		se.unpack();
		Map clones = graph.copyFromArchivedState(se.serializable_cell, new java.awt.Point(0, 0));
		graph.setGraphChanged(false);
		graph.selectionNotify();
	}

	protected boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getSchemeElement() != null)
		{
			SchemeElement sc = sch.getSchemeElement(graph.getSchemeElement().getId());
			if (sc != null)
			{
				GraphActions.clearGraph(graph);
				return true;
			}
		}
		return false;
	}

}