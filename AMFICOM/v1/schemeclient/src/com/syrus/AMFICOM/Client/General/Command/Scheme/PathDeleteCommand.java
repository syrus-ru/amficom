package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

public class PathDeleteCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemePanel panel;

	public PathDeleteCommand(ApplicationContext aContext, SchemePanel panel)
	{
		this.aContext = aContext;
		this.panel = panel;
	}

	public Object clone()
	{
		return new PathDeleteCommand(aContext, panel);
	}

	public void execute()
	{
		SchemePath path = panel.getGraph().currentPath;
		aContext.getDispatcher().notify(new CreatePathEvent(panel,
				new SchemePath[] {path},
				CreatePathEvent.DELETE_PATH_EVENT));

		Pool.put(SchemePath.typ, path.getId(), path);
		panel.removeCurrentPathFromScheme();
	}
}
