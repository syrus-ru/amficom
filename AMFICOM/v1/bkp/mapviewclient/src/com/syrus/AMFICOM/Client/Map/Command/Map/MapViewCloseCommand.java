/*
 * $Id: MapViewCloseCommand.java,v 1.7 2004/12/28 17:35:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
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
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;

/**
 * Класс $RCSfile: MapViewCloseCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/28 17:35:12 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapViewCloseCommand extends VoidCommand
{
	/**
	 * окно карты
	 */
	MapView mapView;

	public MapViewCloseCommand(MapView mapView)
	{
		this.mapView = mapView;
	}

	public void execute()
	{
//		mapFrame.saveConfig();

		if(mapView != null)
		try
		{
			// TODO should be 'remove', node 'delete'
			MapViewStorableObjectPool.delete(mapView.getId());
		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}

		setResult(Command.RESULT_OK);
	}

}
