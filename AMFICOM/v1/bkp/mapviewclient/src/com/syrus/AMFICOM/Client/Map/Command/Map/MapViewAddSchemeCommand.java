/**
 * $Id: MapViewAddSchemeCommand.java,v 1.11 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.SchemeTableController;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
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
 * @version $Revision: 1.11 $, $Date: 2005/05/27 15:14:56 $
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

		WrapperedTableChooserDialog schemeChooserDialog = new WrapperedTableChooserDialog(
				LangModelMap.getString("Scheme"),
				schemeTableController,
				schemeTableController.getKeysArray());

		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.SCHEME_ENTITY_CODE);
			Set schemes = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
			schemeChooserDialog.setContents(schemes);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		schemeChooserDialog.setModal(true);
		schemeChooserDialog.setVisible(true);
		if(schemeChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		if(schemeChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_OK) {
			this.scheme = (Scheme )schemeChooserDialog.getReturnObject();

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
		}
	}

}
