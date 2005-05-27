/**
 * $Id: MapRemoveMapCommand.java,v 1.2 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapTableController;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

import javax.swing.JDesktopPane;

/**
 * убрать из вида выбранную схему 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class MapRemoveMapCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	public MapRemoveMapCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		MapView mapView = mapFrame.getMapView();

		if(mapView == null)
			return;

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening")));

		MapTableController mapTableController = MapTableController.getInstance();

		WrapperedTableChooserDialog mapChooserDialog = new WrapperedTableChooserDialog(
				LangModelMap.getString("Map"),
				mapTableController,
				mapTableController.getKeysArray());

		mapChooserDialog.setContents(mapView.getMap().getMaps());

		mapChooserDialog.setModal(true);
		mapChooserDialog.setVisible(true);
		if(mapChooserDialog.getReturnCode() != WrapperedTableChooserDialog.RET_OK) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		Map map = (Map )mapChooserDialog.getReturnObject();

		mapView.getMap().removeMap(map);
		mapFrame.getContext().getDispatcher().firePropertyChange(
				new MapEvent(mapFrame.getMapView(), MapEvent.MAP_VIEW_CHANGED));

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
