package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.Schedule.Schedule;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class OpenSchedulerCommand extends AbstractCommand {
	ApplicationContext aContext;

	public OpenSchedulerCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		return new OpenSchedulerCommand(this.aContext);
	}

	public void execute() {
		new Schedule();
	}
}

