/*
 * $Id: MapViewCloseCommand.java,v 1.15 2005/08/24 10:20:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ����� $RCSfile: MapViewCloseCommand.java,v $ ������������ ��� �������� �����
 * ��� ���������� �� ������ ������ ���� �����. ��� ���� � ��������� ����
 * ������������ ���������� � ���, ��� �������� ����� ���, � ����� ������������
 * �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/08/24 10:20:59 $
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
		// mapFrame.saveConfig();

		if(this.mapView != null) {
			// TODO should clean only changes in this.mapview
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.MAPVIEW_CODE);
		}

		setResult(Command.RESULT_OK);
	}

}
