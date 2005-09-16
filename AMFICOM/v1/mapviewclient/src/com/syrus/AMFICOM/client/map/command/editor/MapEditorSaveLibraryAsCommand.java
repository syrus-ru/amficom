/**
 * $Id: MapEditorSaveLibraryAsCommand.java,v 1.5 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;

public class MapEditorSaveLibraryAsCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorSaveLibraryAsCommand(JDesktopPane desktop, ApplicationContext aContext) {
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
							LangModelMap.getString("StatusMessage.OpenMapFrameFirst"))); //$NON-NLS-1$
			return;
		}

		Map map = mapFrame.getMapView().getMap();

		Set<MapLibrary> mapLibraries = map.getMapLibraries();

		Identifier userId = LoginManager.getUserId();
		try {
			for(Iterator iter = mapLibraries.iterator(); iter.hasNext();) {
				MapLibrary mapLibrary = (MapLibrary )iter.next();
				StorableObjectPool.flush(mapLibrary, userId, true);
			}
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
}
