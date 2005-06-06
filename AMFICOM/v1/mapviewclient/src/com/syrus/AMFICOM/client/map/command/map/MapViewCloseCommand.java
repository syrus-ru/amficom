/*
 * $Id: MapViewCloseCommand.java,v 1.12 2005/06/06 12:20:31 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ����� $RCSfile: MapViewCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class MapViewCloseCommand extends AbstractCommand {
	/**
	 * ���� �����
	 */
	MapView mapView;

	public MapViewCloseCommand(MapView mapView) {
		this.mapView = mapView;
	}

	public void execute() {
		//		mapFrame.saveConfig();

		if(this.mapView != null)
			// TODO should be 'remove', not 'delete'
			StorableObjectPool.delete(this.mapView.getId());

		setResult(Command.RESULT_OK);
	}

}
