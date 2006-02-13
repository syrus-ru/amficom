package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.launcher.Launcher;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class Schedule extends AbstractApplication {
	private static final String APPLICATION_NAME = "scheduler";

	
	public Schedule() {
		super(APPLICATION_NAME);
	}
	
	@Override
	protected void init() {		
		super.aContext.setApplicationModel(new SchedulerModel(super.aContext));
		final ImageIcon imageIcon = (ImageIcon) UIManager.getIcon(UIStorage.ICON_SCHEDULER_MINI);
		super.startMainFrame(new ScheduleMainFrame(this.aContext), imageIcon.getImage());
	}

	public static void main(String[] args) {
		Launcher.launchApplicationClass(Schedule.class);
	}
}
