/*-
 * $$Id: MapEditorNewSiteTypeCommand.java,v 1.13 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorNewSiteTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorNewSiteTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		try {
			MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
			if(mapFrame == null) {
				this.aContext.getDispatcher().firePropertyChange(
						new StatusMessageEvent(
								this, 
								StatusMessageEvent.STATUS_MESSAGE, 
								I18N.getString(MapEditorResourceKeys.MESSAGE_OPEN_MAP_FRAME_FIRST)));
				return;
			}
			SiteNodeType siteNodeType = SiteNodeType.createInstance(
					LoginManager.getUserId(), 
					SiteNodeTypeSort.BUILDING, 
					"codename", //$NON-NLS-1$
					I18N.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING,
					NodeTypeController.getDefaultImageId(),
					true,
					MapLibraryController.getDefaultMapLibrary().getId());
			siteNodeType.setCodename(siteNodeType.getId().toString());
			SiteNodeTypeEditor siteNodeTypeEditor = new SiteNodeTypeEditor();
			siteNodeTypeEditor.setNetMapViewer(mapFrame.getMapViewer());
			if(EditorDialog.showEditorDialog(
					I18N.getString(MapEditorResourceKeys.ENTITY_SITE_NODE_TYPE),
					siteNodeType,
					siteNodeTypeEditor) ) {
				StorableObjectPool.flush(siteNodeType, LoginManager.getUserId(), true);
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(
								this, 
								MapEvent.LIBRARY_SET_CHANGED, 
								mapFrame.getMapView().getMap().getMapLibraries()));
				setResult(Command.RESULT_OK);
			} else {
				StorableObjectPool.delete(siteNodeType.getId());
				setResult(Command.RESULT_CANCEL);
			}
		} catch(CreateObjectException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		} catch(ApplicationException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		}
	}
}
