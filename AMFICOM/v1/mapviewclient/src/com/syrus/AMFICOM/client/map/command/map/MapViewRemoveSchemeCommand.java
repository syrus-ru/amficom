/**
 * $Id: MapViewRemoveSchemeCommand.java,v 1.8 2005/06/06 12:20:31 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.map;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.SchemeTableController;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * убрать из вида выбранную схему 
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class MapViewRemoveSchemeCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	public MapViewRemoveSchemeCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null)
			return;

		MapViewController controller = mapFrame.getMapViewer()
				.getLogicalNetLayer().getMapViewController();

		MapView mapView = mapFrame.getMapView();

		if(mapView == null)
			return;

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapOpening")));

		SchemeTableController schemeTableController = 
			SchemeTableController.getInstance();

		WrapperedTableChooserDialog schemeChooserDialog = new WrapperedTableChooserDialog(
				LangModelMap.getString("Scheme"),
				schemeTableController,
				schemeTableController.getKeysArray());

		schemeChooserDialog.setContents(mapView.getSchemes());

		schemeChooserDialog.setModal(true);
		schemeChooserDialog.setVisible(true);
		if(schemeChooserDialog.getReturnCode() != WrapperedTableChooserDialog.RET_OK) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		Scheme scheme = (Scheme )schemeChooserDialog.getReturnObject();

		controller.removeScheme(scheme);
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
