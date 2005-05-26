package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.scheme.SchemePath;

public class PathDeleteCommand extends AbstractCommand
{
	/*ApplicationContext aContext;
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
		SchemePath path = pane.getCurrentPanel().getSchemeResource().getSchemePath();
		aContext.getDispatcher().notify(new CreatePathEvent(pane,
				new SchemePath[] {path},
				CreatePathEvent.DELETE_PATH_EVENT));

		aContext.getDispatcher().notify(new TreeListSelectionEvent("",
			TreeListSelectionEvent.SELECT_EVENT + TreeListSelectionEvent.REFRESH_EVENT));


//		Pool.put(SchemePath.typ, path.getId(), path);
//		(pane.getCurrentPanel()).removeCurrentPathFromScheme();
	}*/
}
