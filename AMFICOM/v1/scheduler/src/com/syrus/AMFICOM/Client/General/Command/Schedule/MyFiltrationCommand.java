package com.syrus.AMFICOM.Client.General.Command.Schedule;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import java.awt.*;
import javax.swing.*;

public class MyFiltrationCommand extends VoidCommand
{
  ScheduleMDIMain parent;
  ApplicationContext aContext;
  DataSet ds;

	public MyFiltrationCommand()
	{
	}

	public MyFiltrationCommand(ScheduleMDIMain parent, ApplicationContext aContext, DataSet ds)
	{
		this.ds = ds;
		this.parent = parent;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MyFiltrationCommand(parent, aContext, ds);
	}

	public void resize()
	{
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			if (fram[i] instanceof MyFiltrationFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(screenSize.width*3/4, 0, screenSize.width/4, screenSize.height);
			}
		}
	}

	public void execute()
	{
		boolean bool = false;
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			if (fram[i] instanceof MyFiltrationFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(screenSize.width*3/4, 0, screenSize.width/4, screenSize.height);
				 bool = true;
			}
		}
		if (bool == false)
		{
			MyFiltrationFrame frame = new MyFiltrationFrame(parent, aContext, ds);
			frame.setBounds(screenSize.width*3/4, 0, screenSize.width/4, screenSize.height);
			parent.desktopPane.add(frame, BorderLayout.NORTH);
			frame.show();
		}
	}
}