/**
 * $Id: MapEditorOpenLibraryCommand.java,v 1.4 2005/08/17 14:14:17 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Collection;

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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;

public class MapEditorOpenLibraryCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;
	private MapLibrary mapLibrary;

	public MapEditorOpenLibraryCommand(JDesktopPane pane, ApplicationContext context) {
		this.desktop = pane;
		this.aContext = context;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		MapLibraryTableController mapLibraryTableController = MapLibraryTableController.getInstance();

		Collection allLibraries;
		try {
			StorableObjectCondition condition = new EquivalentCondition(
					ObjectEntities.MAPLIBRARY_CODE);
			allLibraries = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		} catch(CommunicationException e) {
			e.printStackTrace();
			return;
		} catch(DatabaseException e) {
			e.printStackTrace();
			return;
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}
		
		this.mapLibrary = (MapLibrary )WrapperedTableChooserDialog.showChooserDialog(
				LangModelMap.getString("MapLibrary"),
				allLibraries,
				mapLibraryTableController,
				mapLibraryTableController.getKeysArray(),
				true);

		if(this.mapLibrary == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
			return;
		}

		Map map = mapFrame.getMapView().getMap();
		map.addMapLibrary(this.mapLibrary);
		this.aContext.getDispatcher().firePropertyChange(
				new MapEvent(
					this, 
					MapEvent.LIBRARY_SET_CHANGED,
					map.getMapLibraries()));
		setResult(Command.RESULT_OK);
	}
}
