package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

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