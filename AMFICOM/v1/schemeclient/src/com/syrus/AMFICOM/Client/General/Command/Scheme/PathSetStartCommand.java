package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;

public class PathSetStartCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeTabbedPane pane;

	public PathSetStartCommand(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
	}

	public Object clone()
	{
		return new PathSetStartCommand(aContext, pane);
	}

	public void execute()
	{
		SchemeGraph graph = pane.getPanel().getGraph();
		aContext.getDispatcher().notify(new CreatePathEvent(
				graph,
				graph.getSelectionCells(),
				CreatePathEvent.SET_START_EVENT));
	}
}



