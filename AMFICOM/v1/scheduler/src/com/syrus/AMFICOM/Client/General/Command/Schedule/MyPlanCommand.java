package com.syrus.AMFICOM.Client.General.Command.Schedule;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import java.awt.*;
import javax.swing.*;

public class MyPlanCommand extends VoidCommand
{
	ScheduleMDIMain parent;
	ApplicationContext aContext;

	public MyPlanCommand()
	{
	}

	public MyPlanCommand(ScheduleMDIMain parent, ApplicationContext aContext)
	{
		this.parent = parent;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MyPlanCommand(parent, aContext);
	}

	public void resize()
	{
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			if (fram[i] instanceof MyPlanFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
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
			if (fram[i] instanceof MyPlanFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
				 bool = true;
			}
		}
		if (bool == false)
		{
			MyPlanFrame frame = new MyPlanFrame(parent, aContext);
			frame.setBounds(0, 0, screenSize.width*3/4, screenSize.height*3/4);
			parent.desktopPane.add(frame, BorderLayout.NORTH);
			frame.show();
		}
	}
}

