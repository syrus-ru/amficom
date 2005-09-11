package com.syrus.AMFICOM.client.observer.command;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;

public class OpenMapViewCommand extends MapEditorOpenViewCommand {

	public OpenMapViewCommand(
			JDesktopPane desktop, 
			ApplicationContext aContext, 
			MapApplicationModelFactory factory) {
		super(desktop, aContext, factory);
		setCanDelete(false);
	}

	public void execute() {
		try {
			super.execute();

			if(super.getResult() == Command.RESULT_OK) {
				MapFrame frame = super.getMapFrame();
				frame.getModel().getCommand(MapApplicationModel.MODE_NODES).execute();
				frame.getModel().getCommand(MapApplicationModel.MODE_PATH).execute();
			}
		} catch(RuntimeException ex) {
			ex.printStackTrace();
			setResult(Command.RESULT_NO);
		}
	}
}
