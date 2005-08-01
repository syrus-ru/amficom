package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeEditor;

public class OpenSchemeEditorCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenSchemeEditorCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		new SchemeEditor();
	}
}
