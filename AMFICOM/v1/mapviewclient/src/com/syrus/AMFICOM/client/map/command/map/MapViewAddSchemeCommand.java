/**
 * $Id: MapViewAddSchemeCommand.java,v 1.15 2005/06/20 15:30:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.SchemeTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * добавить в вид схему из списка
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/06/20 15:30:56 $
 * @module mapviewclient_v1
 */
public class MapViewAddSchemeCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	protected Scheme scheme;

	public MapViewAddSchemeCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Scheme getScheme() {
		return this.scheme;
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

		Set schemes;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.SCHEME_CODE);
			schemes = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		this.scheme = (Scheme )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("Scheme"),
				schemes,
				schemeTableController,
				schemeTableController.getKeysArray(),
				true);

		if(this.scheme == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		if(!mapView.getSchemes().contains(this.scheme)) {
			controller.addScheme(this.scheme);
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(mapView, MapEvent.MAP_VIEW_CHANGED));
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(mapView, MapEvent.NEED_REPAINT));
		}
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}
}
