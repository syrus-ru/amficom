/*-
 * $$Id: MapViewCloseCommand.java,v 1.16 2005/09/30 16:08:38 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс $RCSfile: MapViewCloseCommand.java,v $ используется для закрытия карты
 * при сохранении на экране самого окна карты. При этом в азголовке окна
 * отображается информация о том, что активной карты нет, и карта центрируется
 * по умолчанию
 * 
 * @version $Revision: 1.16 $, $Date: 2005/09/30 16:08:38 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
		// mapFrame.saveConfig();

		if(this.mapView != null) {
			// TODO should clean only changes in this.mapview
			StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.MAPVIEW_CODE);
		}

		setResult(Command.RESULT_OK);
	}

}
