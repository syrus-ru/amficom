/*
 * $Id: MapSaveCommand.java,v 1.6 2004/10/26 13:32:01 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesDialog;
import com.syrus.AMFICOM.Client.Map.Props.MapPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Класс $RCSfile: MapSaveCommand.java,v $ используется для сохранения топологической схемы на сервере
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/26 13:32:01 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapSaveCommand extends VoidCommand
{
	MapFrame mapFrame;
	ApplicationContext aContext;

	public MapSaveCommand()
	{
	}

	/**
	 * 
	 * @param mapFrame comments
	 * @param aContext comments
	 */
	public MapSaveCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		Map map = mapFrame.getMapView().getMap();

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapSaving")));

		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModelMap.getString("MapProperties"), 
				true, 
				map,
				MapPanel.getInstance());

		Dimension screenSize =  Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize =  dialog.getSize();

		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		dialog.setLocation(
				(screenSize.width - frameSize.width) / 2, 
				(screenSize.height - frameSize.height) / 2);
		dialog.setVisible(true);

		if ( dialog.ifAccept())
		{
//			aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					true));
			Pool.put( Map.typ, map.getId(), map);
			dataSource.RemoveFromMap(map.getId());
			dataSource.SaveMap(map.getId());
//			aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					false));
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Finished")));
			setResult(Command.RESULT_OK);
		}
		else
		{
			aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
	}

}
