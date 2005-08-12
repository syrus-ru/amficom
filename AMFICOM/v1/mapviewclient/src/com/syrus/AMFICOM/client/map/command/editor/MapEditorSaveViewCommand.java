/*
 * $Id: MapEditorSaveViewCommand.java,v 1.11 2005/08/12 10:44:48 krupenn Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewSaveCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.util.Log;

/**
 * Класс MapEditorSaveViewCommand используется для сохранения топологической
 * схемы в модуле "Редактор топологических схем". Использует команду
 * MapSaveCommand
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/08/12 10:44:48 $
 * @module mapviewclient
 * @see MapViewSaveCommand
 */
public class MapEditorSaveViewCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveViewCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(mapFrame == null) {
			Log.debugMessage("map frame is null! Cannot create new map.", Level.SEVERE);
			setResult(Command.RESULT_NO);
			return;
		}
		new MapViewSaveCommand(mapFrame.getMapView(), this.aContext).execute();

		setResult(Command.RESULT_OK);
	}

}
