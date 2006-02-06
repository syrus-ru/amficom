/*-
 * $$Id: MapEditorSaveViewCommand.java,v 1.17 2005/10/31 15:29:31 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewSaveCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.util.Log;

/**
 * Класс MapEditorSaveViewCommand используется для сохранения топологической
 * схемы в модуле "Редактор топологических схем". Использует команду
 * MapSaveCommand
 * 
 * @version $Revision: 1.17 $, $Date: 2005/10/31 15:29:31 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see MapViewSaveCommand
 */
public class MapEditorSaveViewCommand extends AbstractCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * @param aContext контекст модуля "Редактор топологических схем"
	 */
	public MapEditorSaveViewCommand(
			JDesktopPane desktop,
			ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		if(mapFrame == null) {
			Log.debugMessage("map frame is null! Cannot create new map.", Level.SEVERE); //$NON-NLS-1$
			setResult(Command.RESULT_NO);
			return;
		}
		if(MapPropertiesManager.isPermitted(PermissionCodename.MAP_EDITOR_SAVE_TOPOLOGICAL_VIEW)) {
			new MapViewSaveCommand(mapFrame.getMapView(), this.aContext).execute();
		}
		else {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(), 
					I18N.getString(MapEditorResourceKeys.ERROR_NO_PERMISSION), 
					I18N.getString(MapEditorResourceKeys.ERROR), 
					JOptionPane.ERROR_MESSAGE);
			setResult(Command.RESULT_NO);
		}

		setResult(Command.RESULT_OK);
	}

}
