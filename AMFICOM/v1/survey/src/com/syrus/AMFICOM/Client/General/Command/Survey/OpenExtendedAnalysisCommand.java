package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class OpenExtendedAnalysisCommand extends AbstractCommand {
	ApplicationContext aContext;

	public OpenExtendedAnalysisCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		new AnalyseExt();
	}
}
