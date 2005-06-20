/**
 * $Id: MapRemoveMapCommand.java,v 1.5 2005/06/20 15:30:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapTableController;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;

import javax.swing.JDesktopPane;

/**
 * убрать из вида выбранную схему 
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/06/20 15:30:56 $
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

		Map map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("Map"),
				mapView.getMap().getMaps(),
				mapTableController,
				mapTableController.getKeysArray(),
				true);

		if(map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

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
