package com.syrus.AMFICOM.Client.General.Command.Schedule;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import java.awt.*;
import javax.swing.*;

public class MySaveCommand extends VoidCommand
{
  ScheduleMDIMain parent;
  ApplicationContext aContext;

	public MySaveCommand()
	{
	}

	public MySaveCommand(ScheduleMDIMain parent, ApplicationContext aContext)
	{
	  this.parent = parent;
	  this.aContext = aContext;
	}

	public Object clone()
	{
		return new MySaveCommand(parent, aContext);
	}

	public void resize()
	{
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			if (fram[i] instanceof MySaveFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(screenSize.width*3/4, screenSize.height*3/4, screenSize.width/4, screenSize.height/4);
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
			if (fram[i] instanceof MySaveFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(screenSize.width*3/4, screenSize.height*3/4, screenSize.width/4, screenSize.height/4);
				 bool = true;
			}
		}
		if (bool == false)
		{
			MySaveFrame frame = new MySaveFrame(parent, aContext);
			frame.setBounds(screenSize.width*3/4, screenSize.height*3/4, screenSize.width/4, screenSize.height/4);
			parent.desktopPane.add(frame, BorderLayout.SOUTH);
			frame.show();
		}
	}
}