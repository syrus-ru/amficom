package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.Map;

import java.awt.*;
import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class UgoTabbedPane extends JPanel implements OperationListener
{
	ApplicationContext aContext;
	protected UgoPanel panel;

	boolean ignore_loading = false;
	boolean insert_ugo = true;

	public UgoTabbedPane(ApplicationContext aContext)
	{
		this.aContext = aContext;
		panel = new UgoPanel(aContext);

		setLayout(new BorderLayout());
		add(panel);

		init_module();
	}

	public void init_module()
	{
		aContext.getDispatcher().register(this, SchemeElementsEvent.type);
	}

	public UgoPanel getPanel()
	{
		return panel;
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeGraph graph = getPanel().getGraph();
			SchemeElementsEvent see = (SchemeElementsEvent) ae;
			if (see.OPEN_PRIMARY_SCHEME)
			{
				if (!ignore_loading)
				{
					Scheme scheme = (Scheme) see.obj;
//					GraphActions.clearGraph(graph);
					openScheme(scheme);
				}
			}
			if (see.OPEN_ELEMENT)
			{
				if (!ignore_loading)
				{
					graph.skip_notify = true;
					Object obj = see.obj;
					if (obj instanceof ProtoElement)
					{
						ProtoElement proto = (ProtoElement) obj;
						getPanel().setProtoCell(proto, null);
					}
					graph.skip_notify = false;
				}
			}
			if (see.OPEN_SCHEME)
			{
				if (!ignore_loading)
				{
					graph.skip_notify = true;
					Object obj = see.obj;
					if (obj instanceof Scheme)
					{
						Scheme scheme = (Scheme) obj;
						getPanel().setSchemeCell(scheme);
					}
					graph.skip_notify = false;
				}
			}
			if (see.CLOSE_SCHEME)
			{
				graph.skip_notify = true;
				Object obj = see.obj;
				if (obj instanceof Scheme)
				{
					Scheme scheme = (Scheme) obj;
					removeScheme(scheme);
					graph.setGraphChanged(false);
				}
				graph.skip_notify = false;
			}
		}
	}

	protected void openScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		graph.setScheme(sch);
		GraphActions.clearGraph(graph);
		sch.unpack();

		Map clones = graph.copyFromArchivedState(sch.serializable_ugo, new java.awt.Point(0, 0));
		graph.selectionNotify();
	}

	protected boolean removeScheme(Scheme sch)
	{
		SchemeGraph graph = getPanel().getGraph();
		if (graph.getScheme() != null && sch.getId().equals(graph.getScheme().getId()))
		{
			GraphActions.clearGraph(graph);
			graph.setGraphChanged(false);
			return true;
		}
		return false;
	}

}