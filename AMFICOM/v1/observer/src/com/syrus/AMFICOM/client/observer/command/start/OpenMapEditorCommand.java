package com.syrus.AMFICOM.client.observer.command.start;

import com.syrus.AMFICOM.client.map.editor.MapEditor;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class OpenMapEditorCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenMapEditorCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		new MapEditor();
	}
}
