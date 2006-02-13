/*-
 * $$Id: MapViewSaveCommand.java,v 1.39 2006/02/13 08:39:53 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeSaveAllCommand;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Класс используется для сохранения топологической схемы на сервере
 * 
 * @version $Revision: 1.39 $, $Date: 2006/02/13 08:39:53 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewSaveCommand extends AbstractCommand {
	MapView mapView;
	ApplicationContext aContext;

	public MapViewSaveCommand(MapView mapView, ApplicationContext aContext) {
		this.mapView = mapView;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		if(!this.mapView.getUnboundNodes().isEmpty()) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(
									MapEditorResourceKeys.ERROR_UNBOUND_ELEMENTS_EXIST)));
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					I18N.getString(MapEditorResourceKeys.ERROR_UNBOUND_ELEMENTS_EXIST) + " - " + this.mapView.getUnboundNodes().iterator().next().getName(), 
					I18N.getString(MapEditorResourceKeys.ERROR), 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(EditorDialog.showEditorDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_VIEW_PROPERTIES),
				this.mapView,
				MapViewVisualManager.getInstance().getGeneralPropertiesPanel())) {
			this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(
								MapEditorResourceKeys.STATUS_MAP_VIEW_SAVING)));

			MapSaveCommand cmd = new MapSaveCommand(
					this.mapView.getMap(),
					this.aContext);
			cmd.execute();
			if(cmd.getResult() == Command.RESULT_CANCEL) {
				setResult(Command.RESULT_CANCEL);
				return;
			}

			Identifier userId = LoginManager.getUserId();

			/*
			for(Iterator it = this.mapView.getSchemes().iterator(); it.hasNext();) {
				Scheme scheme = (Scheme) it.next();
				scheme.setMap(this.mapView.getMap());
				try {
					Set<Scheme> internalSchemes = new HashSet<Scheme>();
					for (SchemeElement se : scheme.getSchemeElements(false)) {
						Scheme internal = se.getScheme(false);
						if (internal != null && internal.isChanged()) {
							internalSchemes.add(internal);
						}
					}
					StorableObjectPool.flush(scheme.getReverseDependencies(false), userId, false);
					for (Scheme changed : internalSchemes) {
						StorableObjectPool.flush(changed.getReverseDependencies(false), userId, false);
					}
				} catch(VersionCollisionException e) {
					e.printStackTrace();
				} catch(IllegalDataException e) {
					e.printStackTrace();
				} catch(CommunicationException e) {
					e.printStackTrace();
				} catch(DatabaseException e) {
					e.printStackTrace();
				} catch(ApplicationException e) {
					e.printStackTrace();
				}
			}*/

			// MapStorableObjectPool.putStorableObject(mapView);
			try {
				// save schemes
				SchemeSaveAllCommand.saveEntities();
				// save mapview
				StorableObjectPool.flush(this.mapView, userId, true);
				LocalXmlIdentifierPool.flush();
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Finished"))); //$NON-NLS-1$
			setResult(Command.RESULT_OK);
		} else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
		}
	}

}
