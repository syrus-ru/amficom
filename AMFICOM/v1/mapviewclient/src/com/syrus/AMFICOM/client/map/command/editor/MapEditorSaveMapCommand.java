/*
 * $Id: MapEditorSaveMapCommand.java,v 1.8 2005/06/06 12:20:31 krupenn Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.command.editor;

import com.syrus.AMFICOM.client.map.command.map.MapSaveCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;

import javax.swing.JDesktopPane;

/**
 * Класс MapEditorSaveContextCommand используется для сохранения топологической
 * схемы в модуле "Редактор топологических схем". Использует команду
 * MapSaveCommand
 * 
 * @version $Revision: 1.8 $, $Date: 2005/06/06 12:20:31 $
 * @author $Author: krupenn $
 * @module
 * @see MapSaveCommand
 */
public class MapEditorSaveMapCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveMapCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			System.out.println("map frame is null! Cannot save map.");
			setResult(Command.RESULT_NO);
			return;
		}
		new MapSaveCommand(mapFrame.getMap(), this.aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
