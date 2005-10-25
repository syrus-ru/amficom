/*-
 * $$Id: MapEditorNewLinkTypeCommand.java,v 1.16 2005/10/25 08:01:48 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkTypeEditor;
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
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/10/25 08:01:48 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorNewLinkTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorNewLinkTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
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
								I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
				return;
			}
			PhysicalLinkType physicalLinkType = PhysicalLinkType.createInstance(
					LoginManager.getUserId(), 
					PhysicalLinkTypeSort.TUNNEL, 
					"codename", //$NON-NLS-1$
					I18N.getString(MapEditorResourceKeys.VALUE_NEW),
					MapEditorResourceKeys.EMPTY_STRING,
					new IntDimension(1, 1),
					true,
					MapLibraryController.getDefaultMapLibrary().getId());
			physicalLinkType.setCodename(physicalLinkType.getId().toString());
			PhysicalLinkTypeEditor physicalLinkTypeEditor = new PhysicalLinkTypeEditor();
			physicalLinkTypeEditor.setNetMapViewer(mapFrame.getMapViewer());
			if(EditorDialog.showEditorDialog(
					I18N.getString(MapEditorResourceKeys.ENTITY_PHYSICAL_LINK_TYPE),
					physicalLinkType,
					physicalLinkTypeEditor) ) {
				StorableObjectPool.flush(physicalLinkType, LoginManager.getUserId(), true);
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(
								this, 
								MapEvent.LIBRARY_SET_CHANGED, 
								mapFrame.getMapView().getMap().getMapLibraries()));
				setResult(Command.RESULT_OK);
			} else {
				StorableObjectPool.delete(physicalLinkType.getId());
				setResult(Command.RESULT_CANCEL);
			}
		} catch(CreateObjectException e) {
			Log.debugException(e, Level.SEVERE);
			setResult(Command.RESULT_NO);
		} catch(ApplicationException e) {
			Log.debugException(e, Level.SEVERE);
			setResult(Command.RESULT_NO);
		}
	}
}
