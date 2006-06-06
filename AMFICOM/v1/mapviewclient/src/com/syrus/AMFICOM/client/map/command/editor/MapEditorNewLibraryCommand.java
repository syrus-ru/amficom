/*-
 * $$Id: MapEditorNewLibraryCommand.java,v 1.17 2006/06/06 13:07:03 stas Exp $$
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
import com.syrus.AMFICOM.client.map.props.MapLibraryEditor;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2006/06/06 13:07:03 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorNewLibraryCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	public MapEditorNewLibraryCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(mapFrame == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this, 
							StatusMessageEvent.STATUS_MESSAGE, 
							I18N.getString(MapEditorResourceKeys.MESSAGE_OPEN_MAP_FRAME_FIRST)));
			return;
		}

		try {
			MapLibrary mapLibrary = MapLibrary.createInstance(
					LoginManager.getUserId(),
					I18N.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING,
					MapEditorResourceKeys.EMPTY_STRING,
					null);
			
			MapLibraryEditor mapLibraryEditor = new MapLibraryEditor<MapLibrary>();
			if(EditorDialog.showEditorDialog(
					I18N.getString(MapEditorResourceKeys.ENTITY_SITE_NODE_TYPE),
					mapLibrary,
					mapLibraryEditor) ) {
				StorableObjectPool.flush(mapLibrary, LoginManager.getUserId(), true);
				Map map = mapFrame.getMapView().getMap();
				map.addMapLibrary(mapLibrary);
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(
							this, 
							MapEvent.LIBRARY_SET_CHANGED,
							map.getMapLibraries()));
				setResult(Command.RESULT_OK);
			} else {
				StorableObjectPool.delete(mapLibrary.getId());
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
