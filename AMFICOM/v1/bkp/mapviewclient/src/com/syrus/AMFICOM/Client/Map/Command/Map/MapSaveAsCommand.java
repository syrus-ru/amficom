/*
 * $Id: MapSaveAsCommand.java,v 1.17 2005/05/27 15:14:55 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;

/**
 * ????? $RCSfile: MapSaveAsCommand.java,v $ ???????????? ??? ?????????? 
 * ?????????????? ????? ? ????? ??????
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class MapSaveAsCommand extends AbstractCommand {
	Map map;

	Map newMap;

	ApplicationContext aContext;

	public MapSaveAsCommand(Map map, ApplicationContext aContext) {
		this.map = map;
		this.aContext = aContext;
	}

	public void execute() {
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();

		try {
			this.newMap = Map.createInstance(userId, domainId, this.map
					.getName()
					+ "(Copy)", "");
		} catch(CreateObjectException e) {
			e.printStackTrace();
			return;
		}

		this.aContext.getDispatcher().firePropertyChange(
				new StatusMessageEvent(
						this,
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelMap.getString("MapSaving")));

		EditorDialog dialog = new EditorDialog(
				LangModelMap.getString("MapProperties"),
				true,
				this.newMap,
				MapVisualManager.getInstance().getGeneralPropertiesPanel());

		dialog.setVisible(true);

		if(dialog.ifAccept()) {
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
				e.printStackTrace();
			}
			try {
				// save newMap
				StorableObjectPool.flush(this.newMap.getId(), true);
			} catch(ApplicationException e) {
				e.printStackTrace();
			}

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

	public Map getMap() {
		return this.map;
	}

	public Map getNewMap() {
		return this.newMap;
	}

}
