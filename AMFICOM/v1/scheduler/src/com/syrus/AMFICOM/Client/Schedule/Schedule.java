package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Application;

public class Schedule {
	private static final String APPLICATION_NAME = "scheduler";

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
		Application.init(APPLICATION_NAME);

		new Schedule();
	}
}
