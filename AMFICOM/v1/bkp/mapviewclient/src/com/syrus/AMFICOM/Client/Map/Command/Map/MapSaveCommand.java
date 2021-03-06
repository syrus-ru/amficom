/*
 * $Id: MapSaveCommand.java,v 1.13 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Props.MapVisualManager;
import com.syrus.AMFICOM.client.UI.dialogs.EditorDialog;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

/**
 * ????? ???????????? ??? ?????????? ?????????????? ????? ?? ???????
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/05/27 15:14:56 $
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

		EditorDialog dialog = new EditorDialog(
				LangModelMap.getString("MapProperties"), 
				true, 
				this.map, 
				MapVisualManager.getInstance().getGeneralPropertiesPanel());

		dialog.setVisible(true);

		if(dialog.ifAccept()) {
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
