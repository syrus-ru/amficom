package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapSchemeNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

import java.awt.Dimension;

import javax.swing.JDesktopPane;

public class ConfigNewMapViewCommand extends NewMapViewCommand
{
	public ConfigNewMapViewCommand()
	{
	}

	public ConfigNewMapViewCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		super(dispatcher, desktop, aContext, factory);
	}

	public Object clone()
	{
		return new ConfigNewMapViewCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		super.execute();

		Dimension dim = new Dimension(desktop.getWidth(), desktop.getHeight());
		frame.setLocation(0, 0);
		frame.setSize(dim.width * 4 / 5, dim.height);

		new ViewMapPropertiesCommand(desktop, aContext).execute();
		new ViewMapElementsCommand(desktop, aContext).execute();
		new ViewMapSchemeNavigatorCommand(desktop, aContext).execute();
//		new ViewMapSetupCommand(desktop, aContext).execute();
	}
}