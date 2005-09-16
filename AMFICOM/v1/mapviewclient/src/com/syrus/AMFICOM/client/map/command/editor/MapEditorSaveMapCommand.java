/*
 * $Id: MapEditorSaveMapCommand.java,v 1.12 2005/09/16 14:53:33 krupenn Exp $
 * Syrus Systems Научно-технический центр Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapSaveCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.util.Log;

/**
 * Класс MapEditorSaveContextCommand используется для сохранения топологической
 * схемы в модуле "Редактор топологических схем". Использует команду
 * MapSaveCommand
 * 
 * @version $Revision: 1.12 $, $Date: 2005/09/16 14:53:33 $
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

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			Log.debugMessage("map frame is null! Cannot save map.", Level.SEVERE); //$NON-NLS-1$
			setResult(Command.RESULT_NO);
			return;
		}
		new MapSaveCommand(mapFrame.getMap(), this.aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
