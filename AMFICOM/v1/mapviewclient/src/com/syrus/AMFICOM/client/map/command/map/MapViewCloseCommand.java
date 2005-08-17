/*
 * $Id: MapViewCloseCommand.java,v 1.14 2005/08/17 14:14:18 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.map;

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
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/08/17 14:14:18 $
 * @module mapviewclient
 */
public class MapViewCloseCommand extends AbstractCommand {
	/**
	 * окно карты
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
