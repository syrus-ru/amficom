/*
 * $Id: MapEditorSaveMapAsCommand.java,v 1.13 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
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
 * ����� $RCSfile: MapEditorSaveMapAsCommand.java,v $ ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����" � ����� ������. ���������� �������
 * MapSaveAsCommand
 * 
 * @version $Revision: 1.13 $, $Date: 2005/09/16 14:53:33 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapSaveAsCommand
 */
public class MapEditorSaveMapAsCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveMapAsCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			Log.debugMessage("map frame is null! Cannot create new map.", Level.SEVERE); //$NON-NLS-1$
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
				mapFrame.setTitle(LangModelMap.getString("Map") + " - " //$NON-NLS-1$ //$NON-NLS-2$
						+ newMap.getName());
			}
		}

		setResult(Command.RESULT_OK);
	}

}
