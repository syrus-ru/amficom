/*
 * $Id: MapViewSaveAsCommand.java,v 1.6 2004/12/08 16:20:22 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Props.MapViewPanel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Класс $RCSfile: MapViewSaveAsCommand.java,v $ используется для сохранения топологической схемы с новым
 * именем
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/08 16:20:22 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapViewSaveAsCommand extends VoidCommand
{
	MapFrame mapFrame;
    ApplicationContext aContext;

	public MapViewSaveAsCommand()
	{
	}

	/**
	 * 
	 * @param paramName comments
	 * @exception Exception comments
	 */
	public MapViewSaveAsCommand(MapFrame mapFrame, ApplicationContext aContext)
	{
		this.mapFrame = mapFrame;
		this.aContext = aContext;
	}

	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSource();

		if(dataSource == null)
			return;
			
		MapView mv = mapFrame.getMapView();

		MapView mv2 = null;//(MapView)mv.clone(dataSource);

		ObjectResourcePropertiesDialog dialog = new ObjectResourcePropertiesDialog(
				Environment.getActiveWindow(), 
				LangModelMap.getString("MapViewProperties"), 
				true, 
				mv2,
				MapViewPanel.getInstance());

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
			Pool.put( MapView.typ, mv2.getId(), mv2);
			
			dataSource.SaveMapView(mv2.getId());
	
			if (mapFrame != null)
			{
				mapFrame.setMapView(mv2);
				mv2.setMap(mv.getMap());
				mapFrame.setTitle( LangModelMap.getString("Map") + " - "
												 + mv2.getName());
			}
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
