/*
 * $Id: MapCloseCommand.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.MapStorableObjectPool;

/**
 * ����� $RCSfile: MapCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapCloseCommand extends VoidCommand
{
	/**
	 * ���� �����
	 */
	MapFrame mapFrame;

	public MapCloseCommand()
	{
	}

	public MapCloseCommand(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
	}

	public void execute()
	{
		if(mapFrame == null)
			return;
        System.out.println("Closing map");
		mapFrame.saveConfig();
        mapFrame.setMapView(null);
		
		MapView mapView = mapFrame.getMapView();

		Map map = mapView.getMap();
		try
		{
			MapStorableObjectPool.delete(map.getId());
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}

		MapViewNewCommand cmd = new MapViewNewCommand(mapFrame, mapFrame.getContext());
		cmd.execute();
		MapView mv = cmd.mv;
		mapFrame.setMapView(mv);

		setResult(Command.RESULT_OK);
	}

}
