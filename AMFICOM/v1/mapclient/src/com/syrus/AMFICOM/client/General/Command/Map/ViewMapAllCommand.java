package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;
import com.syrus.AMFICOM.Client.General.Command.Config.*;

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