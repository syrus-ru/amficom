package com.syrus.AMFICOM.Client.General.Command.Schedule;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import java.awt.*;
import javax.swing.*;

public class MyMapCommand extends VoidCommand implements OperationListener
{
	ScheduleMDIMain parent;
	ApplicationContext aContext;
	Dispatcher internal_dispatcher;

	public MyMapCommand()
	{
	}

	public MyMapCommand(ScheduleMDIMain parent, ApplicationContext aContext)
	{
	  this.parent = parent;
	  this.aContext = aContext;
	}

	public Object clone()
	{
		return new MyMapCommand(parent, aContext);
	}

	public void resize()
	{
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			String str = fram[i].getName();
			if (str != null && str.equals("map"))
			{
				 fram[i].toFront();
				 fram[i].setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
			}
		}
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("mapframeshownevent"))
		{
			JInternalFrame MapFrame = (JInternalFrame )ae.getSource();

			Dimension dim = parent.desktopPane.getSize();

			MapFrame.setName("map");
			MapFrame.setLocation(0, 0);
			MapFrame.setSize(dim.width*3/4, dim.height*3/4);

			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuVisualizeMapEdit", true);
			aModel.setEnabled("menuVisualizeMapClose", true);
			aModel.fireModelChanged("");

		}
	}

	public void execute()
	{
		internal_dispatcher = aContext.getDispatcher();
		internal_dispatcher.register(this,"mapframeshownevent");
		boolean bool = false;
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			String str = fram[i].getName();
			if (str != null && str.equals("map"))
			{
				 fram[i].toFront();
				 fram[i].setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
				 bool = true;
			}
		}
		if (bool == false)
		{
			new com.syrus.AMFICOM.Client.General.Command.Config.MapViewOpenCommand(internal_dispatcher, parent.desktopPane, aContext, new MapScheduleApplicationModelFactory()).execute();
		}
	}
}

