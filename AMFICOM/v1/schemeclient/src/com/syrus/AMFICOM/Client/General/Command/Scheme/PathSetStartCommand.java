package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;

public class PathSetStartCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;

	public PathSetStartCommand(ApplicationContext aContext, SchemeGraph graph)
	{
		this.aContext = aContext;
		this.graph = graph;
	}

	public Object clone()
	{
		return new PathSetStartCommand(aContext, graph);
	}

	public void execute()
	{
		aContext.getDispatcher().notify(new CreatePathEvent(
				graph,
				graph.getSelectionCells(),
				CreatePathEvent.SET_START_EVENT));
		graph.setGraphChanged(true);
	}
}



