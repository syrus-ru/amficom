package com.syrus.AMFICOM.Client.Schedule;

import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ScheduleApplicationModelFactory;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.DefaultScheduleApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;


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
		LangModelSchedule.initialize();

		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new Schedule(new DefaultScheduleApplicationModelFactory());
	}
}
