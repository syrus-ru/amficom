package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.*;

public class Schedule
{
	ApplicationContext aContext = new ApplicationContext();

	public Schedule(ScheduleApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_SCHEDULE))
			return;

		aContext.setApplicationModel(factory.create());
		ScheduleMainFrame frame = new ScheduleMainFrame(aContext);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/scheduling_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		Environment.initialize();
		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new Schedule(new DefaultScheduleApplicationModelFactory());
	}
}
