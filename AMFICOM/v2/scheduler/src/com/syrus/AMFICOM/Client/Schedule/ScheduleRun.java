package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.*;

public class ScheduleRun
{
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