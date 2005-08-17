/*
 * $Id: MapViewCloseCommand.java,v 1.14 2005/08/17 14:14:18 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/08/17 14:14:18 $
 * @module mapviewclient
 */
public class MapViewCloseCommand extends AbstractCommand {
	/**
	 * ���� �����
	 */
	MapView mapView;

	public MapViewCloseCommand(MapView mapView) {
		this.mapView = mapView;
	}

	@Override
	public void execute() {
		//		mapFrame.saveConfig();

		if(this.mapView != null)
			// TODO should be 'remove', not 'delete'
			StorableObjectPool.delete(this.mapView.getId());

		setResult(Command.RESULT_OK);
	}

}
