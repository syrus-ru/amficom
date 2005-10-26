package com.syrus.AMFICOM.client.observer.command.start;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.Analyse;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class OpenAnalysisCommand extends AbstractCommand {
	ApplicationContext aContext;

	public OpenAnalysisCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		new Analyse();
	}
}
