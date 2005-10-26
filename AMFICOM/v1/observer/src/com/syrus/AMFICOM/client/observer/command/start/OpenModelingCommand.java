package com.syrus.AMFICOM.client.observer.command.start;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class OpenModelingCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenModelingCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		// TODO new Modeling);
	}
}
