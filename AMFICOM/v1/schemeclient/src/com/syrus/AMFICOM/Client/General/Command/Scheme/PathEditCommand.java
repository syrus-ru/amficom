package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schematics.General.SchemePanel;

public class PathEditCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemePanel panel;

	public PathEditCommand(ApplicationContext aContext, SchemePanel panel)
	{
		this.aContext = aContext;
		this.panel = panel;
	}

	public Object clone()
	{
		return new PathEditCommand(aContext, panel);
	}

	public void execute()
	{
		aContext.getDispatcher().notify(new CreatePathEvent(panel, null, CreatePathEvent.EDIT_PATH_EVENT));
	}
}



