package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Config.NewMapViewCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

import javax.swing.JDesktopPane;

public class ViewMapAllCommand extends VoidCommand
{
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public ViewMapAllCommand()
	{
	}

	public ViewMapAllCommand(JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public Object clone()
	{
		return new ViewMapAllCommand(desktop, aContext, factory);
	}

	public void execute()
	{
		new ViewMapPropertiesCommand(desktop, aContext).execute();
		new ViewMapElementsCommand(desktop, aContext).execute();
		new NewMapViewCommand(aContext.getDispatcher(), desktop, aContext, factory).execute();
		new ViewMapSchemeNavigatorCommand(desktop, aContext).execute();
	}
}