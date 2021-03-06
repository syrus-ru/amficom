/*
 * $Id: MapEditorSaveMapAsCommand.java,v 1.7 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapSaveAsCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import javax.swing.JDesktopPane;

/**
 * ????? $RCSfile: MapEditorSaveMapAsCommand.java,v $ ???????????? ??? ?????????? ?????????????? ????? ? ??????
 * "???????? ?????????????? ????" ? ????? ??????. ?????????? ???????
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:55 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveMapAsCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext ???????? ?????? "???????? ?????????????? ????"
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
			System.out.println("map frame is null! Cannot create new map.");
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
