
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import java.awt.*;
import javax.swing.*;

public class Schedule
{
	ApplicationContext aContext = new ApplicationContext();

	public Schedule(ScheduleApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_SCHEDULE))
			return;

		aContext.setApplicationModel(factory.create());
		Frame frame = new ScheduleMDIMain(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/scheduling_mini.gif"));
//		Environment.addWindow(frame);
		frame.setVisible(true);

	}

	public static void main(String[] args)
	{
		Environment.initialize();
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		new Schedule(new DefaultScheduleApplicationModelFactory());
	}
}

