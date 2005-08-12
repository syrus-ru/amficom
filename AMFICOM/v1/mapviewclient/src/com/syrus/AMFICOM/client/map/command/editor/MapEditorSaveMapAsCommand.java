/*
 * $Id: MapEditorSaveMapAsCommand.java,v 1.11 2005/08/12 10:44:48 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapSaveAsCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapEditorSaveMapAsCommand.java,v $ используется для сохранения топологической схемы в модуле
 * "Редактор топологических схем" с новым именем. Использует команду
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.11 $, $Date: 2005/08/12 10:44:48 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveMapAsCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveMapAsCommand(
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
		MapSaveAsCommand msac = new MapSaveAsCommand(
				mapFrame.getMap(),
				this.aContext);
		msac.execute();

		if(msac.getResult() == RESULT_OK) {
			Map newMap = msac.getNewMap();

			if(mapFrame != null) {
				mapFrame.getMapView().setMap(newMap);
				mapFrame.setTitle(LangModelMap.getString("Map") + " - "
						+ newMap.getName());
			}
		}

		setResult(Command.RESULT_OK);
	}

}
