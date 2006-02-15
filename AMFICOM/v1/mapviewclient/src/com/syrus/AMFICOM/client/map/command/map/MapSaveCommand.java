/*-
 * $$Id: MapSaveCommand.java,v 1.26 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.util.Log;

/**
 * Класс используется для сохранения топологической схемы на сервере
 * 
 * @version $Revision: 1.26 $, $Date: 2006/02/15 11:12:43 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapSaveCommand extends AbstractCommand {
	Map map;

	ApplicationContext aContext;

	public MapSaveCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						I18N.getString(MapEditorResourceKeys.STATUS_MAP_SAVING)));

		if(EditorDialog.showEditorDialog(
				I18N.getString(MapEditorResourceKeys.TITLE_MAP_PROPERTIES),
				this.map, 
				MapVisualManager.getInstance().getGeneralPropertiesPanel())) {
// aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					true));
			try {
				StorableObjectPool.putStorableObject(this.map);
			} catch(IllegalObjectEntityException e) {
				Log.errorMessage(e);
			}
			try {
				// save map
				StorableObjectPool.flush(this.map, LoginManager.getUserId(), true);
				LocalXmlIdentifierPool.flush();
			} catch(ApplicationException e) {
				Log.errorMessage(e);
			}
// aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					false));
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

}
