package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

public class ElementsTabbedPane extends UgoTabbedPane
{
	public ElementsTabbedPane(ApplicationContext aContext)
	{
		super(aContext);
	}

	protected void init_panel()
	{
		panel = new ElementsPanel(aContext);
		setLayout(new BorderLayout());
		add(panel);
	}

	public void openScheme(Scheme sch)
	{
	}

	public void openSchemeElement(SchemeElement se)
	{
		SchemeGraph graph = getPanel().getGraph();
		graph.setScheme(null);
		graph.setSchemeElement(se);
		Map clones = graph.copyFromArchivedState(se.schemeCellImpl().getData(), new java.awt.Point(0, 0));
		graph.selectionNotify();
	}

	public boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getSchemeElement() != null)
		{
			if (SchemeUtils.isSchemeContainsElement(sch, graph.getSchemeElement()))
			{
				GraphActions.clearGraph(graph);
				return true;
			}
		}
		return false;
	}

}