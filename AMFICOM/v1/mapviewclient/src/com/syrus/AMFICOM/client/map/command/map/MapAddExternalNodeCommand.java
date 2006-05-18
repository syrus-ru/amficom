/*-
 * $$Id: MapAddExternalNodeCommand.java,v 1.22 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.SiteNodeController;
import com.syrus.AMFICOM.client.map.ui.ExternalMapElementChooserDialog;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
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
import com.syrus.util.Log;

/**
 * добавить в вид схему из списка
 * 
 * @version $Revision: 1.22 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapAddExternalNodeCommand extends AbstractCommand {
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

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(mapFrame == null) {
			return;
		}

		MapView mapView = mapFrame.getMapView();

		if(mapView == null) {
			return;
		}

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_ADDING_EXTERNAL_NODE)));

		MapTableController mapTableController = MapTableController.getInstance();

		Collection availableMaps;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAP_CODE);
			availableMaps = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
			availableMaps.remove(mapView.getMap());
		} catch(ApplicationException e) {
			Log.errorMessage(e);
			return;
		}

		this.map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP),
				availableMaps,
				mapTableController,
				mapTableController.getKeysArray(),
				null);

		if(this.map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			return;
		}

		ExternalMapElementChooserDialog elemengChooserDialog = 
			new ExternalMapElementChooserDialog(
				this.map,
				I18N.getString(MapEditorResourceKeys.TREE_EXTERNAL_NODE));

		elemengChooserDialog.setModal(true);
		elemengChooserDialog.setVisible(true);
		if(elemengChooserDialog.getReturnCode() == ExternalMapElementChooserDialog.RET_CANCEL) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
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
				new MapEvent(this, MapEvent.MAP_CHANGED, mapView));

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Finished"))); //$NON-NLS-1$
		setResult(Command.RESULT_OK);
	}

}
