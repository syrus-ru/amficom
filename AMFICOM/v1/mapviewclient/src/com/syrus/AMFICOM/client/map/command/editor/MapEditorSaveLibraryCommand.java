/**
 * $Id: MapEditorSaveLibraryCommand.java,v 1.3 2005/08/17 14:14:18 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;

public class MapEditorSaveLibraryCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorSaveLibraryCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

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
