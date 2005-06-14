/*
 * $Id: MapSaveCommand.java,v 1.16 2005/06/14 10:11:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

/**
 * Класс используется для сохранения топологической схемы на сервере
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/06/14 10:11:17 $
 * @module mapviewclient_v1
 */
public class MapSaveCommand extends AbstractCommand {
	Map map;

	ApplicationContext aContext;

	public MapSaveCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	public void execute() {
		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapSaving")));

		if(EditorDialog.showEditorDialog(
				LangModelMap.getString("MapProperties"), 
				this.map, 
				MapVisualManager.getInstance().getGeneralPropertiesPanel())) {
// aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					true));
			try {
				StorableObjectPool.putStorableObject(this.map);
			} catch(IllegalObjectEntityException e) {
				e.printStackTrace();
			}
			try {
				// save map
				StorableObjectPool.flush(this.map.getId(), true);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}
// aContext.getDispatcher().notify(new StatusMessageEvent(
//					StatusMessageEvent.STATUS_PROGRESS_BAR, 
//					false));
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Finished")));
			setResult(Command.RESULT_OK);
		}
		else {
			this.aContext.getDispatcher().firePropertyChange(
					new StatusMessageEvent(
							this,
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("Aborted")));
			setResult(Command.RESULT_CANCEL);
		}
	}

}
