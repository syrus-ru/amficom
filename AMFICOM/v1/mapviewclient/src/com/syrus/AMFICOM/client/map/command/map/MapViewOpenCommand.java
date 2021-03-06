/*-
 * $$Id: MapViewOpenCommand.java,v 1.41 2006/06/29 08:39:39 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
import com.syrus.AMFICOM.client.util.SynchronousWorker;
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

/**
 * ??????? ???
 *  
 * @version $Revision: 1.41 $, $Date: 2006/06/29 08:39:39 $
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
		
		final Map map = this.mapView.getMap();
		SynchronousWorker<Map> worker = new SynchronousWorker<Map>(null, 
				I18N.getString("Message.Information.please_wait"), 
				I18N.getString("Message.Information.load_mapview"), true) {
			@Override
			public Map construct() throws Exception {
				map.open();
				return map;
			}
		};
		try {
			worker.execute();
		} catch (ExecutionException e1) {
			Log.errorMessage(e1);
		}

		/*
		 * The following block is turned off. 
		 */
		if (false) {
			try {
				for (final Scheme scheme : this.mapView.getSchemes()) {
					MapViewOpenCommand.openScheme(scheme);
				}
			} catch (final ApplicationException ae) {
				Log.errorMessage(ae);
				return;
			}
		}

		setResult(Command.RESULT_OK);

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Finished"))); //$NON-NLS-1$
	}

	// TODO think of moving this method to 'Scheme'
	public static void openScheme(@SuppressWarnings("unused") final Scheme scheme)
	throws ApplicationException {
		if (true) {
			throw new UnsupportedOperationException();
		}

		/*
		 * The code below is doubly useless.
		 * 
		 * 1. Its author (venerable Andrey Kroupennikov) meant it to
		 * "preload" a scheme - lock, stock and barrel. All the job
		 * would have been nicely done just by invocation of
		 * #getReverseDependencies(true); the hierarchy would have been
		 * traversed completely but maybe the farthest leaves. There's
		 * no use to additionally sort the objects and invoke
		 * StorableObjectPool#getStorableObjects(...).
		 *
		 * 2. The code isn't used anywhere. There're two blocks that
		 * reference it, but these blocks are turned off
		 * by "if (false) {...}" clause.
		 *
		 * Finally, #getReverseDependencies(boolean) is no longer
		 * visible: it has protected access in class StorableObject.
		 */

		final java.util.Map<Short, Set<Identifier>> objectsToLoad = new HashMap<Short, Set<Identifier>>();

//		final Set<Identifiable> reverseDependencies = scheme.getReverseDependencies(true);
		final Set<Identifiable> reverseDependencies = Collections.emptySet();
		for (final Identifiable reverseDependency : reverseDependencies) {
			final Identifier id = reverseDependency.getId();
			final Short entityCode = Short.valueOf(id.getMajor());
			Set<Identifier> ids = objectsToLoad.get(entityCode);
			if (ids == null) {
				ids = new HashSet<Identifier>();
				objectsToLoad.put(entityCode, ids);
			}
			ids.add(id);
		}

		for (final Set<Identifier> ids: objectsToLoad.values()) {
			StorableObjectPool.getStorableObjects(ids, true);
		}
	}
}
