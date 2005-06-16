package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class Schedule extends AbstractApplication {
	private static final String APPLICATION_NAME = "scheduler";

	
	public Schedule() {
		super(APPLICATION_NAME);
	}
	
	protected void init() {
		super.init();
		super.aContext.setApplicationModel(new SchedulerModel(super.aContext));				
		super.startMainFrame(new ScheduleMainFrame(super.aContext), UIStorage.SCHEDULING_ICON_MINI);
	}

	public static void main(String[] args) {
		new Schedule();
	}
}
