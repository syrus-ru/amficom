package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class Schedule {

	private ApplicationContext	aContext;

	public Schedule() {
		if (Environment.canRun(Environment.MODULE_SCHEDULE)) {

			this.aContext = new ApplicationContext();
			this.aContext.setDispatcher(new Dispatcher());
			this.aContext.setApplicationModel(new SchedulerModel(this.aContext));		
			
			ScheduleMainFrame frame = new ScheduleMainFrame(this.aContext);
			frame.setIconImage(UIStorage.SCHEDULING_ICON_MINI);
			frame.setVisible(true);
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Schedule();
	}
}
