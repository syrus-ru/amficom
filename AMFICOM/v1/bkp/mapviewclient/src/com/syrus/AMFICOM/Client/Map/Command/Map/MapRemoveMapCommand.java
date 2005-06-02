/**
 * $Id: MapRemoveMapCommand.java,v 1.1 2005/03/04 14:30:27 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapTableController;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceChooserDialog;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

import javax.swing.JDesktopPane;

/**
 * убрать из вида выбранную схему 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/03/04 14:30:27 $
 * @module mapviewclient_v1
 */
public class MapRemoveMapCommand extends VoidCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	public MapRemoveMapCommand(JDesktopPane desktop, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.aContext = aContext;
	}
	
	public void execute()
	{
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		
		if(mapFrame == null)
			return;
	
		MapView mapView = mapFrame.getMapView();
	
		if(mapView == null)
			return;

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelMap.getString("MapOpening")));

		ObjectResourceChooserDialog mcd = new ObjectResourceChooserDialog(
				LangModelMap.getString("Map"),
				MapTableController.getInstance());

		mcd.setContents(mapView.getMap().getMaps());

		mcd.setModal(true);
		mcd.setVisible(true);
		if(mcd.getReturnCode() != ObjectResourceChooserDialog.RET_OK)
		{
			this.aContext.getDispatcher().notify(new StatusMessageEvent(
					StatusMessageEvent.STATUS_MESSAGE,
					LangModel.getString("Aborted")));
			return;
		}

		Map map = (Map )mcd.getReturnObject();

		mapView.getMap().removeMap(map);
		mapFrame.getContext().getDispatcher().notify(new MapEvent(
				mapFrame.getMapView(),
				MapEvent.MAP_VIEW_CHANGED));

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
