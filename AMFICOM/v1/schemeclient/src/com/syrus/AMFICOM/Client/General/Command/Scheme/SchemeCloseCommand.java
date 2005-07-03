package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;

public class SchemeCloseCommand extends AbstractCommand
{
	/*ApplicationContext aContext;
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
	}*/
}

