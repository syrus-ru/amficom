package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.DefaultScheduleApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class ScheduleRun
{
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