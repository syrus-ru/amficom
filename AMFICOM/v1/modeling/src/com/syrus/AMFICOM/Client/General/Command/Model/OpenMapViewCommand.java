/*-
 * $Id: OpenMapViewCommand.java,v 1.4 2005/11/30 12:08:14 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Model;

import java.util.Set;
import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModel;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.model.MapApplicationModelFactory;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class OpenMapViewCommand extends AbstractCommand {
	ApplicationContext aContext;
	private Identifier schemeId;
	MapApplicationModelFactory factory;
	JDesktopPane desktop;

	
	public OpenMapViewCommand(
			JDesktopPane desktop, 
			ApplicationContext aContext, 
			MapApplicationModelFactory factory) {
		this.factory = factory;
		this.aContext = aContext;
		this.desktop = desktop;
	}

	@Override
	public void setParameter(String field, Object value) {
		if (field.equals("scheme_id")) {
			this.schemeId = (Identifier)value;
		}
	}

	@Override
	public void execute() {
		try {
			if (this.schemeId == null) {
				Log.debugMessage("No scheme selected", Level.WARNING);
				return;
			}
			
//			LinkedIdsCondition condition = new LinkedIdsCondition(this.schemeId, ObjectEntities.MAPVIEW_CODE);
//			Set<MapView> mapViews = StorableObjectPool.getStorableObjectsByCondition(condition, false);
//			if (mapViews.isEmpty()) {
//				Log.debugMessage("No linked map views with scheme " + this.schemeId, Level.WARNING);
//				return;
//			}
			Scheme scheme = StorableObjectPool.getStorableObject(this.schemeId, false);
			Map map = scheme.getMap();
			if (map == null) {
				Log.debugMessage("No linked maps with scheme " + this.schemeId, Level.WARNING);
				return;
			}
			
			LinkedIdsCondition condition = new LinkedIdsCondition(map.getId(), ObjectEntities.MAPVIEW_CODE);
			Set<MapView> mapViews = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (mapViews.isEmpty()) {
				Log.debugMessage("No linked map views with map " + map.getId(), Level.WARNING);
				return;
			}
			
			MapView mapView = mapViews.iterator().next();

			MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
			if(mapFrame == null) {
				ViewMapWindowCommand mapCommand = new ViewMapWindowCommand(
						this.desktop,
						this.aContext,
						this.factory);

				mapCommand.execute();
				mapFrame = mapCommand.getMapFrame();
				mapFrame.setName(MapFrame.NAME);
				
				final ApplicationModel aModel = this.aContext.getApplicationModel();
				aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
				aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_MAP, true);
				aModel.fireModelChanged("");
			}
			
			if(mapFrame == null) {
				return;
			}
			mapView.getMap().open();
			
			final MapViewController mapViewController = mapFrame.getMapViewer().getLogicalNetLayer().getMapViewController();
			MarkController controller = null;
			for(Mark mark : mapView.getMap().getMarks()) {
				if(controller == null) {
					controller = (MarkController) mapViewController.getController(mark);
				}
				try {
					controller.moveToFromStartLt(mark, mark.getDistance());
				} catch(MapException e) {
					e.printStackTrace();
				}
			}

			try {
				mapFrame.setMapView(mapView);

				mapFrame.getModel().getCommand(MapApplicationModel.MODE_NODES).execute();
				mapFrame.getModel().getCommand(MapApplicationModel.MODE_PATH).execute();

				setResult(Command.RESULT_OK);
			} catch(MapException e) {
				mapFrame.getContext().getDispatcher().firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
				e.printStackTrace();
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		} catch(RuntimeException ex) {
			Log.errorMessage(ex);
			setResult(Command.RESULT_NO);
		}
	}
}