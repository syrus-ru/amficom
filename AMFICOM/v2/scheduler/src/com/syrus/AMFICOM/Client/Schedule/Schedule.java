package com.syrus.AMFICOM.Client.Schedule;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.DefaultScheduleApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ScheduleApplicationModelFactory;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class Schedule {

	ApplicationContext	aContext;

	public Schedule(ScheduleApplicationModelFactory factory) {
		if (Environment.canRun(Environment.MODULE_SCHEDULE)) {

			this.aContext = new ApplicationContext();
			this.aContext.setApplicationModel(factory.create());
			aContext.setDispatcher(new Dispatcher());			
			
			ScheduleMainFrame frame = new ScheduleMainFrame(this.aContext);
//			SchedulerModel model =	
			new SchedulerModel(this.aContext);

			frame.setIconImage(UIStorage.SCHEDULING_ICON_MINI);
			frame.setVisible(true);
		}
	}

	public static void main(String[] args) {
		Environment.initialize();
		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Schedule(new DefaultScheduleApplicationModelFactory());
	}
}
