/*
 * $Id: MapEditorCloseMapCommand.java,v 1.2 2004/10/04 16:04:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapCloseCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;

/**
 * ����� MapMapCloseCommand ������������ ��� �������� ����� � ���� ����� � ������ 
 * "�������� �������������� ����"
 * ����� ���������� ������� MapCloseCommand ��� �������� �����, ����� ����
 * ���������� ������� ��������
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/04 16:04:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapEditorCloseMapCommand extends VoidCommand
{
	MapEditorMainFrame mainFrame;
	Dispatcher dispatcher;

	public MapEditorCloseMapCommand(MapEditorMainFrame mainFrame, Dispatcher dispatcher)
	{
		this.mainFrame = mainFrame;
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new MapEditorCloseMapCommand(mainFrame, dispatcher);
	}

	public void execute()
	{
		if(mainFrame.getMapFrame() == null)
			return;

		new MapCloseCommand(mainFrame.getMapFrame()).execute();
		dispatcher.notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
