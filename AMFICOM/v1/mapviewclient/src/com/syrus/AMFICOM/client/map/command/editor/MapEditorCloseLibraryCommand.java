/*-
 * $$Id: MapEditorCloseLibraryCommand.java,v 1.1 2005/10/17 14:04:05 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Set;

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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * @version $Revision: 1.1 $, $Date: 2005/10/17 14:04:05 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorCloseLibraryCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorCloseLibraryCommand(JDesktopPane desktop, ApplicationContext aContext) {
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

		Map map = mapFrame.getMapView().getMap();

		MapLibrary mapLibrary = (MapLibrary )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_LIBRARY),
				map.getMapLibraries(),
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

		final Set<SiteNodeType> siteNodeTypes = mapLibrary.getSiteNodeTypes();
		boolean contains = false;
		for(SiteNode siteNode : map.getAllSiteNodes()) {
			if(siteNodeTypes.contains(siteNode.getType())) {
				contains = true;
				break;
			}
		}
		if(!contains) {
			final Set<PhysicalLinkType> physicalLinkTypes = mapLibrary.getPhysicalLinkTypes();
			for(PhysicalLink physicalLink : map.getAllPhysicalLinks()) {
				if(physicalLinkTypes.contains(physicalLink.getType())) {
					contains = true;
					break;
				}
			}
		}
		if(!contains) {
			map.removeMapLibrary(mapLibrary);
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(
						this, 
						MapEvent.LIBRARY_SET_CHANGED,
						map.getMapLibraries()));
			setResult(Command.RESULT_OK);
		}
		else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_CLOSE)));
			setResult(Command.RESULT_NO);
		}
	}
}
