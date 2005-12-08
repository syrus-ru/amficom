package com.syrus.AMFICOM.client.observer.command;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.OpenLinkedMapViewCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.util.Log;

public class OpenMapViewCommand extends OpenLinkedMapViewCommand {
	public OpenMapViewCommand(
			JDesktopPane desktop, 
			ApplicationContext aContext, 
			MapApplicationModelFactory factory) {
		super(desktop, aContext, factory);
	}

	@Override
	public void execute() {
		try {
			super.execute();

			if(super.getResult() == Command.RESULT_OK) {
				MapFrame frame = MapDesktopCommand.findMapFrame(super.desktop);
				frame.getModel().getCommand(MapApplicationModel.MODE_NODES).execute();
				frame.getModel().getCommand(MapApplicationModel.MODE_PATH).execute();
			}
		} catch(RuntimeException ex) {
			Log.errorMessage(ex);
			setResult(Command.RESULT_NO);
		}
	}
}
