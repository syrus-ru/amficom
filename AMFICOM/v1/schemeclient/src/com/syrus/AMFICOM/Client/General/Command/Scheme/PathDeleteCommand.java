package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class PathDeleteCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeTabbedPane pane;

	public PathDeleteCommand(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
	}

	public Object clone()
	{
		return new PathDeleteCommand(aContext, pane);
	}

	public void execute()
	{
		SchemePath path = pane.getPanel().getGraph().getCurrentPath();
		aContext.getDispatcher().notify(new CreatePathEvent(pane.getPanel(),
				new SchemePath[] {path},
				CreatePathEvent.DELETE_PATH_EVENT));

		aContext.getDispatcher().notify(new TreeListSelectionEvent(Scheme.typ,
			TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));


//		Pool.put(SchemePath.typ, path.getId(), path);
		((SchemePanel)pane.getPanel()).removeCurrentPathFromScheme();
	}
}
