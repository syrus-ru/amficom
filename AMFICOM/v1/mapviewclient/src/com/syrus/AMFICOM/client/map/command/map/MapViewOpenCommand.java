/*-
 * $$Id: MapViewOpenCommand.java,v 1.37 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.DefaultStorableObjectRemoveWrapper;
import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.ui.MapViewTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 * открыть вид
 *  
 * @version $Revision: 1.37 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewOpenCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	protected MapView mapView;

	protected boolean canDelete = false;

	public MapViewOpenCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag) {
		this.canDelete = flag;
	}

	public MapView getMapView() {
		return this.mapView;
	}

	@Override
	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_MAP_VIEW_OPENING)));

		Collection mapViews;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAPVIEW_CODE);
			mapViews = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
		} catch(CommunicationException e) {
			Log.errorMessage(e);
			return;
		} catch(DatabaseException e) {
			Log.errorMessage(e);
			return;
		} catch(ApplicationException e) {
			Log.errorMessage(e);
			return;
		}

		MapViewTableController mapViewTableController = 
			MapViewTableController.getInstance();

		this.mapView = (MapView )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW),
				mapViews,
				mapViewTableController,
				mapViewTableController.getKeysArray(),
				DefaultStorableObjectRemoveWrapper.getInstance(),
				true);

		if(this.mapView == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

//		try {
			this.mapView.getMap().open();
//			for(Scheme scheme : this.mapView.getSchemes()) {
//				MapViewOpenCommand.openScheme(scheme);
//			}
//		} catch(ApplicationException e) {
//			Log.errorMessage(e);
//			return;
//		}
		setResult(Command.RESULT_OK);

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Finished"))); //$NON-NLS-1$
	}

	// TODO think of moving this method to 'Scheme'
	public static void openScheme(Scheme scheme) throws ApplicationException {
		Set<Identifiable> reverseDependencies = scheme.getReverseDependencies(true);

		Map<Short, Set<Identifier>> objectsToLoad = new HashMap<Short, Set<Identifier>>();

		for(Identifiable identifiable : reverseDependencies) {
			Short major = Short.valueOf(identifiable.getId().getMajor());
			Set<Identifier> identifierSet = objectsToLoad.get(major);
			if(identifierSet == null) {
				identifierSet = new HashSet<Identifier>();
			}
			identifierSet.add(identifiable.getId());
		}

		for(Short major : objectsToLoad.keySet()) {
			Set<Identifier> identifierSet = objectsToLoad.get(major);
			StorableObjectPool.getStorableObjects(identifierSet, true);
		}
	}
}
