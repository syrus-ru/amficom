package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.General.SchemeGraph;

public class PathCancelCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;

	public PathCancelCommand(ApplicationContext aContext, SchemeGraph graph)
	{
		this.aContext = aContext;
		this.graph = graph;
	}

	public Object clone()
	{
		return new PathCancelCommand(aContext, graph);
	}

	public void execute()
	{
		aContext.getDispatcher().notify(new CreatePathEvent(graph, null, CreatePathEvent.CANCEL_PATH_CREATION_EVENT));
	}
}



