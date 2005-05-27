/*
 * $Id: MapEditorCloseMapCommand.java,v 1.10 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapCloseCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapNewCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ����� MapMapCloseCommand ������������ ��� �������� ����� � ���� ����� � ������ 
 * "�������� �������������� ����"
 * ����� ���������� ������� MapCloseCommand ��� �������� �����, ����� ����
 * ���������� ������� ��������
 * 
 * @version $Revision: 1.10 $, $Date: 2005/05/27 15:14:55 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see MapCloseCommand
 */
public class MapEditorCloseMapCommand extends AbstractCommand
{
	JDesktopPane desktop;
	Dispatcher dispatcher;

	public MapEditorCloseMapCommand(JDesktopPane desktop, Dispatcher dispatcher)
	{
		this.desktop = desktop;
		this.dispatcher = dispatcher;
	}

	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		if(!mapFrame.checkCanCloseMap())
			return;
		if(!mapFrame.checkCanCloseMapView())
			return;

		new MapCloseCommand(mapFrame.getMap()).execute();

		MapNewCommand cmd = new MapNewCommand(mapFrame.getContext());
		cmd.execute();
		
		Map map = cmd.getMap();

		MapView mapView = mapFrame.getMapView();

		mapView.setMap(map);

		this.dispatcher.firePropertyChange(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));
		setResult(Command.RESULT_OK);
	}

}
