package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;

public class PathSetEndCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;

	public PathSetEndCommand(ApplicationContext aContext, SchemeGraph graph)
	{
		this.aContext = aContext;
		this.graph = graph;
	}

	public Object clone()
	{
		return new PathSetEndCommand(aContext, graph);
	}

	public void execute()
	{
		aContext.getDispatcher().notify(new CreatePathEvent(
				graph,
				graph.getSelectionCells(),
				CreatePathEvent.SET_END_EVENT));
		aContext.getDispatcher().notify(new SchemeElementsEvent(this, graph, SchemeElementsEvent.SCHEME_CHANGED_EVENT));
	}
}



