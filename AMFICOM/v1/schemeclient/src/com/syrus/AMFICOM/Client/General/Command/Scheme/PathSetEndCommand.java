package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;

public class PathSetEndCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeTabbedPane pane;

	public PathSetEndCommand(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
	}

	public Object clone()
	{
		return new PathSetEndCommand(aContext, pane);
	}

	public void execute()
	{
		SchemeGraph graph = pane.getPanel().getGraph();
		aContext.getDispatcher().notify(new CreatePathEvent(
				graph,
				graph.getSelectionCells(),
				CreatePathEvent.SET_END_EVENT));
	}
}



