package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeTabbedPane;

public class PathSaveCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeTabbedPane pane;

	public PathSaveCommand(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
	}

	public Object clone()
	{
		return new PathSaveCommand(aContext, pane);
	}

	public void execute()
	{
		aContext.getDispatcher().notify(new CreatePathEvent(pane.getPanel(),
				null,
				CreatePathEvent.SAVE_PATH_EVENT));
	}
}



