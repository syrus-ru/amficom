/*-
 * $$Id: MapSaveAsCommand.java,v 1.32 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapSaveAsCommand.java,v $ используется для сохранения 
 * топологической схемы с новым именем
 * 
 * @version $Revision: 1.32 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapSaveAsCommand extends AbstractCommand {
	Map map;

	Map newMap;

	ApplicationContext aContext;

	public MapSaveAsCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		try {
			this.newMap = Map.createInstance(
					userId, 
					domainId, 
					this.map.getName() + I18N.getString(MapEditorResourceKeys.IS_ACOPY_IN_PARENTHESIS),
					MapEditorResourceKeys.EMPTY_STRING);
			this.newMap.addMapLibrary(MapLibraryController.getDefaultMapLibrary());
		} catch(CreateObjectException e) {
			Log.errorMessage(e);
			return;
		}

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_MAP_SAVING)));

		if(EditorDialog.showEditorDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_PROPERTIES),
				this.newMap,
				MapVisualManager.getInstance().getGeneralPropertiesPanel())) {
// try
// {
//				newMap = (Map )map.clone();
//			}
//			catch(CloneNotSupportedException e)
//			{
//				return;
//			}
			try {
				StorableObjectPool.putStorableObject(this.newMap);
			} catch(IllegalObjectEntityException e) {
				Log.errorMessage(e);
			}
			try {
				// save newMap
				StorableObjectPool.flush(this.newMap, userId, true);
				LocalXmlIdentifierPool.flush();
			} catch(ApplicationException e) {
				Log.errorMessage(e);
			}

			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Finished"))); //$NON-NLS-1$
			setResult(Command.RESULT_OK);
		}
		else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Aborted"))); //$NON-NLS-1$
			setResult(Command.RESULT_CANCEL);
		}
	}

	public Map getMap() {
		return this.map;
	}

	public Map getNewMap() {
		return this.newMap;
	}

}
