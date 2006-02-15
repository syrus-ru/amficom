/*-
 * $$Id: MapOpenCommand.java,v 1.37 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.dialogs.WrapperedTableChooserDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.MapRemoveWrapper;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapTableController;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.util.Log;

/**
 * открыть карту. карта открывается в новом виде
 * 
 * @version $Revision: 1.37 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapOpenCommand extends AbstractCommand {
	ApplicationContext aContext;

	JDesktopPane desktop;

	protected Map map;

	protected boolean canDelete = false;

	public MapOpenCommand(JDesktopPane desktop, ApplicationContext aContext) {
		this.desktop = desktop;
		this.aContext = aContext;
	}

	public void setCanDelete(boolean flag) {
		this.canDelete = flag;
	}

	public Map getMap() {
		return this.map;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktop);
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_MAP_OPENING)));

		Collection maps;
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAP_CODE);
			maps = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
		} catch(CommunicationException e) {
			Log.errorMessage(e);
			return;
		} catch(DatabaseException e) {
			Log.errorMessage(e);
			return;
		} catch(ApplicationException e) {
			Log.errorMessage(e);
			return;
		}

		MapTableController mapTableController = MapTableController.getInstance();

		this.map = (Map )WrapperedTableChooserDialog.showChooserDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP),
				maps,
				mapTableController,
				mapTableController.getKeysArray(),
				MapRemoveWrapper.getInstance(),
				true);

		if(this.map == null) {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
			return;
		}

		this.map.open();

		final MapViewController mapViewController = mapFrame.getMapViewer().getLogicalNetLayer().getMapViewController();
		MarkController controller = null;
		for(Mark mark : this.map.getMarks()) {
			if(controller == null) {
				controller = (MarkController) mapViewController.getController(mark);
			}
			try {
				controller.moveToFromStartLt(mark, mark.getDistance());
			} catch(MapException e) {
				Log.errorMessage(e);
			}
		}
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString("Finished"))); //$NON-NLS-1$
		setResult(Command.RESULT_OK);
	}

}
