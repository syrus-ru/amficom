/*
 * $Id: MapViewCloseCommand.java,v 1.6 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * ����� $RCSfile: MapViewCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/22 16:38:40 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapViewCloseCommand extends VoidCommand
{
	/**
	 * ���� �����
	 */
	MapFrame mapFrame;

	public MapViewCloseCommand()
	{
	}

	public MapViewCloseCommand(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
	}

	public void execute()
	{
		if(mapFrame == null)
			return;
        System.out.println("Closing map view");
		mapFrame.saveConfig();

		MapView mapView = mapFrame.getMapView();

		Map map = mapView.getMap();

        mapFrame.setMapView(null);

		MapViewNewCommand cmd = new MapViewNewCommand(mapFrame, mapFrame.getContext());
		cmd.execute();
		MapView mv = cmd.mv;
		mapFrame.setMapView(mv);

		setResult(Command.RESULT_OK);
	}

}
