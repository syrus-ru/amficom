/*
 * $Id: MapEditorCloseViewCommand.java,v 1.6 2004/12/28 17:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewCloseCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewNewCommand;
import com.syrus.AMFICOM.Client.Map.Editor.MapEditorMainFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.map.Map;

/**
 * ����� MapMapCloseCommand ������������ ��� �������� ����� � ���� ����� � ������ 
 * "�������� �������������� ����"
 * ����� ���������� ������� MapCloseCommand ��� �������� �����, ����� ����
 * ���������� ������� ��������
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/28 17:35:12 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapEditorCloseViewCommand extends VoidCommand
{
	MapEditorMainFrame mainFrame;
	Dispatcher dispatcher;

	public MapEditorCloseViewCommand(MapEditorMainFrame mainFrame, Dispatcher dispatcher)
	{
		this.mainFrame = mainFrame;
		this.dispatcher = dispatcher;
	}

	public void execute()
	{
		MapFrame mapFrame = mainFrame.getMapFrame();

		if(mapFrame == null)
			return;

		if(!mapFrame.checkCanCloseMap())
			return;
		if(!mapFrame.checkCanCloseMapView())
			return;

		new MapViewCloseCommand(mapFrame.getMapView()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();
		
		Map map = cmd.getMap();

		MapViewNewCommand cmd2 = new MapViewNewCommand(map, mapFrame.getContext());
		cmd2.execute();

		MapView mapView = cmd2.getMapView();

        mapFrame.setMapView(mapView);

		dispatcher.notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
