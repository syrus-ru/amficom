package com.syrus.AMFICOM.Client.General.Command.Schedule;

//import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import java.awt.*;
import javax.swing.*;

public class MyTimeCommand extends VoidCommand
{
  ScheduleMDIMain parent;
  ApplicationContext aContext;

	public MyTimeCommand()
	{
	}

	public MyTimeCommand(ScheduleMDIMain parent, ApplicationContext aContext)
	{
	  this.parent = parent;
	  this.aContext = aContext;
	}

	public Object clone()
	{
		return new MyTimeCommand(parent, aContext);
	}

	public void resize()
	{
		Dimension screenSize = parent.desktopPane.getSize();
		JInternalFrame fram[] = parent.desktopPane.getAllFrames();
		for (int i = 0; i < fram.length; i++)
		{
			if (fram[i] instanceof MyTimeFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(screenSize.width*3/4, screenSize.height*30/200, screenSize.width/4, screenSize.height*35/200);
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
			if (fram[i] instanceof MyTimeFrame)
			{
				 fram[i].toFront();
				 fram[i].setBounds(screenSize.width*3/4, screenSize.height*30/200, screenSize.width/4, screenSize.height*35/200);
				 bool = true;
			}
		}
		if (bool == false)
		{
			MyTimeFrame frame = new MyTimeFrame(parent, aContext);
			frame.setBounds(screenSize.width*3/4, screenSize.height*30/200, screenSize.width/4, screenSize.height*35/200);
			parent.desktopPane.add(frame, BorderLayout.EAST);
			frame.show();
		}

	}
}

