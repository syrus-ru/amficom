/**
 * $Id: MapEditorRemoveLibraryCommand.java,v 1.5 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapLibraryTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;

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
							LangModelMap.getString("MapException.ServerConnection"))); //$NON-NLS-1$
			return;
		}
		MapLibraryTableController mapLibraryTableController = MapLibraryTableController.getInstance();

		Map map = mapFrame.getMapView().getMap();

		MapLibrary mapLibrary = (MapLibrary )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("MapLibrary"),
				map.getMapLibraries(),
				mapLibraryTableController,
				mapLibraryTableController.getKeysArray(),
				true);

		if(mapLibrary == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		map.removeMapLibrary(mapLibrary);
		this.aContext.getDispatcher().firePropertyChange(
				new MapEvent(
					this, 
					MapEvent.LIBRARY_SET_CHANGED,
					map.getMapLibraries()));
		setResult(Command.RESULT_OK);
	}
}
