/*
 * $Id: MapViewCloseCommand.java,v 1.2 2004/10/04 16:04:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

/**
 * Класс $RCSfile: MapViewCloseCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/04 16:04:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapViewCloseCommand extends VoidCommand
{
	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapViewCloseCommand()
	{
	}

	public MapViewCloseCommand(MapFrame mapFrame)
	{
		this.mapFrame = mapFrame;
	}

	public Object clone()
	{
		return new MapViewCloseCommand(mapFrame);
	}

	public void execute()
	{
		if(mapFrame == null)
			return;
        System.out.println("Closing map view");
		mapFrame.saveConfig();
        mapFrame.setMapView(null);

		MapViewNewCommand cmd = new MapViewNewCommand(mapFrame, mapFrame.getContext());
		cmd.execute();
		MapView mv = cmd.mv;
		mapFrame.setMapView(mv);

		setResult(Command.RESULT_OK);
	}

}
