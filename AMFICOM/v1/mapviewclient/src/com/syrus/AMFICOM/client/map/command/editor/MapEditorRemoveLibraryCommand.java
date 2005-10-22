/*-
 * $$Id: MapEditorRemoveLibraryCommand.java,v 1.12 2005/10/22 13:50:27 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Set;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapLibraryTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;

/**
 * @version $Revision: 1.12 $, $Date: 2005/10/22 13:50:27 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorRemoveLibraryCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorRemoveLibraryCommand(JDesktopPane desktop, ApplicationContext aContext) {
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
							I18N.getString(MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION)));
			return;
		}
		MapLibraryTableController mapLibraryTableController = MapLibraryTableController.getInstance();

		StorableObjectCondition libcondition = new EquivalentCondition(ObjectEntities.MAPLIBRARY_CODE);
		final Set<StorableObject> libraries;
		try {
			libraries = StorableObjectPool.getStorableObjectsByCondition(libcondition, true);
		} catch(ApplicationException e1) {
			e1.printStackTrace();
			return;
		}

		MapLibrary mapLibrary = (MapLibrary )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_LIBRARY),
				libraries,
				mapLibraryTableController,
				mapLibraryTableController.getKeysArray(),
				null,
				false);

		if(mapLibrary == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		try {
			LinkedIdsCondition condition = new LinkedIdsCondition(mapLibrary.getId(), ObjectEntities.MAP_CODE);
			final Set<Map> maps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(maps.isEmpty()) {
				StorableObjectPool.delete(mapLibrary.getId());
				StorableObjectPool.flush(mapLibrary, LoginManager.getUserId(), true);
				setResult(Command.RESULT_OK);
			}
			else {
				this.aContext.getDispatcher().firePropertyChange(
						new StatusMessageEvent(
								this,
								StatusMessageEvent.STATUS_MESSAGE,
								I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE)));
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(), 
						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + maps.iterator().next().getName(), 
						I18N.getString(MapEditorResourceKeys.ERROR), 
						JOptionPane.ERROR_MESSAGE);
				setResult(Command.RESULT_NO);
			}
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
}
