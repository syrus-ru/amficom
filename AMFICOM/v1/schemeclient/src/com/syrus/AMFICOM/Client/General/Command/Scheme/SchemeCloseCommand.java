package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.General.GraphActions;
import com.syrus.AMFICOM.Client.Schematics.General.SchemeGraph;
import com.syrus.AMFICOM.Client.Resource.Pool;

public class SchemeCloseCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeGraph graph;

	public SchemeCloseCommand(ApplicationContext aContext, SchemeGraph graph)
	{
		this.aContext = aContext;
		this.graph = graph;
	}

	public Object clone()
	{
		return new SchemeCloseCommand(aContext, graph);
	}

	public void execute()
	{
		if (graph != null)
			GraphActions.clearGraph(graph);

		aContext.getDispatcher().notify(new SchemeElementsEvent(this, null, SchemeElementsEvent.CLOSE_SCHEME_EVENT));
	}
}

