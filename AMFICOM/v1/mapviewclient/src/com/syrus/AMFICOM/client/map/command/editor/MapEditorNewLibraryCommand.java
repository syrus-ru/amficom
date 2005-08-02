/**
 * $Id: MapEditorNewLibraryCommand.java,v 1.1 2005/08/02 07:22:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.MapLibrary;

public class MapEditorNewLibraryCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapEditorNewLibraryCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		try {
			MapLibrary mapLibrary = new MapLibrary(
					LoginManager.getUserId(),
					IdentifierPool.getGeneratedIdentifier((short )1),// todo use ObjectEntities
					"new",
					null);
			
			MapViewController.addMapLibrary(mapLibrary);
			mapFrame.getMapViewer().getLogicalNetLayer().addMapLibrary(mapLibrary);
			setResult(Command.RESULT_OK);

		} catch(IdentifierGenerationException e) {
			e.printStackTrace();
			setResult(Command.RESULT_NO);
		}
	}
}
