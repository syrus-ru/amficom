/**
 * $Id: MapEditorSaveLibraryAsCommand.java,v 1.1 2005/08/02 07:22:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.map.MapLibrary;

public class MapEditorSaveLibraryAsCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorSaveLibraryAsCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		Set<MapLibrary> mapLibraries = mapFrame.getMapViewer().getLogicalNetLayer().getMapLibraries();

		for(Iterator iter = mapLibraries.iterator(); iter.hasNext();) {
			MapLibrary mapLibrary = (MapLibrary )iter.next();
			//todo save
		}
	}
}
