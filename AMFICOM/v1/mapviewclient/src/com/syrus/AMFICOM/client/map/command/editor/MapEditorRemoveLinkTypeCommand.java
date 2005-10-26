/*-
 * $$Id: MapEditorRemoveLinkTypeCommand.java,v 1.15 2005/10/26 14:17:34 krupenn Exp $$
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
import com.syrus.AMFICOM.client.map.controllers.LinkTypeController;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 * @version $Revision: 1.15 $, $Date: 2005/10/26 14:17:34 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorRemoveLinkTypeCommand extends AbstractCommand {

	private final JDesktopPane desktop;
	private final ApplicationContext aContext;

	public MapEditorRemoveLinkTypeCommand(JDesktopPane desktop, ApplicationContext aContext) {
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
		Collection<PhysicalLinkType> topologicalLinkTypes = LinkTypeController.getTopologicalLinkTypes(mapFrame.getMap());
		topologicalLinkTypes.removeAll(MapLibraryController.getDefaultMapLibrary().getPhysicalLinkTypes());
		PhysicalLinkType physicalLinkType = (PhysicalLinkType )WrapperedComboChooserDialog.showChooserDialog(
				topologicalLinkTypes);

		if(physicalLinkType == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		try {
			LinkedIdsCondition condition = new LinkedIdsCondition(physicalLinkType.getId(), ObjectEntities.PHYSICALLINK_CODE);
			final Set<PhysicalLink> physicalLinks = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(physicalLinks.isEmpty()) {
				StorableObjectPool.delete(physicalLinkType.getId());
				StorableObjectPool.flush(physicalLinkType, LoginManager.getUserId(), true);
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(
								this, 
								MapEvent.LIBRARY_SET_CHANGED, 
								mapFrame.getMapView().getMap().getMapLibraries()));
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
						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + physicalLinks.iterator().next().getName(), 
						I18N.getString(MapEditorResourceKeys.ERROR), 
						JOptionPane.ERROR_MESSAGE);
				setResult(Command.RESULT_NO);
			}
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
}
