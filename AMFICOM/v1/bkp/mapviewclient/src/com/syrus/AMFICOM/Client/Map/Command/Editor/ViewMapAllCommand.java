package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapWindowCommand;

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
		new ViewMapElementsBarCommand(desktop, aContext).execute();
		new ViewMapNavigatorCommand(desktop, aContext).execute();
		new ViewMapWindowCommand(aContext.getDispatcher(), desktop, aContext, factory).execute();
		setResult(Command.RESULT_OK);
	}
}
