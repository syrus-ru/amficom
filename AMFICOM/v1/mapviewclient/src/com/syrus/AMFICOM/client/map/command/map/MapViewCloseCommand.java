/*
 * $Id: MapViewCloseCommand.java,v 1.11 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс $RCSfile: MapViewCloseCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public class MapViewCloseCommand extends AbstractCommand {
	/**
	 * окно карты
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
