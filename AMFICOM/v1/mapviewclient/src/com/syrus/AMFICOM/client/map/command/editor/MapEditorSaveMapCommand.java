/*
 * $Id: MapEditorSaveMapCommand.java,v 1.5 2005/01/21 13:49:27 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapSaveCommand;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import javax.swing.JDesktopPane;

/**
 * ����� MapEditorSaveContextCommand ������������ ��� ���������� �������������� ����� � ������
 * "�������� �������������� ����". ���������� ������� MapSaveCommand
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/01/21 13:49:27 $
 * @module
 * @author $Author: krupenn $
 * @see MapSaveCommand
 */
public class MapEditorSaveMapCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	/**
	 * 
	 * @param mapFrame ���� �����, �� �������� ��������� �����
	 * @param aContext �������� ������ "�������� �������������� ����"
	 */
	public MapEditorSaveMapCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(desktop);

		if(mapFrame == null)
		{
			System.out.println("map frame is null! Cannot save map.");
			setResult(Command.RESULT_NO);
			return;
		}
		new MapSaveCommand(mapFrame.getMap(), aContext).execute();
		setResult(Command.RESULT_OK);
	}

}
