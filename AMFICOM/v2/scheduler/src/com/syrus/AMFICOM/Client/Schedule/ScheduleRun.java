package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.*;

public class ScheduleRun {

	private ScheduleRun() {
		// nothing
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Schedule(new DefaultScheduleApplicationModelFactory());
	}
}
