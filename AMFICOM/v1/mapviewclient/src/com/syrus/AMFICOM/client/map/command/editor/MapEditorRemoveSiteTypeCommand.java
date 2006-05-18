/*-
 * $$Id: MapEditorRemoveSiteTypeCommand.java,v 1.17 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.editor;

import java.util.Collection;
import java.util.Set;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedComboChooserDialog;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorRemoveSiteTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorRemoveSiteTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
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
		Collection<SiteNodeType> topologicalNodeTypes = NodeTypeController.getTopologicalNodeTypes(mapFrame.getMapView().getMap());
		topologicalNodeTypes.removeAll(MapLibraryController.getDefaultMapLibrary().getSiteNodeTypes());
		SiteNodeType siteNodeType = (SiteNodeType )WrapperedComboChooserDialog.showChooserDialog(
				topologicalNodeTypes);

		if(siteNodeType == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		try {
			LinkedIdsCondition condition = new LinkedIdsCondition(siteNodeType.getId(), ObjectEntities.SITENODE_CODE);
			final Set<SiteNode> siteNodes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(siteNodes.isEmpty()) {
				StorableObjectPool.delete(siteNodeType.getId());
				StorableObjectPool.flush(siteNodeType, LoginManager.getUserId(), true);
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(
								this, 
								MapEvent.LIBRARY_SET_CHANGED, 
								mapFrame.getMapView().getMap().getMapLibraries()));
				setResult(Command.RESULT_OK);
			}
			else {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(), 
						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + siteNodes.iterator().next().getName(), 
						I18N.getString(MapEditorResourceKeys.ERROR), 
						JOptionPane.ERROR_MESSAGE);
				this.aContext.getDispatcher().firePropertyChange(
						new StatusMessageEvent(
								this,
								StatusMessageEvent.STATUS_MESSAGE,
								I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE)));
				setResult(Command.RESULT_NO);
			}
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
	}
}
