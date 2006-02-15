/*-
 * $$Id: MapViewSaveAsCommand.java,v 1.31 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.util.Log;

/**
 * Класс используется для сохранения топологической схемы с новым
 * именем
 * 
 * @version $Revision: 1.31 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewSaveAsCommand extends AbstractCommand {
	MapView mapView;

	MapView newMapView;

	ApplicationContext aContext;

	public MapViewSaveAsCommand(MapView mapView, ApplicationContext aContext) {
		this.mapView = mapView;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		try {
			this.newMapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
					userId,
					domainId,
					I18N.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING,
					0.0D,
					0.0D,
					1.0D,
					1.0D,
					this.mapView.getMap());

			StorableObjectPool.putStorableObject(this.newMapView);

			this.newMapView.setName(this.mapView.getName() + I18N.getString(MapEditorResourceKeys.IS_ACOPY_IN_PARENTHESIS));
		} catch(CreateObjectException e) {
			Log.errorMessage(e);
			return;
		} catch(IllegalObjectEntityException e) {
			Log.errorMessage(e);
			setResult(RESULT_NO);
			return;
		}

		if(EditorDialog.showEditorDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW_PROPERTIES),
				this.newMapView,
				MapViewVisualManager.getInstance().getGeneralPropertiesPanel())) {
// try
// {
// newMapView = (MapView )mapView.clone();
//			}
//			catch(CloneNotSupportedException e)
//			{
//				return;
//			}
/*
			if(!mc2.scheme_id.equals(mc.scheme_id))
			{
				Pool.removeHash(MapPropertiesManager.MAP_CLONED_IDS);
			}
*/
			this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
					this,
					StatusMessageEvent.STATUS_MESSAGE,
					I18N.getString(MapEditorResourceKeys.STATUS_MAP_VIEW_SAVING)));
			try {
				// save mapview
				StorableObjectPool.flush(this.newMapView, userId, true);
				LocalXmlIdentifierPool.flush();
			} catch(ApplicationException e) {
				Log.errorMessage(e);
			}

			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Finished"))); //$NON-NLS-1$
			setResult(Command.RESULT_OK);
		}
		else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
		}
	}

	public MapView getMapView() {
		return this.mapView;
	}

	public MapView getNewMapView() {
		return this.newMapView;
	}

}
