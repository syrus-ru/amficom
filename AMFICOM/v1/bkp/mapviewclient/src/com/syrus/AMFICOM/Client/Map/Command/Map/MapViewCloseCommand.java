/*
 * $Id: MapViewCloseCommand.java,v 1.10 2005/02/22 11:00:14 krupenn Exp $
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * ����� $RCSfile: MapViewCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/02/22 11:00:14 $
 * @module mapviewclient_v1
 */
public class MapViewCloseCommand extends VoidCommand
{
	/**
	 * ���� �����
	 */
	MapView mapView;

	public MapViewCloseCommand(MapView mapView)
	{
		this.mapView = mapView;
	}

	public void execute()
	{
//		mapFrame.saveConfig();

		if(this.mapView != null)
			// TODO should be 'remove', not 'delete'
			MapViewStorableObjectPool.delete(this.mapView.getId());

		setResult(Command.RESULT_OK);
	}

}
