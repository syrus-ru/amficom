/*
 * $Id: MapEditorSaveMapCommand.java,v 1.7 2005/05/27 15:14:55 krupenn Exp $
 * Syrus Systems ������-����������� ����� ������: �������
 */

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.Map.Command.Map.MapSaveCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;

import javax.swing.JDesktopPane;

/**
 * ����� MapEditorSaveContextCommand ������������ ��� ���������� ��������������
 * ����� � ������ "�������� �������������� ����". ���������� �������
 * MapSaveCommand
 * 
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:55 $
 * @author $Author: krupenn $
 * @module
 * @see MapSaveCommand
 */
public class MapEditorSaveMapCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext �������� ������ "�������� �������������� ����"
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
