package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

public class PathNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemeTabbedPane pane;

	public PathNewCommand(ApplicationContext aContext, SchemeTabbedPane pane)
	{
		this.aContext = aContext;
		this.pane = pane;
	}

	public Object clone()
	{
		return new PathNewCommand(aContext, pane);
	}

	public void execute()
	{
		SchemeGraph graph = pane.getPanel().getGraph();
		SchemePath path = new SchemePath(aContext.getDataSourceInterface().GetUId(SchemePath.typ));
		path.setSchemeId(graph.getScheme().getId());
		graph.setCurrentPath(path);

		ElementAttribute ea = new ElementAttribute(aContext.getDataSourceInterface().GetUId(ElementAttribute.typ), "", "false", "alarmed");
		graph.getCurrentPath().attributes.put(ea.type_id, ea);

//		graph.removeSelectionCells();
		aContext.getDispatcher().notify(new CreatePathEvent(pane.getPanel(), null, CreatePathEvent.CREATE_PATH_EVENT));
	}
}



