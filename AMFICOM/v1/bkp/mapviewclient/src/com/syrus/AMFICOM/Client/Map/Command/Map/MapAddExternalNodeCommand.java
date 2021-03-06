/**
 * $Id: MapAddExternalNodeCommand.java,v 1.5 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ??????? ?????????????????? ???????????????????
 *         ???????????????? ???????? ?????????? ???????????
 *
 * ?????????: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.UI.ExternalMapElementChooserDialog;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapTableController;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ???????? ? ??? ????? ?? ??????
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class MapAddExternalNodeCommand extends AbstractCommand
{
	JDesktopPane desktop;
	ApplicationContext aContext;

	protected Map map;
	protected SiteNode node;

	public MapAddExternalNodeCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public Map getMap() {
		return this.map;
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

		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAP_ENTITY_CODE);
			Collection ss = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
			ss.remove(mapView.getMap());
			mapChooserDialog.setContents(ss);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		mapChooserDialog.setModal(true);
		mapChooserDialog.setVisible(true);
		if(mapChooserDialog.getReturnCode() == WrapperedTableChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		if(mapChooserDialog.getReturnCode() != WrapperedTableChooserDialog.RET_OK)
			return;

		this.map = (Map )mapChooserDialog.getReturnObject();

		ExternalMapElementChooserDialog elemengChooserDialog = 
			new ExternalMapElementChooserDialog(
				this.map,
				LangModelMap.getString("SiteNode"));

		elemengChooserDialog.setModal(true);
		elemengChooserDialog.setVisible(true);
		if(elemengChooserDialog.getReturnCode() == ExternalMapElementChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			return;
		}

		if(elemengChooserDialog.getReturnCode() != ExternalMapElementChooserDialog.RET_OK)
			return;

		this.node = elemengChooserDialog.getReturnObject();

		SiteNodeController siteNodeController = (SiteNodeController )mapFrame
				.getMapViewer().getLogicalNetLayer().getMapViewController()
				.getController(this.node);

		siteNodeController.updateScaleCoefficient(this.node);
		mapView.getMap().addExternalNode(this.node);

		this.aContext.getDispatcher().firePropertyChange(
				new MapEvent(mapView, MapEvent.MAP_CHANGED));
		this.aContext.getDispatcher().firePropertyChange(
				new MapEvent(mapView, MapEvent.NEED_REPAINT));

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("Finished")));
		setResult(Command.RESULT_OK);
	}

}
