package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.CreatePathEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanel;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

public class PathNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	SchemePanel panel;

	public PathNewCommand(ApplicationContext aContext, SchemePanel panel)
	{
		this.aContext = aContext;
		this.panel = panel;
	}

	public Object clone()
	{
		return new PathNewCommand(aContext, panel);
	}

	public void execute()
	{
		panel.getGraph().currentPath = new SchemePath(aContext.getDataSourceInterface().GetUId(SchemePath.typ));

		ElementAttribute ea = new ElementAttribute(aContext.getDataSourceInterface().GetUId(ElementAttribute.typ), "", "false", "alarmed");
		panel.getGraph().currentPath.attributes.put(ea.type_id, ea);

		panel.getGraph().removeSelectionCells();
		aContext.getDispatcher().notify(new CreatePathEvent(panel, null, CreatePathEvent.CREATE_PATH_EVENT));
	}
}



