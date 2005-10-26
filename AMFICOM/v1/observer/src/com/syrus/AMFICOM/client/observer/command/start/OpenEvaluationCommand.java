package com.syrus.AMFICOM.client.observer.command.start;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class OpenEvaluationCommand extends AbstractCommand {
	ApplicationContext aContext;

	public OpenEvaluationCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		new Evaluation();
	}
}
