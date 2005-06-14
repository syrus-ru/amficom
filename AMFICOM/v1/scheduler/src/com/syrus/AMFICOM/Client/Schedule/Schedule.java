package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Application;

public class Schedule extends AbstractApplication {
	private static final String APPLICATION_NAME = "scheduler";

	public Schedule() {
		if (Environment.canRun(Environment.MODULE_SCHEDULE)) {			
			super.aContext.setApplicationModel(new SchedulerModel(super.aContext));				
			super.startMainFrame(new ScheduleMainFrame(super.aContext), UIStorage.SCHEDULING_ICON_MINI);
		}
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		new Schedule();
	}
}
