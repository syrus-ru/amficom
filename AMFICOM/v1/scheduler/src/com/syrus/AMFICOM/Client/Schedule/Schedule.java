package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.util.Application;

public class Schedule extends AbstractApplication {
	private static final String APPLICATION_NAME = "scheduler";

	protected void init() {
		super.init();
		super.aContext.setApplicationModel(new SchedulerModel(super.aContext));				
		super.startMainFrame(new ScheduleMainFrame(super.aContext), UIStorage.SCHEDULING_ICON_MINI);
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		new Schedule();
	}
}
